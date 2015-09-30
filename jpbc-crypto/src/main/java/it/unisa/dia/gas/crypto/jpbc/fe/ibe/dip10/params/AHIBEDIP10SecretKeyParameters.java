package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10SecretKeyParameters extends AHIBEDIP10KeyParameters {
    private Element k11, k12;
    private Element k21, k22;
    private Element[] E1s, E2s;
    private Element[] ids;

    public AHIBEDIP10SecretKeyParameters(PairingParameters curveParams,
                                         Element k11, Element k12, Element[] e1s,
                                         Element k21, Element k22, Element[] e2s,
                                         Element[] ids) {
        super(true, curveParams);

        this.k11 = k11.getImmutable();
        this.k12 = k12.getImmutable();
        this.E1s = ElementUtils.cloneImmutable(e1s);

        this.k21 = k21.getImmutable();
        this.k22 = k22.getImmutable();
        this.E2s = ElementUtils.cloneImmutable(e2s);

        this.ids = ElementUtils.cloneImmutable(ids);
    }


    public Element getK11() {
        return k11;
    }

    public Element getK12() {
        return k12;
    }

    public Element getK21() {
        return k21;
    }

    public Element getK22() {
        return k22;
    }

    public Element[] getE1s() {
        return Arrays.copyOf(E1s, E1s.length);
    }

    public Element getE1At(int index) {
        return E1s[index];
    }

    public Element[] getE2s() {
        return Arrays.copyOf(E2s, E2s.length);
    }

    public Element getE2At(int index) {
        return E2s[index];
    }

    public Element[] getIds() {
        return Arrays.copyOf(ids, ids.length);
    }

    public int getE1Length() {
        return E1s.length;
    }

    public int getDepth() {
        return ids.length;
    }
}
