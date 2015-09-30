package it.unisa.dia.gas.crypto.jpbc.signature.bls01.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01PublicKeyParameters extends BLS01KeyParameters {
    private Element pk;


    public BLS01PublicKeyParameters(BLS01Parameters parameters, Element pk) {
        super(false, parameters);
        this.pk = pk;
    }


    public Element getPk() {
        return pk;
    }
}
