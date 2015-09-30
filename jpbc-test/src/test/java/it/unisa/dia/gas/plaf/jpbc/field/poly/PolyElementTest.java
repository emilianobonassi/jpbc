package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyElementTest extends TestCase {

/*
    public void testGDC() {
        PolyField field = new PolyField(new ZrField(BigInteger.valueOf(17)));

        PolyElement r = field.newElement();
        r.setFromCoefficientMonic(new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(1)});

        PolyElement g = field.newElement();
        g.setFromCoefficientMonic(new BigInteger[]{BigInteger.valueOf(14), BigInteger.valueOf(8),BigInteger.valueOf(1)});

        Element fac = field.newElement().set(r).gcd(g);
        System.out.println("fac = " + fac);
    }
*/
    public void testBytes(){
        PolyField field = new PolyField(new ZrField(BigInteger.valueOf(17)));

        PolyElement source = field.newElement();
        source.setToRandomMonic(5);

        byte buffer[] = source.toBytes();
        PolyElement target = field.newElement();
        int len = target.setFromBytes(buffer);

        assertEquals(buffer.length, len);
        assertEquals(true, source.isEqual(target));
    }

    public void testFindRoot() {
        PolyField field = new PolyField(new ZrField(BigInteger.valueOf(17)));
        PolyElement element = field.newElement();

        BigInteger[] coeff = new BigInteger[]{
                BigInteger.valueOf(5),
                BigInteger.valueOf(12),
                BigInteger.valueOf(12),
                BigInteger.valueOf(1),
        };
        element.setFromCoefficientMonic(coeff);

        Element root = element.findRoot();

        BigInteger rootInt = root.toBigInteger();

        if (!BigInteger.valueOf(2).equals(rootInt) &&
            !BigInteger.valueOf(7).equals(rootInt) &&
            !BigInteger.valueOf(13).equals(rootInt))
            fail("Invalid root!");
    }

}
