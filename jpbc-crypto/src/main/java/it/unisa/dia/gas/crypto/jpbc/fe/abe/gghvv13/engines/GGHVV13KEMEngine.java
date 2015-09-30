package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.engines;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13SecretKeyParameters;
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
public class GGHVV13KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof GGHVV13EncryptionParameters))
                throw new IllegalArgumentException("GGHVV13EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof GGHVV13SecretKeyParameters))
                throw new IllegalArgumentException("GGHVV13SecretKeyParameters are required for decryption.");
        }

        GGHVV13KeyParameters gghswKey = (GGHVV13KeyParameters) key;

        this.pairing = gghswKey.getParameters().getPairing();
        this.keyBytes = pairing.getFieldAt(pairing.getDegree()).getCanonicalRepresentationLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GGHVV13SecretKeyParameters) {
            // Decrypt
            GGHVV13SecretKeyParameters sk = (GGHVV13SecretKeyParameters) key;

            // Load the ciphertext
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);

            String assignment = reader.readString();
            Element gs = reader.readG1Element();
            Element gamma1 = reader.readG1Element();

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
                            Element t1 = pairing.getG1().newOneElement();
                            for (int i = 0, n = assignment.length(); i < n; i++) {
                                if (assignment.charAt(i) == '1')
                                    t1.mul(sk.getMAt(i + 1, index));
                            }

                            Element t2 = sk.getMAt(0, index);

                            evaluations.put(index,
                                    pairing.pairing(gs, t1).mul(pairing.pairing(gamma1, t2))
                            );
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
            GGHVV13EncryptionParameters encKey = (GGHVV13EncryptionParameters) key;
            GGHVV13PublicKeyParameters publicKey = encKey.getPublicKey();
            String assignment = encKey.getAssignment();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {
                // Sample the randomness
                Element s = pairing.getZr().newRandomElement().getImmutable();

                Element mask = publicKey.getH().powZn(s);
                writer.write(mask.toCanonicalRepresentation());

                writer.write(assignment);
                writer.write(pairing.getFieldAt(1).newElement().powZn(s));

                Element gamma1 = pairing.getFieldAt(1).newZeroElement();
                for (int i = 0, n = assignment.length(); i < n; i++) {
                    if (assignment.charAt(i) == '1')
                        gamma1.mul(publicKey.getHAt(i));
                }
                gamma1.powZn(s);
                writer.write(gamma1);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}