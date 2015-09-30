package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06VerifyParameters extends PS06KeyParameters {
    private PS06PublicKeyParameters publicKey;
    private String identity;

    public PS06VerifyParameters(PS06PublicKeyParameters publicKey, String identity) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.identity = identity;
    }

    public PS06PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getIdentity() {
        return identity;
    }
}
