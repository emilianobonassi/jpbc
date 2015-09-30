package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12EncryptionParameters extends RLW12KeyParameters {

    private RLW12PublicKeyParameters publicKey;
    private String w;


    public RLW12EncryptionParameters(RLW12PublicKeyParameters publicKey,
                                     String w) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.w = w;
    }


    public RLW12PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getW() {
        return w;
    }
}
