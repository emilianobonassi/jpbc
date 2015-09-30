package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10KeyGenerationParameters extends KeyGenerationParameters {

    private IPLOSTW10Parameters params;

    public IPLOSTW10KeyGenerationParameters(SecureRandom random, IPLOSTW10Parameters params) {
        super(random, params.getG().getField().getLengthInBytes());

        this.params = params;
    }

    public IPLOSTW10Parameters getParameters() {
        return params;
    }

}