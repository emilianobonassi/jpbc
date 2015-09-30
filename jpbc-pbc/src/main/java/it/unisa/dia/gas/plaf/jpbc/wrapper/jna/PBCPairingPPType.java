package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCPairingPPType extends Memory {

    public PBCPairingPPType() {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_sizeof());
    }


    public PBCPairingPPType(Pointer element, Pointer pairing) {
        this();
        WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_init(this, element, pairing);
    }

    public PBCPairingPPType(Pointer pairing, byte[] source, int offset) {
        this();

        if (WrapperLibraryProvider.getWrapperLibrary().pbc_is_pairing_pp_io_available(pairing))
            WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_init_from_bytes(this, pairing, source, offset);
        else
            throw new IllegalStateException("Initialization not supported.");
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_clear(this);
            super.finalize();
        }
    }
}