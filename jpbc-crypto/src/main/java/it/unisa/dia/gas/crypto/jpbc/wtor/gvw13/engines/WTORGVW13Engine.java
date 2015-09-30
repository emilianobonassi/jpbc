package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.engines;

import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13RecodeParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class WTORGVW13Engine implements AsymmetricBlockCipher {

    private CipherParameters param;
    private Pairing pairing;
    private int inputBlockSize, outputBlockSize;

    public void init(boolean forEncryption, CipherParameters param) {
        this.param = param;

        WTORGVW13KeyParameters keyParameters = (WTORGVW13KeyParameters) param;
        pairing = PairingFactory.getPairing(keyParameters.getParameters().getParameters());

        if (param instanceof WTORGVW13PublicKeyParameters) {
            inputBlockSize = pairing.getZr().getLengthInBytes();
            outputBlockSize = ((WTORGVW13PublicKeyParameters) param).getLevel() == 0 ?
                    pairing.getG1().getLengthInBytes() :
                    pairing.getGT().getLengthInBytes();
        } else if (param instanceof WTORGVW13RecodeParameters) {
            inputBlockSize = pairing.getG1().getLengthInBytes() + pairing.getGT().getLengthInBytes();
            outputBlockSize = pairing.getGT().getLengthInBytes();
        } else
            throw new IllegalArgumentException("Invalid key parameters!");
    }


    public int getInputBlockSize() {
        return inputBlockSize;
    }

    public int getOutputBlockSize() {
        return outputBlockSize;
    }

    public byte[] processBlock(byte[] in, int inOff, int len) throws InvalidCipherTextException {
        if (param instanceof WTORGVW13PublicKeyParameters) {
            WTORGVW13PublicKeyParameters keyParameters = (WTORGVW13PublicKeyParameters) param;

            // Read Input
            Element s = new PairingStreamReader(pairing, in, inOff).readG1Element();

            // Encode
            Element result;
            if (keyParameters.getLevel() == 0) {
                result = keyParameters.getLeft().powZn(s);
            } else {
                result = pairing.pairing(
                        keyParameters.getParameters().getG1a(),
                        keyParameters.getRight()
                ).powZn(s);
            }

            return result.toBytes();
        } else {
            WTORGVW13RecodeParameters keyParameters = (WTORGVW13RecodeParameters) param;

            // Read Input
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);
            Element c0 = reader.readG1Element();
            Element c1 = reader.readGTElement();

            // Recode
            Element result =pairing.pairing(c0, keyParameters.getRk()).mul(c1);

            return result.toBytes();
       }

    }
}
