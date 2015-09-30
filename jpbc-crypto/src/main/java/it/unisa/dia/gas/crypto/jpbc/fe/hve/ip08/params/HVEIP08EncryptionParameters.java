package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08EncryptionParameters extends HVEIP08KeyParameters {

    private HVEIP08PublicKeyParameters publicKey;
    private int[] attributes;


    public HVEIP08EncryptionParameters(HVEIP08PublicKeyParameters publicKey,
                                       int[] attributes) {
        super(false, publicKey.getParameters());
        this.publicKey = publicKey;
        this.attributes = Arrays.copyOf(attributes, attributes.length);
    }


    public HVEIP08PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public int[] getAttributes() {
        return Arrays.copyOf(attributes, attributes.length);
    }

    public int getAttributeAt(int index) {
        return attributes[index];
    }

    public int getLength() {
        return attributes.length;
    }
}
