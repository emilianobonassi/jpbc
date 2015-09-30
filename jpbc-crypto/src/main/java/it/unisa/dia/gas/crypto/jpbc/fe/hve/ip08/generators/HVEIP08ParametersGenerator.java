package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08ParametersGenerator {
    private PairingParameters curveParams;
    private int[] attributeLengths;

    private Pairing pairing;


    public void init(PairingParameters curveParams, int... attributeLengths) {
        this.curveParams = curveParams;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);

        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public void init(int n, PairingParameters curveParams) {
        this.init(n, 1, curveParams);
    }

    public void init(int n, int numBitsPerAttribute, PairingParameters curveParams) {
        this.curveParams = curveParams;
        this.attributeLengths = new int[n];
        for (int i = 0; i < attributeLengths.length; i++) {
            attributeLengths[i] = numBitsPerAttribute;
        }

        this.pairing = PairingFactory.getPairing(curveParams);
    }


    public HVEIP08Parameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();

        return new HVEIP08Parameters(curveParams, g.getImmutable(), attributeLengths);
    }

}