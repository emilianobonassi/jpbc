package it.unisa.dia.gas.crypto.kem.cipher.params;

import org.bouncycastle.crypto.CipherParameters;

import java.security.AlgorithmParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class KEMCipherEncryptionParameters extends KEMCipherParameters {
    private int cipherKeyStrength;
    private CipherParameters kemCipherParameters;


    public KEMCipherEncryptionParameters(int cipherKeyStrength, CipherParameters kemCipherParameters) {
        super(null);
        this.cipherKeyStrength = cipherKeyStrength;
        this.kemCipherParameters = kemCipherParameters;
    }

    public KEMCipherEncryptionParameters(AlgorithmParameters algorithmParameters, int cipherKeyStrength, CipherParameters kemCipherParameters) {
        super(algorithmParameters);
        this.cipherKeyStrength = cipherKeyStrength;
        this.kemCipherParameters = kemCipherParameters;
    }


    public int getCipherKeyStrength() {
        return cipherKeyStrength;
    }

    public CipherParameters getKemCipherParameters() {
        return kemCipherParameters;
    }
}
