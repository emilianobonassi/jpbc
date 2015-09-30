package it.unisa.dia.gas.crypto.jpbc.kem;

import it.unisa.dia.gas.crypto.jpbc.cipher.PairingAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class PairingKeyEncapsulationMechanism extends PairingAsymmetricBlockCipher implements KeyEncapsulationMechanism {

    private static byte[] EMPTY = new byte[0];


    protected int keyBytes = 0;

    public int getKeyBlockSize() {
        return keyBytes;
    }

    public int getInputBlockSize() {
        if (forEncryption)
            return 0;

        return outBytes - keyBytes;
    }

    public int getOutputBlockSize() {
        if (forEncryption)
            return outBytes;

        return keyBytes;
    }


    public byte[] processBlock(byte[] in) throws InvalidCipherTextException {
        return processBlock(in, in.length, 0);
    }

    public byte[] process() throws InvalidCipherTextException {
        return processBlock(EMPTY, 0, 0);
    }

}
