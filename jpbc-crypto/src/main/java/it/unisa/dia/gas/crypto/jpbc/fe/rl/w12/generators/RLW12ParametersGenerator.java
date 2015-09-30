package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators;

import it.unisa.dia.gas.crypto.dfa.DFA;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.RLW12Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12ParametersGenerator {
    private PairingParameters parameters;
    private DFA.Alphabet alphabet;

    private Pairing pairing;


    public RLW12ParametersGenerator init(PairingParameters parameters, DFA.Alphabet alphabet) {
        this.parameters = parameters;
        this.alphabet = alphabet;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public RLW12Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new RLW12Parameters(parameters, g.getImmutable(), alphabet);
    }

}