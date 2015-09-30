package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10SecretKeyGenerator {
    private IPLOSTW10SecretKeyGenerationParameters param;
    private Pairing pairing;
    private int n;

    public void init(KeyGenerationParameters param) {
        this.param = (IPLOSTW10SecretKeyGenerationParameters) param;
        this.n = this.param.getParameters().getParameters().getN();
        this.pairing = PairingFactory.getPairing(this.param.getParameters().getParameters().getParameters());
    }

    public CipherParameters generateKey() {
        IPLOSTW10MasterSecretKeyParameters secretKey = param.getParameters();

        Element sigma = pairing.getZr().newRandomElement();
        Element eta = pairing.getZr().newRandomElement();

        Element k = secretKey.getBStarAt(0).duplicate().powZn(param.getYAt(0));
        for (int i = 1; i < n; i++) {
            k.add(secretKey.getBStarAt(i).powZn(param.getYAt(i)));
        }
        k.mulZn(sigma)
                .add(secretKey.getBStarAt(n))
                .add(secretKey.getBStarAt(n + 1).powZn(eta));

        return new IPLOSTW10SecretKeyParameters(param.getParameters().getParameters(), k);
    }

}