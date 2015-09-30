package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private RLW12KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (RLW12KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        RLW12Parameters parameters = params.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());
        Element g = parameters.getG();

        // Generate required elements
        Element z = ElementUtils.randomIn(pairing, g).getImmutable();
        Element hStart = ElementUtils.randomIn(pairing, g).getImmutable();
        Element hEnd = ElementUtils.randomIn(pairing, g).getImmutable();

        Element[] hs = new Element[parameters.getAlphabetSize()];
        for (int i = 0; i < hs.length; i++) {
            hs[i] = ElementUtils.randomIn(pairing, g).getImmutable();
        }

        Element alpha = pairing.getZr().newRandomElement().getImmutable();
        Element omega = pairing.pairing(g, g).powZn(alpha).getImmutable();

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new RLW12PublicKeyParameters(parameters, z, hStart, hEnd, hs, omega),
                new RLW12MasterSecretKeyParameters(parameters, alpha)
        );
    }


}
