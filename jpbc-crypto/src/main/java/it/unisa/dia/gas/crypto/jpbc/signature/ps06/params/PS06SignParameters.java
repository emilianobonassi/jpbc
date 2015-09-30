package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06SignParameters extends PS06KeyParameters {
    private PS06SecretKeyParameters secretKey;


    public PS06SignParameters(PS06SecretKeyParameters secretKey) {
        super(true, secretKey.getParameters());

        this.secretKey = secretKey;
    }

    public PS06SecretKeyParameters getSecretKey() {
        return secretKey;
    }

}
