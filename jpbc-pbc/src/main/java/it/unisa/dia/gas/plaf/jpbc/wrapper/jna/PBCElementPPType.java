package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCElementPPType extends Memory {

    public PBCElementPPType() {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_sizeof());
    }

    public PBCElementPPType(Pointer element) {
        this();
        
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_init(this, element);
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_clear(this);
            super.finalize();
        }
    }
}