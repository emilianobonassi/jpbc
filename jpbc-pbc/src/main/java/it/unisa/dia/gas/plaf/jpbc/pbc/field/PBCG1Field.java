package it.unisa.dia.gas.plaf.jpbc.pbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCCurvePointElement;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCField;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingType;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCG1Field extends PBCField {


    public PBCG1Field(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        return new PBCCurvePointElement(
                new PBCElementType(PBCElementType.FieldType.G1, pairing),
                this
        );
    }

}
