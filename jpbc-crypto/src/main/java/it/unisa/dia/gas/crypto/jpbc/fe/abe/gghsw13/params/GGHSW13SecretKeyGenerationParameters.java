package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private GGHSW13PublicKeyParameters publicKeyParameters;
    private GGHSW13MasterSecretKeyParameters masterSecretKeyParameters;
    private Circuit circuit;


    public GGHSW13SecretKeyGenerationParameters(
            GGHSW13PublicKeyParameters publicKeyParameters,
            GGHSW13MasterSecretKeyParameters masterSecretKeyParameters,
            Circuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GGHSW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GGHSW13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public Circuit getCircuit() {
        return circuit;
    }
}