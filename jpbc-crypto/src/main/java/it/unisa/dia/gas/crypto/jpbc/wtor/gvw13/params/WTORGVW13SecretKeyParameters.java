package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13SecretKeyParameters extends WTORGVW13KeyParameters {

    private Element t;


    public WTORGVW13SecretKeyParameters(WTORGVW13Parameters parameters, Element t) {
        super(true, parameters);

        this.t = t.getImmutable();
    }


    public Element getT() {
        return t;
    }
}
