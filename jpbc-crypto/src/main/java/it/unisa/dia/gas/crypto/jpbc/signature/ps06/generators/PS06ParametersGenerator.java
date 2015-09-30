package it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators;

import it.unisa.dia.gas.crypto.jpbc.signature.ps06.params.PS06Parameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06ParametersGenerator {
    private PairingParameters curveParams;
    private Pairing pairing;
    private int nU, nM;


    public PS06ParametersGenerator init(PairingParameters curveParams, int nU, int nM) {
        this.curveParams = curveParams;
        this.nU = nU;
        this.nM = nM;

        this.pairing = PairingFactory.getPairing(curveParams);

        return this;
    }

    public PS06Parameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();

        return new PS06Parameters(curveParams, g.getImmutable(), nU, nM);
    }
}
