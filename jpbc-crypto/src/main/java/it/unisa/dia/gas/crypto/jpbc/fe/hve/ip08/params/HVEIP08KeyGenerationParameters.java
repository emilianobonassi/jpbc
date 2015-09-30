package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08KeyGenerationParameters extends KeyGenerationParameters {

    private HVEIP08Parameters params;

    public HVEIP08KeyGenerationParameters(SecureRandom random, HVEIP08Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public HVEIP08Parameters getParameters() {
        return params;
    }

}