package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06Parameters implements CipherParameters {
    private PairingParameters curveParams;
    private Element g;
    private int nU, nM;


    public PS06Parameters(PairingParameters curveParams, Element g, int nU, int nM) {
        this.curveParams = curveParams;
        this.g = g;
        this.nU = nU;
        this.nM = nM;
    }


    public PairingParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public int getnU() {
        return nU;
    }

    public int getnM() {
        return nM;
    }

}