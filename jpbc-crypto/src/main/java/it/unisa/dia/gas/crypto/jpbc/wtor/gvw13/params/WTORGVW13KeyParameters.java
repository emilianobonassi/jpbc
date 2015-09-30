package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13KeyParameters extends AsymmetricKeyParameter {

    private WTORGVW13Parameters parameters;


    public WTORGVW13KeyParameters(boolean isPrivate, WTORGVW13Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public WTORGVW13Parameters getParameters() {
        return parameters;
    }
}


