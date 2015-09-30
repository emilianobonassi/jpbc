package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11DelegateGenerationParameters extends KeyGenerationParameters {

    private UHIBELW11PublicKeyParameters publicKey;
    private UHIBELW11SecretKeyParameters secretKey;
    private Element id;


    public UHIBELW11DelegateGenerationParameters(UHIBELW11PublicKeyParameters publicKey, UHIBELW11SecretKeyParameters secretKey, Element id) {
        super(null, 12);

        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.id = id.getImmutable();
    }

    public UHIBELW11PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public UHIBELW11SecretKeyParameters getSecretKey() {
        return secretKey;
    }

    public Element getId() {
        return id;
    }
}
