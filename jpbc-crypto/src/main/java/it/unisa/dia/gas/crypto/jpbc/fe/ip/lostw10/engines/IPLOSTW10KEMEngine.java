package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10SecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.product.ProductPairing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10KEMEngine extends PairingKeyEncapsulationMechanism {

    private int n;
    private Pairing productPairing;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof IPLOSTW10EncryptionParameters))
                throw new IllegalArgumentException("IPLOSTW10EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof IPLOSTW10SecretKeyParameters))
                throw new IllegalArgumentException("IPLOSTW10SecretKeyParameters are required for decryption.");
        }

        IPLOSTW10KeyParameters ipKey = (IPLOSTW10KeyParameters) key;
        this.n = ipKey.getParameters().getN();
        int N = (2 * n + 3);
        this.pairing = PairingFactory.getPairing(ipKey.getParameters().getParameters());
        this.productPairing = new ProductPairing(null, pairing, N);

        this.keyBytes = pairing.getGT().getLengthInBytes();
        this.outBytes = 2 * pairing.getGT().getLengthInBytes() + N * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof IPLOSTW10SecretKeyParameters) {
            // Decrypt

            // Convert bytes to Elements...
            Element c1 = productPairing.getG1().newElement();
            inOff += c1.setFromBytes(in, inOff);

            Element c2 = pairing.getGT().newElement();
            c2.setFromBytes(in, inOff);

            // Run the decryption
            IPLOSTW10SecretKeyParameters secretKey = (IPLOSTW10SecretKeyParameters) key;
            Element result = c2.div(productPairing.pairing(c1, secretKey.getK()));

            return result.toBytes();
        } else {
            Element M = pairing.getGT().newRandomElement();

            // Encrypt the massage under the specified attributes
            IPLOSTW10EncryptionParameters encKey = (IPLOSTW10EncryptionParameters) key;
            IPLOSTW10PublicKeyParameters pub = encKey.getPublicKey();

            Element delta1 = pairing.getZr().newRandomElement();
            Element delta2 = pairing.getZr().newRandomElement();

            Element alpha = pairing.getZr().newRandomElement();

            Element c1 = pub.getBAt(0).duplicate().powZn(encKey.getXAt(0));
            for (int i = 1; i < n; i++) {
                c1.add(pub.getBAt(i).powZn(encKey.getXAt(i)));
            }
            c1.mulZn(delta1)
                    .add(pub.getBAt(n).powZn(alpha))
                    .add(pub.getBAt(n + 1).powZn(delta2));

            Element c2 = pub.getSigma().powZn(alpha).mul(M);

            // Convert to byte array
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                bytes.write(M.toBytes());
                bytes.write(c1.toBytes());
                bytes.write(c2.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bytes.toByteArray();
        }
    }


}