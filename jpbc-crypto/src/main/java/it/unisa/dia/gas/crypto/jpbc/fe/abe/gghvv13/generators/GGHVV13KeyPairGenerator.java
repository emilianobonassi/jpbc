package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private GGHVV13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (GGHVV13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        GGHVV13Parameters parameters = params.getParameters();

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
                new GGHVV13PublicKeyParameters(parameters, H, hs),
                new GGHVV13MasterSecretKeyParameters(parameters, alpha)
        );
    }


}
