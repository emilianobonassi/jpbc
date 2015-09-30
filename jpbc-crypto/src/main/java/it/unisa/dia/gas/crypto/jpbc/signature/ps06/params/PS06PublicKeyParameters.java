package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06PublicKeyParameters extends PS06KeyParameters {
    private Element g1, g2;
    private Element uPrime, mPrime;
    private Element[] Us, Ms;


    public PS06PublicKeyParameters(PS06Parameters parameters,
                                   Element g1, Element g2,
                                   Element uPrime, Element mPrime,
                                   Element[] Us, Element[] Ms) {
        super(false, parameters);

        this.g1 = g1.getImmutable();
        this.g2 = g2.getImmutable();
        this.uPrime = uPrime.getImmutable();
        this.mPrime = mPrime.getImmutable();
        this.Us = ElementUtils.cloneImmutable(Us);
        this.Ms = ElementUtils.cloneImmutable(Ms);
    }

    public Element getG1() {
        return g1;
    }

    public Element getG2() {
        return g2;
    }

    public Element getuPrime() {
        return uPrime;
    }

    public Element getmPrime() {
        return mPrime;
    }

    public Element getMAt(int index) {
        return Ms[index];
    }

    public Element getUAt(int index) {
        return Us[index];
    }

}
