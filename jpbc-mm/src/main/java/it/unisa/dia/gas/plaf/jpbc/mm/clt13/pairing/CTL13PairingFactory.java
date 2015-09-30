package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class CTL13PairingFactory {

    public static Pairing getPairing(SecureRandom random, PairingParameters parameters) {
        return new CTL13MMPairing(random, parameters);
    }

}
