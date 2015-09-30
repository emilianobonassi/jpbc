package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12KeyPairGenerationParameters extends KeyGenerationParameters {

    private RLW12Parameters params;

    public RLW12KeyPairGenerationParameters(SecureRandom random, RLW12Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public RLW12Parameters getParameters() {
        return params;
    }

}
