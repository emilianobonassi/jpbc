package it.unisa.dia.gas.plaf.jpbc.pbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCElement;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCField;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingType;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCZrField extends PBCField {


    public PBCZrField(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        return new PBCElement(
                new PBCElementType(PBCElementType.FieldType.Zr, pairing),
                this
        );
    }

}