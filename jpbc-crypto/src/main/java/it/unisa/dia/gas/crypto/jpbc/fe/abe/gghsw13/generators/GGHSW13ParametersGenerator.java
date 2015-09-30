package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.GGHSW13Parameters;
import it.unisa.dia.gas.jpbc.Pairing;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13ParametersGenerator {

    private Pairing pairing;
    private int n;


    public GGHSW13ParametersGenerator init(Pairing pairing, int n) {
        this.pairing = pairing;
        this.n = n;

        return this;
    }


    public GGHSW13Parameters generateParameters() {
        return new GGHSW13Parameters(pairing, n);
    }

}