package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.PairingParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10KeyParameters extends AsymmetricKeyParameter {

    private PairingParameters parameters;


    public AHIBEDIP10KeyParameters(boolean isPrivate, PairingParameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public PairingParameters getParameters() {
        return parameters;
    }

}

