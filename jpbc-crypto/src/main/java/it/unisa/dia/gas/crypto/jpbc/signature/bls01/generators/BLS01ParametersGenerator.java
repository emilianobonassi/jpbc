package it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01ParametersGenerator {
    private PairingParameters parameters;
    private Pairing pairing;


    public void init(PairingParameters parameters) {
        this.parameters = parameters;
        this.pairing = PairingFactory.getPairing(parameters);
    }

    public BLS01Parameters generateParameters() {
        Element g = pairing.getG2().newRandomElement();

        return new BLS01Parameters(parameters, g.getImmutable());
    }
}
