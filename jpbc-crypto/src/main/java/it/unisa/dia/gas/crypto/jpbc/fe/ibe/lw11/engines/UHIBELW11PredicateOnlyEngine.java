package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.engines;

import it.unisa.dia.gas.crypto.jpbc.cipher.PredicateOnlyPairingAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11PredicateOnlyEngine extends PredicateOnlyPairingAsymmetricBlockCipher {

    private int depth;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof UHIBELW11EncryptionParameters))
                throw new IllegalArgumentException("UHIBELW11EncryptionParameters are required for encryption.");

            this.pairing = PairingFactory.getPairing(((UHIBELW11EncryptionParameters) key).getPublicKey().getParameters());
            this.depth = ((UHIBELW11EncryptionParameters) key).getLength();
        } else {
            if (!(key instanceof UHIBELW11SecretKeyParameters))
                throw new IllegalArgumentException("UHIBELW11SecretKeyParameters are required for decryption.");

            this.pairing = PairingFactory.getPairing(((UHIBELW11SecretKeyParameters) key).getParameters());
            this.depth = ((UHIBELW11SecretKeyParameters) key).getLength();
        }

        this.outBytes = pairing.getGT().getLengthInBytes() +
                pairing.getG1().getLengthInBytes() +
                (depth * 3 * pairing.getG1().getLengthInBytes());
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof UHIBELW11SecretKeyParameters) {
            // Decrypt
            UHIBELW11SecretKeyParameters sk = (UHIBELW11SecretKeyParameters) key;

            PairingStreamReader streamParser = new PairingStreamReader(pairing, in, inOff);
            Element C = streamParser.readGTElement();
            Element C0 = streamParser.readG1Element();

            // Run the decryption
            Element numerator = pairing.getGT().newOneElement();
            Element denominator = pairing.getGT().newOneElement();
            for (int i = 0; i < depth; i++) {
                numerator
                        .mul(pairing.pairing(C0, sk.getK0At(i)))
                        .mul(pairing.pairing(streamParser.readG1Element(), sk.getK2At(i))); // C2

                denominator
                        .mul(pairing.pairing(streamParser.readG1Element(), sk.getK1At(i)))  // C1
                        .mul(pairing.pairing(streamParser.readG1Element(), sk.getK3At(i))); // C3
            }
            Element M = C.div(numerator.div(denominator));

            return new byte[]{(byte) (M.isOne() ? 1 : 0)};
        } else {
            // Encrypt the message under the specified attributes and convert to byte array
            UHIBELW11EncryptionParameters encParams = (UHIBELW11EncryptionParameters) key;
            UHIBELW11PublicKeyParameters pk = encParams.getPublicKey();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                Element s = pairing.getZr().newRandomElement();

                Element C = pk.getOmega().powZn(s);
                Element C0 = pk.getG().powZn(s);

                bytes.write(C.toBytes());
                bytes.write(C0.toBytes());

                for (int i = 0; i < depth; i++) {
                    Element t = pairing.getZr().newRandomElement();

                    // Computes C_{i,1}, C_{i,2}, C_{i,3} and writes them to the byte stream
                    Element C1 = pk.getW().powZn(s).mul(pk.getV().powZn(t));
                    Element C2 = pk.getG().powZn(t);
                    Element C3 = pk.getU().powZn(encParams.getIdAt(i)).mul(pk.getH()).powZn(t);

                    bytes.write(C2.toBytes());
                    bytes.write(C1.toBytes());
                    bytes.write(C3.toBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bytes.toByteArray();
        }
    }


}
