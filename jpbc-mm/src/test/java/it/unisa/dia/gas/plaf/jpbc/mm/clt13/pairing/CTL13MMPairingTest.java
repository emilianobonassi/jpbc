package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.AbstractCTL13MMTest;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import org.junit.Before;

import static junit.framework.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMPairingTest extends AbstractCTL13MMTest {

    protected Pairing pairing;


    public CTL13MMPairingTest(CTL13MMSystemParameters instanceParameters, int genType) {
        super(instanceParameters, genType);
    }


    @Before
    public void before() {
        super.before();

        pairing = new CTL13MMPairing(random, instance);
    }


    @org.junit.Test
    public void testMultilinearity() {
        for (int num = 2; num < instanceParameters.getKappa() + 1; num++) {

            Element[] as = new Element[num];
            Element[] gas = new Element[num];
            Element prod = pairing.getFieldAt(0).newOneElement();
            for (int i = 0; i < as.length; i++) {
                as[i] = pairing.getFieldAt(0).newRandomElement();
                prod.mul(as[i]);

                gas[i] = pairing.getFieldAt(1).newElement().powZn(as[i]);
            }

            // compute left expression
            Element left = pairing.getFieldAt(num).newElement().powZn(prod);

            // compute right expression
            Element right = gas[0];
            for (int i = 1; i < as.length; i++) {
                right = pairing.pairing(right, gas[i]);
            }

            assertEquals(true, left.isEqual(right));
        }
    }


    @org.junit.Test
    public void testIsZero() {
        Element a = pairing.getFieldAt(0).newRandomElement();

        Element b = pairing.getFieldAt(2).newElement().powZn(a);
        Element c = pairing.getFieldAt(5).newElement().powZn(a);

        assertEquals(true, b.isEqual(c));
    }


    @org.junit.Test
    public void testToBytes() {
        for (int index = 0; index < instanceParameters.getKappa() + 1; index++) {
            Field field = pairing.getFieldAt(index);

            Element a = field.newRandomElement();
            Element b = field.newElementFromBytes(a.toBytes());

            assertEquals(true, a.isEqual(b));

            b = pairing.getFieldAt(index).newElement();
            int length = b.setFromBytes(a.toBytes());

            assertEquals(true, a.isEqual(b));
            assertEquals(length, field.getLengthInBytes(b));

        }
    }
}

