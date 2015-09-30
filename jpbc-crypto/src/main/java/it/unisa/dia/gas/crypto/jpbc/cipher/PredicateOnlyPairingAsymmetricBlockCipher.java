package it.unisa.dia.gas.crypto.jpbc.cipher;

import it.unisa.dia.gas.crypto.fe.PredicateOnlyEncryptionScheme;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class PredicateOnlyPairingAsymmetricBlockCipher extends PairingAsymmetricBlockCipher
        implements PredicateOnlyEncryptionScheme {

    /**
     * Return the maximum size for an input block to this engine.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption)
            return 0;

        return outBytes;
    }

    /**
     * Return the maximum size for an output block to this engine.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption)
            return outBytes;

        return 1;
    }

    public byte[] process() throws InvalidCipherTextException {
        return processBlock(new byte[0], 0, 0);
    }

    public boolean evaluate(byte[] in, int inOff, int len) throws InvalidCipherTextException {
        return processBlock(in, 0, len)[0] == 1;
    }

    public boolean evaluate(byte[] in) throws InvalidCipherTextException {
        return processBlock(in, 0, in.length)[0] == 1;
    }
}
