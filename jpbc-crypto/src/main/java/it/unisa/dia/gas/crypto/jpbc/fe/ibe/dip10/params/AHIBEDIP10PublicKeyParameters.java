package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10PublicKeyParameters extends AHIBEDIP10KeyParameters {

    private Element Y1;
    private Element Y3;
    private Element Y4;
    private Element t;
    private Element[] us;
    private Element omega;


    public AHIBEDIP10PublicKeyParameters(PairingParameters curveParams,
                                         Element y1, Element y3, Element y4,
                                         Element t, Element[] us,
                                         Element omega) {
        super(false, curveParams);

        this.Y1 = y1.getImmutable();
        this.Y3 = y3.getImmutable();
        this.Y4 = y4.getImmutable();
        this.t = t.getImmutable();
        this.us = ElementUtils.cloneImmutable(us);
        this.omega = omega.getImmutable();
    }

    
    public Element getY1() {
        return Y1;
    }

    public Element getY3() {
        return Y3;
    }

    public Element getY4() {
        return Y4;
    }

    public Element getT() {
        return t;
    }

    public Element[] getUs() {
        return Arrays.copyOf(us, us.length);
    }

    public Element getOmega() {
        return omega;
    }

    public Element getUAt(int index) {
        return us[index];
    }

    public int getLength() {
        return us.length;
    }
}
