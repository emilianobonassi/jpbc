package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCPairingType extends Memory {

    private int size = 0;


    public PBCPairingType(String buf) {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_sizeof());
        WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_init_inp_buf(this, buf, buf.length());
    }


    @Override
    protected void finalize() {
        if (size <= 0 && isValid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_clear(this);
            super.finalize();
        } else {
            // TODO: add to a thread to finalize later...
            throw new IllegalStateException("Cannot be finalized!!!");
        }
    }

    public void addElement() {
        size++;
    }

    public void removeElement() {
        size--;
    }
}