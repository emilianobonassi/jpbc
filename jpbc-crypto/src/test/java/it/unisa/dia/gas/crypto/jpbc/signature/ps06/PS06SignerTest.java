package it.unisa.dia.gas.crypto.jpbc.signature.ps06;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.engines.PS06Signer;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06SetupGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.params.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06SignerTest extends AbstractJPBCCryptoTest {


    public PS06SignerTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testSignerEngine() {
        // Setup -> (Public Key, Master Secret Key)
        AsymmetricCipherKeyPair keyPair = setup(createParameters(256, 256));

        // Extract -> Secret Key for Identity "01001101"
        CipherParameters secretKey = extract(keyPair, "01001101");

        // Sign
        String message = "Hello World!!!";
        byte[] signature = sign(message, secretKey);

        // verify with the same identity
        assertTrue(verify(keyPair.getPublic(), message, "01001101", signature));

        // verify with another identity
        assertFalse(verify(keyPair.getPublic(), message, "01001100", signature));
    }

    protected PS06Parameters createParameters(int nU, int nM) {
        // Generate Public PairingParameters
        return new PS06ParametersGenerator().init(parameters, nU, nM).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(PS06Parameters parameters) {
        PS06SetupGenerator setup = new PS06SetupGenerator();
        setup.init(new PS06SetupGenerationParameters(null, parameters));

        return setup.generateKeyPair();
    }


    protected CipherParameters extract(AsymmetricCipherKeyPair keyPair, String identity) {
        PS06SecretKeyGenerator extract = new PS06SecretKeyGenerator();
        extract.init(new PS06SecretKeyGenerationParameters(keyPair, identity));

        return extract.generateKey();
    }

    protected byte[] sign(String message, CipherParameters secretKey) {
        byte[] bytes = message.getBytes();

        PS06Signer signer = new PS06Signer(new SHA256Digest());
        signer.init(true, new PS06SignParameters((PS06SecretKeyParameters) secretKey));
        signer.update(bytes, 0, bytes.length);

        byte[] signature = null;
        try {
            signature = signer.generateSignature();
        } catch (CryptoException e) {
            fail(e.getMessage());
        }

        return signature;
    }

    protected boolean verify(CipherParameters publicKey, String message, String identity, byte[] signature) {
        byte[] bytes = message.getBytes();

        PS06Signer signer = new PS06Signer(new SHA256Digest());
        signer.init(false, new PS06VerifyParameters((PS06PublicKeyParameters) publicKey, identity));
        signer.update(bytes, 0, bytes.length);

        return signer.verifySignature(signature);
    }
}
