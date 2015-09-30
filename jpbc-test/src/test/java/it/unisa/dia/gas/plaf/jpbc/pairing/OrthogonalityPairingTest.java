package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
@RunWith(value = Parameterized.class)
public class OrthogonalityPairingTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"},
        };

        return Arrays.asList(data);

    }


    protected PairingParameters parameters;


    public OrthogonalityPairingTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Before
    public void before() throws Exception {
        assumeTrue(!usePBC || PairingFactory.getInstance().isPBCAvailable());

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        parameters = PairingFactory.getInstance().loadParameters(curvePath);
        pairing = PairingFactory.getPairing(parameters);

        assumeTrue(parameters != null);
        assumeTrue(pairing != null);
    }

    @Test
    public void testOrthogonality() {
        BigInteger p0 = parameters.getBigInteger("n0");
        BigInteger p1 = parameters.getBigInteger("n1");
        BigInteger p2 = parameters.getBigInteger("n2");

        Element gen = pairing.getG1().newRandomElement().getImmutable();
        Element gen0 = gen.pow(p1.multiply(p2)).getImmutable();
        Element gen1 = gen.pow(p0.multiply(p2)).getImmutable();
        Element gen2 = gen.pow(p0.multiply(p1)).getImmutable();
        Element gen01 = gen.pow(p2).getImmutable();
        Element gen02 = gen.pow(p1).getImmutable();
        Element gen12 = gen.pow(p0).getImmutable();

        Element e0 = gen0.powZn(pairing.getZr().newRandomElement());
        Element e1 = gen1.powZn(pairing.getZr().newRandomElement());
        Element e2 = gen2.powZn(pairing.getZr().newRandomElement());

        Element e01 = gen01.powZn(pairing.getZr().newRandomElement());
        Element e02 = gen02.powZn(pairing.getZr().newRandomElement());
        Element e12 = gen12.powZn(pairing.getZr().newRandomElement());

        assertEquals(true, pairing.pairing(e0, e1).isOne());
        assertEquals(true, pairing.pairing(e0, e2).isOne());
        assertEquals(true, pairing.pairing(e1, e2).isOne());
        assertEquals(true, pairing.pairing(e1, e0).isOne());
        assertEquals(true, pairing.pairing(e2, e0).isOne());
        assertEquals(true, pairing.pairing(e2, e1).isOne());

        assertEquals(false, pairing.pairing(e01, e02).isOne());
        assertEquals(false, pairing.pairing(e01, e12).isOne());
        assertEquals(false, pairing.pairing(e12, e02).isOne());
        assertEquals(false, pairing.pairing(e02, e01).isOne());
        assertEquals(false, pairing.pairing(e12, e01).isOne());
        assertEquals(false, pairing.pairing(e02, e12).isOne());

        assertEquals(true, pairing.pairing(e01, e2).isOne());
        assertEquals(true, pairing.pairing(e02, e1).isOne());
        assertEquals(true, pairing.pairing(e12, e0).isOne());
        assertEquals(true, pairing.pairing(e2, e01).isOne());
        assertEquals(true, pairing.pairing(e1, e02).isOne());
        assertEquals(true, pairing.pairing(e0, e12).isOne());
    }

}
