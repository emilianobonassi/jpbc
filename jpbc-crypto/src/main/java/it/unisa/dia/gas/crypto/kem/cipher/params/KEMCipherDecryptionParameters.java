package it.unisa.dia.gas.crypto.kem.cipher.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.AlgorithmParameters;
import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class KEMCipherDecryptionParameters extends KEMCipherParameters {
    private CipherParameters kemCipherParameters;
    private byte[] encapsulation;
    private int cipherKeyStrength;


    public KEMCipherDecryptionParameters(CipherParameters kemCipherParameters, byte[] encapsulation, int cipherKeyStrength) {
        super(null);
        this.kemCipherParameters = kemCipherParameters;
        this.encapsulation = Arrays.copyOf(encapsulation, encapsulation.length);
        this.cipherKeyStrength = cipherKeyStrength;
    }

    public KEMCipherDecryptionParameters(AlgorithmParameters algorithmParameters, CipherParameters kemCipherParameters, byte[] encapsulation, int cipherKeyStrength) {
        super(algorithmParameters);
        this.kemCipherParameters = kemCipherParameters;
        this.encapsulation = Arrays.copyOf(encapsulation, encapsulation.length);
        this.cipherKeyStrength = cipherKeyStrength;
    }


    public CipherParameters getKemCipherParameters() {
        return kemCipherParameters;
    }

    public byte[] getEncapsulation() {
        return Arrays.copyOf(encapsulation, encapsulation.length);
    }

    public int getCipherKeyStrength() {
        return cipherKeyStrength;
    }
}
