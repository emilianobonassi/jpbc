package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06MasterSecretKeyParameters extends PS06KeyParameters {
    private Element msk;


    public PS06MasterSecretKeyParameters(PS06Parameters parameters, Element msk) {
        super(true, parameters);

        this.msk = msk.getImmutable();
    }


    public Element getMsk() {
        return msk;
    }
}