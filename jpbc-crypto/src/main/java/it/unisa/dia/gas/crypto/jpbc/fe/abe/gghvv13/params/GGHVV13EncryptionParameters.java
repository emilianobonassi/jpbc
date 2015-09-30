package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13EncryptionParameters extends GGHVV13KeyParameters {

    private GGHVV13PublicKeyParameters publicKey;
    private String assignment;


    public GGHVV13EncryptionParameters(GGHVV13PublicKeyParameters publicKey,
                                       String assignment) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.assignment = assignment;
    }


    public GGHVV13PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getAssignment() {
        return assignment;
    }
}
