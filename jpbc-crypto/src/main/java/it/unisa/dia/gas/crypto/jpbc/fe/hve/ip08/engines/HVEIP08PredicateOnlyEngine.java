package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines;

import it.unisa.dia.gas.crypto.jpbc.cipher.PredicateOnlyPairingAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.DataLengthException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08PredicateOnlyEngine extends PredicateOnlyPairingAsymmetricBlockCipher {

    private int n;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof HVEIP08EncryptionParameters))
                throw new IllegalArgumentException("HVEIP08EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof HVEIP08SecretKeyParameters))
                throw new IllegalArgumentException("HVEIP08SecretKeyParameters are required for decryption.");
        }

        HVEIP08KeyParameters hveKey = (HVEIP08KeyParameters) key;
        this.pairing = PairingFactory.getPairing(hveKey.getParameters().getParameters());
        this.n = hveKey.getParameters().getN();

        this.outBytes = (2 * n) * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof HVEIP08SecretKeyParameters) {
            // Test/Match

            // If the key is for all stars then the match predicate is
            // always satisfied.
            HVEIP08SecretKeyParameters secretKey = (HVEIP08SecretKeyParameters) key;
            if (secretKey.isAllStar())
                return new byte[]{1};

            // Convert bytes to Elements...
            int offset = inOff;

            // Load Xs, Ws..
            List<Element> X = new ArrayList<Element>(n);
            List<Element> W = new ArrayList<Element>(n);
            for (int i = 0; i < n; i++) {
                Element x = pairing.getG1().newElement();
                offset += x.setFromBytes(in, offset);
                X.add(x);

                Element w = pairing.getG1().newElement();
                offset += w.setFromBytes(in, offset);
                W.add(w);
            }

            // Run the rest
            Element result = pairing.getGT().newOneElement();
            if (secretKey.isPreProcessed()) {
                for (int i = 0; i < secretKey.getParameters().getN(); i++) {
                    if (!secretKey.isStar(i)) {
                        result.mul(
                                secretKey.getPreYAt(i).pairing(X.get(i))
                        ).mul(
                                secretKey.getPreLAt(i).pairing(W.get(i))
                        );
                    }
                }
            } else {
                for (int i = 0; i < secretKey.getParameters().getN(); i++) {
                    if (!secretKey.isStar(i)) {
                        result.mul(
                                pairing.pairing(secretKey.getYAt(i), X.get(i))
                        ).mul(
                                pairing.pairing(secretKey.getLAt(i), W.get(i))
                        );
                    }
                }
            }
            return new byte[]{(byte) (result.isOne() ? 1 : 0)};
        } else {
            // Encryption
            if (inLen > inBytes || inLen < inBytes)
                throw new DataLengthException("input must be of size " + inBytes);

            // Encrypt the message under the specified attributes
            HVEIP08EncryptionParameters encParams = (HVEIP08EncryptionParameters) key;
            HVEIP08PublicKeyParameters pk = encParams.getPublicKey();

            Element s = pairing.getZr().newRandomElement().getImmutable();

            List<Element> elements = new ArrayList<Element>();
            for (int i = 0; i < n; i++) {
                Element si = pairing.getZr().newElement().setToRandom();
                Element sMinusSi = s.sub(si);

                int j = encParams.getAttributeAt(i);

                elements.add(pk.getElementPowTAt(i, j).powZn(sMinusSi));  // X_i
                elements.add(pk.getElementPowVAt(i, j).powZn(si));        // W_i
            }

            // Convert to byte array
            ByteArrayOutputStream outputStream;
            try {
                outputStream = new ByteArrayOutputStream(getOutputBlockSize());
                for (Element element : elements)
                    outputStream.write(element.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return outputStream.toByteArray();
        }
    }


}