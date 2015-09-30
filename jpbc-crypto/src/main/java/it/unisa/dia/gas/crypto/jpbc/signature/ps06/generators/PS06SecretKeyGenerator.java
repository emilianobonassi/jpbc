package it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06SecretKeyGenerator implements CipherParametersGenerator {
    private PS06SecretKeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (PS06SecretKeyGenerationParameters) param;
    }

    public CipherParameters generateKey() {
        // get params
        PS06Parameters parameters = param.getPublicKey().getParameters();
        PS06PublicKeyParameters pk = param.getPublicKey();
        PS06MasterSecretKeyParameters msk = param.getMasterSecretKey();
        String identity = param.getIdentity();

        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
        Element g = parameters.getG();

        // compute secret key
        Element r = pairing.getZr().newRandomElement();

        Element idEncoding = pk.getuPrime();
        for (int i = 0; i < identity.length(); i++) {
            if (identity.charAt(i) == '1')
                idEncoding = idEncoding.mul(pk.getUAt(i));
        }

        Element d1 = msk.getMsk().mul(idEncoding.powZn(r)).getImmutable();
        Element d2 = g.powZn(r).getImmutable();

        return new PS06SecretKeyParameters(pk, identity, d1, d2);
    }

}