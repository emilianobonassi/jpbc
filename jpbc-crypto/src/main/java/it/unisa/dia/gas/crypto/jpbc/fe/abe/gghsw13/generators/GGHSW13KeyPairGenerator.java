package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private GGHSW13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (GGHSW13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        GGHSW13Parameters parameters = params.getParameters();

        Pairing pairing = parameters.getPairing();

        // Sample secret key
        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        // Sample public key
        int n = parameters.getN();
        Element[] hs = new Element[n];
        for (int i = 0; i < hs.length; i++)
            hs[i] = pairing.getFieldAt(1).newRandomElement().getImmutable();

        Element H = pairing.getFieldAt(pairing.getDegree()).newElement().powZn(alpha).getImmutable();

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new GGHSW13PublicKeyParameters(parameters, H, hs),
                new GGHSW13MasterSecretKeyParameters(parameters, alpha)
        );
    }


}
