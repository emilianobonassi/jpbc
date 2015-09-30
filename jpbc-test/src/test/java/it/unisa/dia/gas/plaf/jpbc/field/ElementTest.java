package it.unisa.dia.gas.plaf.jpbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
@RunWith(value = Parameterized.class)
public class ElementTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        SecureRandom secureRandom = new SecureRandom();

        Object[][] data = {
                {new ZrField(new BigInteger(1024, 12, secureRandom))}
        };

        return Arrays.asList(data);
    }


    protected Field field;


    public ElementTest(Field field) {
        this.field = field;
    }


    @Test
    public void testAddSub() {
        Element a = field.newRandomElement().getImmutable();
        Element b = field.newRandomElement();

        Element c = a.add(b).sub(b);

        assertTrue(c.isEqual(a));
    }

    @Test
    public void testMulDiv() {
        Element a = field.newRandomElement().getImmutable();
        Element b = field.newRandomElement();

        Element c = a.mul(b).div(b);

        assertTrue(c.isEqual(a));
    }

    @Test
    public void testToFromBytes() {
        Element a = field.newRandomElement().getImmutable();
        Element c = field.newElement();
        int length = c.setFromBytes(a.toBytes());

        assertTrue(c.isEqual(a));
        assertEquals(length, field.getLengthInBytes(c));
    }

    @Test
    public void testToFromBytes2() {
        Element a = field.newRandomElement();
        Element c = field.newElementFromBytes(a.toBytes());

        assertTrue(c.isEqual(a));
    }

}
