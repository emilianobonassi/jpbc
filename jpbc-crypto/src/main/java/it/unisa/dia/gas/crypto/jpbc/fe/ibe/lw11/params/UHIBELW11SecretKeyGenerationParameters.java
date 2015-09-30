package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11SecretKeyGenerationParameters extends KeyGenerationParameters {

    private UHIBELW11MasterSecretKeyParameters masterSecretKey;
    private UHIBELW11PublicKeyParameters publicKey;
    private Element[] ids;


    public UHIBELW11SecretKeyGenerationParameters(
            UHIBELW11MasterSecretKeyParameters masterSecretKeyParameters,
            UHIBELW11PublicKeyParameters publicKeyParameters,
            Element[] ids) {

        super(null, 12);

        this.masterSecretKey = masterSecretKeyParameters;
        this.publicKey = publicKeyParameters;
        this.ids = ElementUtils.cloneImmutable(ids);
    }


    public UHIBELW11MasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public UHIBELW11PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element getIdAt(int index) {
        return ids[index];
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public int getLength() {
        return ids.length;
    }
}
