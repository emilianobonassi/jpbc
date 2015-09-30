package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10EncryptionParameters extends IPLOSTW10KeyParameters {

    private IPLOSTW10PublicKeyParameters publicKey;
    private Element[] x;


    public IPLOSTW10EncryptionParameters(IPLOSTW10PublicKeyParameters publicKey,
                                         Element[] x) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.x = ElementUtils.cloneImmutable(x);
    }


    public IPLOSTW10PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element[] getX() {
        return Arrays.copyOf(x, x.length);
    }

    public Element getXAt(int index) {
        return x[index];
    }

    public int getLength() {
        return x.length;
    }
}
