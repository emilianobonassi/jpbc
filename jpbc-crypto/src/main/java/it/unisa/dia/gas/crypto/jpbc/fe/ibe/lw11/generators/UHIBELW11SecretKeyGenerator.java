package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11SecretKeyGenerator implements CipherParametersGenerator {
    private KeyGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = keyGenerationParameters;
    }

    public CipherParameters generateKey() {
        if (params instanceof UHIBELW11SecretKeyGenerationParameters) {
            UHIBELW11SecretKeyGenerationParameters parameters = (UHIBELW11SecretKeyGenerationParameters) params;

            UHIBELW11PublicKeyParameters pk = parameters.getPublicKey();
            UHIBELW11MasterSecretKeyParameters msk = parameters.getMasterSecretKey();

            Pairing pairing = PairingFactory.getPairing(pk.getParameters());
            Field Zr = pairing.getZr();
            int length = parameters.getLength();

            Element[] K0s = new Element[length];
            Element[] K1s = new Element[length];
            Element[] K2s = new Element[length];
            Element[] K3s = new Element[length];

            Element lambdaSum = Zr.newZeroElement();
            int lastIndex = length - 1;
            for (int i = 0; i < lastIndex; i++) {
                Element lambda = Zr.newRandomElement();
                Element y = Zr.newRandomElement();
                Element r = Zr.newRandomElement();

                lambdaSum.add(lambda);

                K0s[i] = pk.getG().powZn(lambda).mul(pk.getW().powZn(y));
                K1s[i] = pk.getG().powZn(y);
                K2s[i] = pk.getV().powZn(y).mul(pk.getU().powZn(parameters.getIdAt(i)).mul(pk.getH()).powZn(r));
                K3s[i] = pk.getG().powZn(r);
            }

            Element y = Zr.newRandomElement();
            Element r = Zr.newRandomElement();

            K0s[lastIndex] = pk.getG().powZn(msk.getAlpha().sub(lambdaSum)).mul(pk.getW().powZn(y));
            K1s[lastIndex] = pk.getG().powZn(y);
            K2s[lastIndex] = pk.getV().powZn(y).mul(pk.getU().powZn(parameters.getIdAt(lastIndex)).mul(pk.getH()).powZn(r));
            K3s[lastIndex] = pk.getG().powZn(r);

            return new UHIBELW11SecretKeyParameters(
                    pk.getParameters(),
                    K0s, K1s, K2s, K3s,
                    parameters.getIds()
            );
        } else if (params instanceof UHIBELW11DelegateGenerationParameters) {
            UHIBELW11DelegateGenerationParameters parameters = (UHIBELW11DelegateGenerationParameters) params;

            UHIBELW11PublicKeyParameters pk = parameters.getPublicKey();
            UHIBELW11SecretKeyParameters sk = parameters.getSecretKey();
            Pairing pairing = PairingFactory.getPairing(pk.getParameters());

            Field zr = pairing.getZr();
            int length = sk.getLength();
            int newLength = length + 1;

            Element[] K0s = new Element[newLength];
            Element[] K1s = new Element[newLength];
            Element[] K2s = new Element[newLength];
            Element[] K3s = new Element[newLength];

            Element lambdaSum = zr.newZeroElement();
            for (int i = 0; i < length; i++) {
                Element lambda = zr.newRandomElement();
                Element y = zr.newRandomElement();
                Element r = zr.newRandomElement();

                lambdaSum.add(lambda);

                K0s[i] = sk.getK0At(i).mul(pk.getG().powZn(lambda).mul(pk.getW().powZn(y)));
                K1s[i] = sk.getK1At(i).mul(pk.getG().powZn(y));
                K2s[i] = sk.getK2At(i).mul(pk.getV().powZn(y).mul(pk.getU().powZn(sk.getIdAt(i)).mul(pk.getH()).powZn(r)));
                K3s[i] = sk.getK3At(i).mul(pk.getG().powZn(r));
            }

            Element y = zr.newRandomElement();
            Element r = zr.newRandomElement();
            K0s[length] = pk.getG().powZn(lambdaSum.negate()).mul(pk.getW().powZn(y));
            K1s[length] = pk.getG().powZn(y);
            K2s[length] = pk.getV().powZn(y).mul(pk.getU().powZn(parameters.getId()).mul(pk.getH()).powZn(r));
            K3s[length] = pk.getG().powZn(r);

            Element[] ids = new Element[sk.getIds().length + 1];
            System.arraycopy(sk.getIds(), 0, ids, 0, sk.getIds().length);
            ids[sk.getIds().length] = parameters.getId();

            return new UHIBELW11SecretKeyParameters(pk.getParameters(),
                    K0s, K1s, K2s, K3s,
                    ids);
        }

        throw new IllegalStateException("UHIBELW11SecretKeyGenerator not initialized correctly.");
    }
}
