package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10ParametersGenerator {
    private PairingParameters parameters;
    private int n;

    private Pairing pairing;


    public IPLOSTW10ParametersGenerator init(PairingParameters parameters, int n) {
        this.parameters = parameters;
        this.n = n;
        this.pairing = PairingFactory.getPairing(this.parameters);

        return this;
    }


    public IPLOSTW10Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new IPLOSTW10Parameters(parameters, g.getImmutable(), n);
    }

}