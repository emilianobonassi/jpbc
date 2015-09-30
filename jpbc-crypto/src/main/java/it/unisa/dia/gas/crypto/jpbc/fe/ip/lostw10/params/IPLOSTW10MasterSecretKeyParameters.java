package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10MasterSecretKeyParameters extends IPLOSTW10KeyParameters {
    private Element[] Bstar;


    public IPLOSTW10MasterSecretKeyParameters(IPLOSTW10Parameters parameters, Element[] Bstar) {
        super(true, parameters);

        this.Bstar = ElementUtils.cloneImmutable(Bstar);
    }

    public Element getBStarAt(int index) {
        return Bstar[index];
    }

}