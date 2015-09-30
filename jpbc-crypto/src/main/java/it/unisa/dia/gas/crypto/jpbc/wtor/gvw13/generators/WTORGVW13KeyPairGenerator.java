package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.generators;

import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13Parameters;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private WTORGVW13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (WTORGVW13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        WTORGVW13Parameters parameters = params.getParameters();
        int level = params.getLevel();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());

        if (level == 1) {
            Element t = pairing.getZr().newRandomElement().getImmutable();
            Element tInv = t.invert();

            Element left = parameters.getG1a().powZn(tInv);
            Element right = parameters.getG2a().powZn(tInv);

            return new AsymmetricCipherKeyPair(
                    new WTORGVW13PublicKeyParameters(parameters, left, right, level),
                    new WTORGVW13SecretKeyParameters(parameters, t)
            );
        } else {
            Element right = pairing.getG2().newRandomElement();

            return new AsymmetricCipherKeyPair(
                    new WTORGVW13PublicKeyParameters(parameters, null, right, level),
                    null
            );
        }
    }


}
