package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13MasterSecretKeyParameters extends GGHVV13KeyParameters {

    private Element alpha;


    public GGHVV13MasterSecretKeyParameters(GGHVV13Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
