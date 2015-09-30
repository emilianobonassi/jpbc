package it.unisa.dia.gas.crypto.jpbc.signature.bls01.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01KeyParameters extends AsymmetricKeyParameter {
    private BLS01Parameters parameters;


    public BLS01KeyParameters(boolean isPrivate, BLS01Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public BLS01Parameters getParameters() {
        return parameters;
    }

}
