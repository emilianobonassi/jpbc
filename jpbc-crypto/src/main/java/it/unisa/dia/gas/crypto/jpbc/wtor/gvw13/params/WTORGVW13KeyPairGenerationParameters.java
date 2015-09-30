package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13KeyPairGenerationParameters extends KeyGenerationParameters {

    private WTORGVW13Parameters params;
    private int level;

    public WTORGVW13KeyPairGenerationParameters(SecureRandom random, WTORGVW13Parameters params, int level) {
        super(random, params.getG1().getField().getLengthInBytes());

        this.params = params;
        this.level = level;
    }

    public WTORGVW13Parameters getParameters() {
        return params;
    }


    public int getLevel() {
        return level;
    }
}
