package it.unisa.dia.gas.plaf.jpbc;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractJPBCTest {

    static {
        PairingFactory.getInstance().setReuseInstance(false);
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties"}
        };

        return Arrays.asList(data);
    }


    protected String curvePath;
    protected boolean usePBC;
    protected Pairing pairing;


    public AbstractJPBCTest(boolean usePBC, String curvePath) {
        this.usePBC = usePBC;
        this.curvePath = curvePath;
    }

    @Before
    public void before() throws Exception {
        assumeTrue(!usePBC || PairingFactory.getInstance().isPBCAvailable());

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        pairing = PairingFactory.getPairing(curvePath);

        assumeTrue(pairing != null);
    }

}
