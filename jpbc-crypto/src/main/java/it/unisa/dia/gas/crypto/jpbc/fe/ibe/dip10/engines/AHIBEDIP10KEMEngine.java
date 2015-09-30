package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10SecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof AHIBEDIP10EncryptionParameters))
                throw new IllegalArgumentException("AHIBEDIP10EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof AHIBEDIP10SecretKeyParameters))
                throw new IllegalArgumentException("AHIBEDIP10SecretKeyParameters are required for decryption.");
        }

        this.pairing = PairingFactory.getPairing(((AHIBEDIP10KeyParameters) key).getParameters());
        this.keyBytes = pairing.getGT().getLengthInBytes();
        this.outBytes = 2 * pairing.getGT().getLengthInBytes() + 2 * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof AHIBEDIP10SecretKeyParameters) {
            // decaps

            // Convert bytes to Elements...
            int offset = inOff;

            // Load C0...
            Element C0 = pairing.getGT().newElement();
            offset += C0.setFromBytes(in, offset);

            // Load C1...
            Element C1 = pairing.getG1().newElement();
            offset += C1.setFromBytes(in, offset);

            // Load C2...
            Element C2 = pairing.getG1().newElement();
            offset += C2.setFromBytes(in, offset);

            // Run the decryption
            AHIBEDIP10SecretKeyParameters sk = (AHIBEDIP10SecretKeyParameters) key;
            Element M = C0.mul(pairing.pairing(sk.getK12(), C2).mul(pairing.pairing(sk.getK11(), C1).invert()).invert());

            return M.toBytes();
        } else {
            // encaps
            Element M = pairing.getGT().newRandomElement().getImmutable();

            AHIBEDIP10EncryptionParameters encParams = (AHIBEDIP10EncryptionParameters) key;
            AHIBEDIP10PublicKeyParameters pk = encParams.getPublicKey();

            Element s = pairing.getZr().newRandomElement();

            Element C0 = M.mul(pk.getOmega().powZn(s));

            Element C1 = pairing.getG1().newOneElement();
            for (int i = 0; i < encParams.getLength(); i++) {
                C1.mul(pk.getUAt(i).powZn(encParams.getIdAt(i)));
            }
            C1.mul(pk.getT()).powZn(s).mul(ElementUtils.randomIn(pairing, pk.getY4()));

            Element C2 = pk.getY1().powZn(s).mul(ElementUtils.randomIn(pairing, pk.getY4()));

            // Convert to byte array
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                bytes.write(M.toBytes());
                bytes.write(C0.toBytes());
                bytes.write(C1.toBytes());
                bytes.write(C2.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bytes.toByteArray();
        }
    }


}