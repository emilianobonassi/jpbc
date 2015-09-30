package it.unisa.dia.gas.crypto.cipher;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MultiBlockAsymmetricBlockCipher implements AsymmetricBlockCipher {

    protected AsymmetricBlockCipher cipher;
    protected BlockCipherPadding padding;

    protected boolean forEncryption;


    public MultiBlockAsymmetricBlockCipher(AsymmetricBlockCipher cipher) {
        this(cipher, null);
    }

    public MultiBlockAsymmetricBlockCipher(AsymmetricBlockCipher cipher, BlockCipherPadding padding) {
        this.cipher = cipher;
        this.padding = padding;
    }


    public void init(boolean forEncryption, CipherParameters parameters) {
        this.forEncryption = forEncryption;
        cipher.init(forEncryption, parameters);
        if (padding != null) {
            if (parameters instanceof ParametersWithRandom) {
                ParametersWithRandom p = (ParametersWithRandom) parameters;
                padding.init(p.getRandom());
            } else
                padding.init(new SecureRandom());
        }
    }

    public int getInputBlockSize() {
        return cipher.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return cipher.getOutputBlockSize();
    }

    public byte[] processBlock(byte[] in, int inOff, int len) throws InvalidCipherTextException {
        int inputBlockSize = getInputBlockSize();
        int inputOffset = inOff;
        byte[] buffer = new byte[inputBlockSize];

        int outputBlockSize = getOutputBlockSize();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((len / inputBlockSize + (len % inputBlockSize == 0 ? 0 : 1)) * outputBlockSize);

        while (inputOffset < len) {

            // Copy next piece of input...
            int bufLength = inputBlockSize;
            if (inputOffset + inputBlockSize > len)
                bufLength = len - inputOffset;
            System.arraycopy(in, inputOffset, buffer, 0, bufLength);
            inputOffset += bufLength;

            // Apply padding if necessary
            if (padding != null && forEncryption && bufLength < inputBlockSize) {
                padding.addPadding(buffer, bufLength);
                bufLength = inputBlockSize;
            }

            // Produce output for the current piece...
            byte[] outputBlock = cipher.processBlock(buffer, 0, bufLength);
            try {
                outputStream.write(outputBlock);
            } catch (IOException e) {
                e.printStackTrace();
                throw new InvalidCipherTextException(e.getMessage());
            }
        }

        byte[] output = outputStream.toByteArray();

        // Apply padding if necessary
        if (padding != null && !forEncryption) {
            int padCount = 0;
            try {
                padCount = padding.padCount(output);
                if (padCount > 0)
                    return Arrays.copyOf(output, output.length - padCount);
            } catch (InvalidCipherTextException e) {
                if (inputBlockSize % outputBlockSize != 0)
                    throw e;
            }
        }

        return output;
    }

}
