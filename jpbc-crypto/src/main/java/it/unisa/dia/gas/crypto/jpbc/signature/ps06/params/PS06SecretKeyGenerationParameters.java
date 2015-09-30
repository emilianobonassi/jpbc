package it.unisa.dia.gas.crypto.jpbc.signature.ps06.params;


import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06SecretKeyGenerationParameters extends KeyGenerationParameters {

    private PS06PublicKeyParameters publicKey;
    private PS06MasterSecretKeyParameters masterSecretKey;
    private String identity;


    public PS06SecretKeyGenerationParameters(AsymmetricCipherKeyPair keyPair, String identity) {
        super(null, ((PS06PublicKeyParameters) keyPair.getPublic()).getParameters().getG().getField().getLengthInBytes());

        this.publicKey = (PS06PublicKeyParameters) keyPair.getPublic();
        this.masterSecretKey = (PS06MasterSecretKeyParameters) keyPair.getPrivate();
        this.identity = identity;
    }


    public PS06PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public PS06MasterSecretKeyParameters getMasterSecretKey() {
        return masterSecretKey;
    }

    public String getIdentity() {
        return identity;
    }
}