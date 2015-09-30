package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11KeyPairGenerationParameters extends KeyGenerationParameters {

    private int bitLength;


    public UHIBELW11KeyPairGenerationParameters(int bitLength) {
        super(null, 12);
        this.bitLength = bitLength;
    }
    

    public int getBitLength() {
        return bitLength;
    }

}
