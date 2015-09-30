package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12MasterSecretKeyParameters extends RLW12KeyParameters {

    private Element alpha;


    public RLW12MasterSecretKeyParameters(RLW12Parameters parameters, Element alpha) {
        super(true, parameters);

        this.alpha = alpha.getImmutable();
    }


    public Element getAlpha() {
        return alpha;
    }
}
