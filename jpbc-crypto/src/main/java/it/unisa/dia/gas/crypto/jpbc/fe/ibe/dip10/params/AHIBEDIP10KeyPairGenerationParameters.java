package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10KeyPairGenerationParameters extends KeyGenerationParameters {

    private int bitLength;
    private int length;


    public AHIBEDIP10KeyPairGenerationParameters(int bitLength, int length) {
        super(null, 12);
        this.bitLength = bitLength;
        this.length = length;
    }
    

    public int getBitLength() {
        return bitLength;
    }

    public int getLength() {
        return length;
    }
}
