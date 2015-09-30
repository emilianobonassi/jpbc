package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13Parameters;
import it.unisa.dia.gas.jpbc.Pairing;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13ParametersGenerator {

    private Pairing pairing;
    private int n;


    public GGHVV13ParametersGenerator init(Pairing pairing, int n) {
        this.pairing = pairing;
        this.n = n;

        return this;
    }


    public GGHVV13Parameters generateParameters() {
        return new GGHVV13Parameters(pairing, n);
    }

}