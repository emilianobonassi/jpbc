package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.crypto.dfa.DFA;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12SecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulatorFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.io.IOException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof RLW12EncryptionParameters))
                throw new IllegalArgumentException("RLW12EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof RLW12SecretKeyParameters))
                throw new IllegalArgumentException("RLW12SecretKeyParameters are required for decryption.");
        }

        RLW12KeyParameters rlKey = (RLW12KeyParameters) key;
        this.pairing = PairingFactory.getPairing(rlKey.getParameters().getParameters());

        this.keyBytes = pairing.getGT().getLengthInBytes();
//        this.outBytes = 2 * pairing.getGT().getLengthInBytes() + N * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof RLW12SecretKeyParameters) {
            // Decrypt
            RLW12SecretKeyParameters sk = (RLW12SecretKeyParameters) key;

            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);
            String w = reader.readString();
            Element[] wEnc = reader.readG1Elements(((inLen - pairing.getGT().getLengthInBytes()) / pairing.getG1().getLengthInBytes()));
            Element cm = reader.readGTElement();

            // Run the decryption...
            // Init
            PairingAccumulator accumulator = PairingAccumulatorFactory.getInstance().getPairingMultiplier(pairing);

            int index = 0;
            accumulator.addPairing(wEnc[index++], sk.getkStart(0))
                    .addPairingInverse(wEnc[index++], sk.getkStart(1));

            // Run
            int currentState = sk.getDfa().getInitialState(); // Initial state

            for (int i = 0; i < w.length(); i++) {
                DFA.Transition transition = sk.getDfa().getTransition(currentState, w.charAt(i));

                accumulator.addPairing(wEnc[index - 2], sk.getkTransition(transition, 0))
                        .addPairing(wEnc[index++], sk.getkTransition(transition, 2))
                        .addPairingInverse(wEnc[index++], sk.getkTransition(transition, 1));

                currentState = transition.getTo();
            }

            // Finalize
            if (sk.getDfa().isFinalState(currentState)) {
                accumulator.addPairingInverse(wEnc[index++], sk.getkEnd(currentState, 0))
                        .addPairing(wEnc[index], sk.getkEnd(currentState, 1));

                // Recover the message...
                Element M = cm.div(accumulator.awaitResult());

                return M.toBytes();
            } else {
                return cm.toBytes();
            }
        } else {
            Element M = pairing.getGT().newRandomElement();

            // Encrypt the massage under the specified attributes
            RLW12EncryptionParameters encKey = (RLW12EncryptionParameters) key;
            RLW12PublicKeyParameters publicKey = encKey.getPublicKey();
            String w = encKey.getW();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {
                // Store M
                writer.write(M);

                // Store the ciphertext

                // Store w
                writer.write(w);

                // Initialize
                Element s0 = pairing.getZr().newRandomElement();
                writer.write(publicKey.getParameters().getG().powZn(s0));
                writer.write(publicKey.gethStart().powZn(s0));

                // Sequence
                Element sPrev = s0;
                for (int i = 0, l = w.length(); i < l; i++) {
                    Element sNext = pairing.getZr().newRandomElement();

                    writer.write(publicKey.getParameters().getG().powZn(sNext));
                    writer.write(publicKey.getHAt(w.charAt(i)).powZn(sNext).mul(publicKey.getZ().powZn(sPrev)));

                    sPrev = sNext;
                }

                // Finalize
                writer.write(publicKey.getParameters().getG().powZn(sPrev));
                writer.write(publicKey.gethEnd().powZn(sPrev));

                // Store the masked message
                writer.write(publicKey.getOmega().powZn(sPrev).mul(M));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return writer.toBytes();
        }
    }


}