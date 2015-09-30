package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10Parameters implements CipherParameters {
    private PairingParameters parameters;
    private Element g;
    private int n;


    public IPLOSTW10Parameters(PairingParameters parameters, Element g, int n) {
        this.parameters = parameters;
        this.g = g.getImmutable();
        this.n = n;
    }


    public PairingParameters getParameters() {
        return parameters;
    }

    public Element getG() {
        return g;
    }

    public int getN() {
        return n;
    }

}