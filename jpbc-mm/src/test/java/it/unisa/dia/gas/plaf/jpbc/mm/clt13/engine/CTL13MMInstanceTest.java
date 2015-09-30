package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.AbstractCTL13MMTest;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import junit.framework.Assert;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMInstanceTest extends AbstractCTL13MMTest {

    public CTL13MMInstanceTest(CTL13MMSystemParameters instanceParameters, int genType) {
        super(instanceParameters, genType);
    }

    @org.junit.Test
    public void test() {
        for (int i = 0; i < instance.getSystemParameters().getKappa() + 1; i++) {
            Assert.assertTrue(instance.isZero(instance.encodeZeroAt(i), i));
            Assert.assertFalse(instance.isZero(instance.encodeOneAt(i), i));
        }
    }

    @org.junit.Test
    public void testIsZero() {
        BigInteger a = instance.encodeAt(0);

        BigInteger b = instance.encodeAt(instance.encodeAt(a, 0, 2), 2, 5);
        BigInteger c = instance.encodeAt(a, 0, 5);

        BigInteger diff = instance.reduce(b.subtract(c));

        Assert.assertEquals(true, instance.isZero(diff, 5));
    }


}
