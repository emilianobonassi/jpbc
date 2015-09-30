package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13ReKeyPairGenerationParameters extends KeyGenerationParameters {

    private WTORGVW13Parameters params;

    private WTORGVW13PublicKeyParameters leftPk;
    private WTORGVW13SecretKeyParameters leftSk;

    private WTORGVW13PublicKeyParameters rightPk;

    private WTORGVW13PublicKeyParameters targetPk;


    public WTORGVW13ReKeyPairGenerationParameters(SecureRandom random, int strength, WTORGVW13Parameters params,
                                                  WTORGVW13PublicKeyParameters leftPk,
                                                  WTORGVW13SecretKeyParameters leftSk,
                                                  WTORGVW13PublicKeyParameters rightPk,
                                                  WTORGVW13PublicKeyParameters targetPk) {
        super(random, strength);

        this.params = params;

        this.leftPk = leftPk;
        this.leftSk = leftSk;

        this.rightPk = rightPk;

        this.targetPk = targetPk;
    }

    public WTORGVW13Parameters getParameters() {
        return params;
    }


    public WTORGVW13Parameters getParams() {
        return params;
    }

    public WTORGVW13PublicKeyParameters getLeftPk() {
        return leftPk;
    }

    public WTORGVW13SecretKeyParameters getLeftSk() {
        return leftSk;
    }

    public WTORGVW13PublicKeyParameters getRightPk() {
        return rightPk;
    }

    public WTORGVW13PublicKeyParameters getTargetPk() {
        return targetPk;
    }
}
