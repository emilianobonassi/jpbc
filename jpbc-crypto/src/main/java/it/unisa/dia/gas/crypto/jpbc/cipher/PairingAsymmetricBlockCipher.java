package it.unisa.dia.gas.crypto.jpbc.cipher;

import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class PairingAsymmetricBlockCipher implements AsymmetricBlockCipher {
    
    protected CipherParameters key;
    protected boolean forEncryption;

    protected int inBytes = 0;
    protected int outBytes = 0;

    protected Pairing pairing;


    /**
     * Return the maximum size for an input block to this engine.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption) {
            return inBytes;
        }

        return outBytes;
    }

    /**
     * Return the maximum size for an output block to this engine.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption) {
            return outBytes;
        }

        return inBytes;
    }

    /**
     * initialise the cipher engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary cipher key parameters.
     */
    public void init(boolean forEncryption, CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = p.getParameters();
        } else {
            this.key = param;
        }

        this.forEncryption = forEncryption;

        initialize();
    }

    /**
     * Process a single block using the basic cipher algorithm.
     *
     * @param in    the input array.
     * @param inOff the offset into the input buffer where the data starts.
     * @param inLen the length of the data to be processed.
     * @return the result of the cipher process.
     * @throws org.bouncycastle.crypto.DataLengthException the input block is too large.
     */
    public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (key == null)
            throw new IllegalStateException("Engine not initialized");

        int maxLength = getInputBlockSize();

        if (inLen < maxLength)
            throw new DataLengthException("Input too small for the cipher.");

        return process(in, inOff, inLen);
    }


    public abstract void initialize();

    public abstract byte[] process(byte[] in, int inOff, int inLen) throws InvalidCipherTextException;

}
