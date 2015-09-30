package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private GGHVV13PublicKeyParameters publicKeyParameters;
    private GGHVV13MasterSecretKeyParameters masterSecretKeyParameters;
    private Circuit circuit;


    public GGHVV13SecretKeyGenerationParameters(
            GGHVV13PublicKeyParameters publicKeyParameters,
            GGHVV13MasterSecretKeyParameters masterSecretKeyParameters,
            Circuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GGHVV13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GGHVV13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public Circuit getCircuit() {
        return circuit;
    }
}