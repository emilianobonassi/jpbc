package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;


import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06SetupGenerationParameters extends KeyGenerationParameters {

    private PS06Parameters params;

    public PS06SetupGenerationParameters(SecureRandom random, PS06Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public PS06Parameters getParameters() {
        return params;
    }

}