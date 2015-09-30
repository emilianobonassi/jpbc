package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13RecodeParameters extends WTORGVW13KeyParameters {

    private Element rk;

    public WTORGVW13RecodeParameters(WTORGVW13Parameters parameters, Element rk) {
        super(true, parameters);
        this.rk = rk;
    }

    public Element getRk() {
        return rk;
    }
}
