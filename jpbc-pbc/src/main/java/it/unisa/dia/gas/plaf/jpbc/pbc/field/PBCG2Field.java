package it.unisa.dia.gas.plaf.jpbc.pbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCCurvePointElement;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCField;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingType;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCG2Field extends PBCField {


    public PBCG2Field(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        return new PBCCurvePointElement(
                new PBCElementType(PBCElementType.FieldType.G2, pairing),
                this
        );
    }

}