package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params;

import it.unisa.dia.gas.jpbc.PairingParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UHIBELW11KeyParameters extends AsymmetricKeyParameter {

    private PairingParameters parameters;


    public UHIBELW11KeyParameters(boolean isPrivate, PairingParameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public PairingParameters getParameters() {
        return parameters;
    }

}


