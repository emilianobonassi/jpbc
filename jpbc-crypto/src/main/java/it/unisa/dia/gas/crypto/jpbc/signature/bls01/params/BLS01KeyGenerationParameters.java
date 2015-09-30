package it.unisa.dia.gas.crypto.jpbc.signature.bls01.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01KeyGenerationParameters extends KeyGenerationParameters {

    private BLS01Parameters params;

    public BLS01KeyGenerationParameters(SecureRandom random, BLS01Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public BLS01Parameters getParameters() {
        return params;
    }

}