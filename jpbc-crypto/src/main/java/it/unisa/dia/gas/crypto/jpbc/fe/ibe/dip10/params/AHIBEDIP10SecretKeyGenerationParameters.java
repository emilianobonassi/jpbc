package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10SecretKeyGenerationParameters extends KeyGenerationParameters {

    private AHIBEDIP10MasterSecretKeyParameters masterSecretKey;
    private AHIBEDIP10PublicKeyParameters publicKey;
    private Element[] ids;


    public AHIBEDIP10SecretKeyGenerationParameters(
            AHIBEDIP10MasterSecretKeyParameters masterSecretKey,
            AHIBEDIP10PublicKeyParameters publicKey,
            Element[] ids) {

        super(null, 12);
        this.masterSecretKey = masterSecretKey;
        this.publicKey = publicKey;
        this.ids = ElementUtils.cloneImmutable(ids);
    }


    public AHIBEDIP10MasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public AHIBEDIP10PublicKeyParameters getPublicKey() {
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
