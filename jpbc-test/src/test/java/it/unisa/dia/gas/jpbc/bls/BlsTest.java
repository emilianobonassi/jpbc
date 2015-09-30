package it.unisa.dia.gas.jpbc.bls;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BlsTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    public BlsTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testBls() {
        // Generate system parameters
        Element g = pairing.getG2().newRandomElement();

        // Generate the secret key
        Element x = pairing.getZr().newRandomElement();

        // Generate the corresponding public key
        Element pk = g.duplicate().powZn(x); // We need to duplicate g because it's a system parameter.

        // Map the hash of the message m to some element of G1

        byte[] hash = "ABCDEF".getBytes(); // Generate an hash from m (48-bit hash)
        Element h = pairing.getG1().newElementFromHash(hash, 0, hash.length);

        // Generate the signature

        Element sig = h.powZn(x); // We can discard the value h, so we don't need to duplicate it.

        // Map again the hash of the message m

        hash = "ABCDEF".getBytes(); // Generate an hash from m (48-bit hash)
        h = pairing.getG1().newElementFromHash(hash, 0, hash.length);

        // Verify the signature

        Element temp1 = pairing.pairing(sig, g);
        Element temp2 = pairing.pairing(h, pk);

        assertTrue(temp1.isEqual(temp2));
    }

}