package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.engines;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13SecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof GGHSW13EncryptionParameters))
                throw new IllegalArgumentException("GGHSW13EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof GGHSW13SecretKeyParameters))
                throw new IllegalArgumentException("GGHSW13SecretKeyParameters are required for decryption.");
        }

        GGHSW13KeyParameters gghswKey = (GGHSW13KeyParameters) key;

        this.pairing = gghswKey.getParameters().getPairing();
        this.keyBytes = pairing.getFieldAt(pairing.getDegree()).getCanonicalRepresentationLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GGHSW13SecretKeyParameters) {
            // Decrypt
            GGHSW13SecretKeyParameters sk = (GGHSW13SecretKeyParameters) key;

            // Load the ciphertext
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);

            String assignment = reader.readString();
            Element gs = reader.readG1Element();

            // compute hamming with
            Element[] cs = new Element[sk.getParameters().getN()];
            for (int i = 0; i < assignment.length(); i++)
                if (assignment.charAt(i) == '1')
                    cs[i] = reader.readG1Element();

            // Evaluate the circuit against the ciphertext
            Circuit circuit = sk.getCircuit();
            Element root = pairing.pairing(sk.getKeyElementsAt(-1)[0], gs);

            // evaluate the circuit
            Map<Integer, Element> evaluations = new HashMap<Integer, Element>();
            for (Circuit.Gate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.set(assignment.charAt(index) == '1');

                        if (gate.isSet()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(keys[0], gs);
                            Element t2 = pairing.pairing(keys[1], cs[index]);

                            evaluations.put(index, t1.mul(t2));
                        }

                        break;

                    case OR:
                        gate.evaluate();

                        if (gate.getInputAt(0).isSet()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(0).getIndex()),
                                    keys[0]
                            );

                            Element t2 = pairing.pairing(
                                    keys[2],
                                    gs
                            );

                            evaluations.put(index, t1.mul(t2));
                        } else if (gate.getInputAt(1).isSet()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(1).getIndex()),
                                    keys[1]
                            );

                            Element t2 = pairing.pairing(
                                    keys[3],
                                    gs
                            );

                            evaluations.put(index, t1.mul(t2));
                        }

                        break;

                    case AND:
                        gate.evaluate();

                        if (gate.isSet()) {
                            Element[] keys = sk.getKeyElementsAt(index);
                            Element t1 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(0).getIndex()),
                                    keys[0]
                            );

                            Element t2 = pairing.pairing(
                                    evaluations.get(gate.getInputAt(1).getIndex()),
                                    keys[1]
                            );

                            Element t3 = pairing.pairing(
                                    keys[2],
                                    gs
                            );

                            evaluations.put(index, t1.mul(t2).mul(t3));
                        }

                        break;
                }
            }

            if (circuit.getOutputGate().isSet()) {
                Element result = root.mul(evaluations.get(circuit.getOutputGate().getIndex()));

                return result.toCanonicalRepresentation();
            } else
                return new byte[]{-1};
        } else {
            // Encrypt the massage under the specified attributes
            GGHSW13EncryptionParameters encKey = (GGHSW13EncryptionParameters) key;
            GGHSW13PublicKeyParameters publicKey = encKey.getPublicKey();
            String assignment = encKey.getAssignment();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {
                // Sample the randomness
                Element s = pairing.getZr().newRandomElement().getImmutable();

                Element mask = publicKey.getH().powZn(s);
                writer.write(mask.toCanonicalRepresentation());

                writer.write(assignment);
                writer.write(pairing.getFieldAt(1).newElement().powZn(s));
                int n = publicKey.getParameters().getN();
                for (int i = 0; i < n; i++) {
                    if (assignment.charAt(i) == '1')
                        writer.write(publicKey.getHAt(i).powZn(s));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}