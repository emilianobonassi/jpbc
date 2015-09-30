package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10SecretKeyGenerationParameters extends KeyGenerationParameters {

    private IPLOSTW10MasterSecretKeyParameters params;
    private Element[] y;


    public IPLOSTW10SecretKeyGenerationParameters(IPLOSTW10MasterSecretKeyParameters params, Element[] y) {
        super(null, params.getParameters().getG().getField().getLengthInBytes());

        this.params = params;
        this.y = Arrays.copyOf(y, y.length);
    }

    public IPLOSTW10MasterSecretKeyParameters getParameters() {
        return params;
    }

    public Element getYAt(int index) {
        return y[index];
    }

}