package it.unisa.dia.gas.plaf.jpbc.mm.clt13;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.DefaultCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.MultiThreadCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMMultiThreadPublicParameterGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMPublicParameterGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractCTL13MMTest {

    static protected SecureRandom random;

    static {
        random = new SecureRandom();
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {CTL13MMSystemParameters.TOY, 0},
                {CTL13MMSystemParameters.TOY, 1}
        };

        return Arrays.asList(data);
    }


    protected CTL13MMSystemParameters instanceParameters;
    protected int type;
    protected CTL13MMInstance instance;


    public AbstractCTL13MMTest(CTL13MMSystemParameters instanceParameters, int type) {
        this.instanceParameters = instanceParameters;
        this.type = type;
    }

    @Before
    public void before() {
        switch (type) {
            case 0:
                instance = new MultiThreadCTL13MMInstance(
                        random,
                        new CTL13MMMultiThreadPublicParameterGenerator(random, instanceParameters).generate()
                );
                break;
            case 1:
                instance = new DefaultCTL13MMInstance(
                        random,
                        new CTL13MMPublicParameterGenerator(random, instanceParameters).generate()
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid instance type.");
        }
    }
}
