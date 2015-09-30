package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCPairingFactory {

    public static boolean isPBCAvailable() {
        return WrapperLibraryProvider.isAvailable();
    }

    public static Pairing getPairing(PairingParameters parameters) {
        if (WrapperLibraryProvider.isAvailable())
            return new PBCPairing(parameters);
        else
            return null;
    }

}
