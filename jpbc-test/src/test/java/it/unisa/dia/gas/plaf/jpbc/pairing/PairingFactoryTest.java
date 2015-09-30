package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PairingFactoryTest extends TestCase {

    public void testReusing() {
        PairingFactory.getInstance().setReuseInstance(true);
        PairingFactory.getInstance().setUsePBCWhenPossible(false);

        Pairing pairing1 = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");
        Pairing pairing2 = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");

        assertEquals(true, pairing1 == pairing2);
    }

    public void testAvoidReusing() {
        PairingFactory.getInstance().setReuseInstance(false);
        PairingFactory.getInstance().setUsePBCWhenPossible(false);

        Pairing pairing1 = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");
        Pairing pairing2 = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");

        assertEquals(true, pairing1 != pairing2);
    }
}
