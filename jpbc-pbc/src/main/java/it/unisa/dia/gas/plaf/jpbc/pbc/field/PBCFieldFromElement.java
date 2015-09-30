package it.unisa.dia.gas.plaf.jpbc.pbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCElement;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCField;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCFieldFromElement extends PBCField {

    public PBCFieldFromElement(PBCElementType baseElement) {
        super(baseElement);
    }

    public Element newElement() {
        PBCElementType element = new PBCElementType();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_init_same_as(element, baseElement);

        return new PBCElement(element, this);
    }
}
