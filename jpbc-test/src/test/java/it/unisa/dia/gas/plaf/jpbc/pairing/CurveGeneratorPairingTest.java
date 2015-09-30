package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeECurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;
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
public class CurveGeneratorPairingTest extends PairingTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, new TypeACurveGenerator(181, 603)},
                {false, new TypeA1CurveGenerator(2, 512)},
                {false, new TypeECurveGenerator(160, 1024)},
                {false, new TypeFCurveGenerator(160)},
                {true, new TypeACurveGenerator(181, 603)},
                {true, new TypeA1CurveGenerator(2, 512)},
                {true, new TypeECurveGenerator(160, 1024)},
                {true, new TypeFCurveGenerator(160)}
        };

        return Arrays.asList(data);
    }


    protected PairingParametersGenerator parametersGenerator;


    public CurveGeneratorPairingTest(boolean usePBC, PairingParametersGenerator parametersGenerator) {
        super(usePBC, null);

        this.parametersGenerator = parametersGenerator;
    }

    @Before
    public void before() throws Exception {
        assumeTrue(!usePBC || PairingFactory.getInstance().isPBCAvailable());

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        pairing = PairingFactory.getPairing(parametersGenerator.generate());

        assumeTrue(pairing != null);
    }

}
