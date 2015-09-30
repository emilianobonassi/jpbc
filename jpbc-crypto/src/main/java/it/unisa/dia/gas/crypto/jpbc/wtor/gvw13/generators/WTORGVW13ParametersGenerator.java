package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.generators;

import it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params.WTORGVW13Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13ParametersGenerator {
    private PairingParameters parameters;

    private Pairing pairing;


    public WTORGVW13ParametersGenerator init(PairingParameters parameters) {
        this.parameters = parameters;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public WTORGVW13Parameters generateParameters() {
        Element a = pairing.getZr().newElement().setToRandom();

        Element g1 = pairing.getG1().newElement().setToRandom().getImmutable();
        Element g2 = pairing.getG2().newElement().setToRandom().getImmutable();

        Element g1a = g1.powZn(a).getImmutable();
        Element g2a = g2.powZn(a).getImmutable();

        return new WTORGVW13Parameters(parameters, g1, g2, g1a, g2a);
    }

}