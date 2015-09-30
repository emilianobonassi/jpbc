package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06SecretKeyParameters extends PS06KeyParameters {
    private PS06PublicKeyParameters publicKey;
    private String identity;
    private Element d1, d2;

    public PS06SecretKeyParameters(PS06PublicKeyParameters publicKey, String identity, Element d1, Element d2) {
        super(true, publicKey.getParameters());

        this.publicKey = publicKey;
        this.identity = identity;
        this.d1 = d1.getImmutable();
        this.d2 = d2.getImmutable();
    }

    public PS06PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getIdentity() {
        return identity;
    }

    public Element getD1() {
        return d1;
    }

    public Element getD2() {
        return d2;
    }
}