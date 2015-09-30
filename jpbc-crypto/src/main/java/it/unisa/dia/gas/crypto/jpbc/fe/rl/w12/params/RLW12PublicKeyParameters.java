package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12PublicKeyParameters extends RLW12KeyParameters {

    private Element z;
    private Element hStart;
    private Element hEnd;
    private Element[] hs;
    private Element omega;


    public RLW12PublicKeyParameters(RLW12Parameters parameters,
                                    Element z,
                                    Element hStart, Element hEnd,
                                    Element[] hs,
                                    Element omega) {
        super(false, parameters);

        this.z = z.getImmutable();
        this.hStart = hStart.getImmutable();
        this.hEnd = hEnd.getImmutable();
        this.hs = ElementUtils.cloneImmutable(hs);
        this.omega = omega.getImmutable();
    }


    public Element getZ() {
        return z;
    }

    public Element gethStart() {
        return hStart;
    }

    public Element gethEnd() {
        return hEnd;
    }

    public Element getHAt(Character character) {
        return hs[getParameters().getCharacterIndex(character)];
    }

    public Element getOmega() {
        return omega;
    }

}
