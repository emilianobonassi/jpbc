package it.unisa.dia.gas.plaf.jpbc.field.polymod;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyModElementTest extends TestCase {

    protected Pairing pairing;

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeDPairing(getParameters());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected PairingParameters getParameters() {
        return PairingFactory.getInstance().loadParameters("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties");
    }


    public void testBytesMethod() {
        Element source = pairing.getGT().newRandomElement();
        byte[] buffer = source.toBytes();
        Element target = pairing.getGT().newElement();
        int len = target.setFromBytes(buffer);

        assertEquals(true, source.isEqual(target));
        assertEquals(buffer.length, len);
    }

}
