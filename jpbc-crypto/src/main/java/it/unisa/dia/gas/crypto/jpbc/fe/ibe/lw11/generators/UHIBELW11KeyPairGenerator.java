package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private UHIBELW11KeyPairGenerationParameters parameters;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.parameters = (UHIBELW11KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        PropertiesParameters parameters;
        Pairing pairing;
        Element g;

        // Generate curve parameters
        while (true) {
            parameters = generateCurveParams();
            pairing = PairingFactory.getPairing(parameters);

            Element generator = pairing.getG1().newRandomElement();
            g = ElementUtils.getGenerator(pairing, generator, parameters, 0, 3).getImmutable();
            if (!pairing.pairing(g, g).isOne())
                break;
        }

        // Generate required elements
        Element u = ElementUtils.randomIn(pairing, g).getImmutable();
        Element h = ElementUtils.randomIn(pairing, g).getImmutable();
        Element v = ElementUtils.randomIn(pairing, g).getImmutable();
        Element w = ElementUtils.randomIn(pairing, g).getImmutable();

        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        Element omega = pairing.pairing(g, g).powZn(alpha).getImmutable();

        // Remove factorization from curveParams
        parameters.remove("n0");
        parameters.remove("n1");
        parameters.remove("n2");

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new UHIBELW11PublicKeyParameters(parameters, g, u, h, v, w, omega),
                new UHIBELW11MasterSecretKeyParameters(parameters, alpha)
        );
    }


    private PropertiesParameters generateCurveParams() {
        PairingParametersGenerator parametersGenerator = new TypeA1CurveGenerator(3, parameters.getBitLength());
        return (PropertiesParameters) parametersGenerator.generate();
    }
}
