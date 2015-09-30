package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10SecretKeyParameters extends IPLOSTW10KeyParameters {
    private Element K;

    public IPLOSTW10SecretKeyParameters(IPLOSTW10Parameters parameters, Element k) {
        super(true, parameters);
        K = k;
    }

    public Element getK() {
        return K;
    }
}