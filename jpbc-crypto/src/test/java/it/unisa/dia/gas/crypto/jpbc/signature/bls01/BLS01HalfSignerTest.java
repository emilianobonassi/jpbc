package it.unisa.dia.gas.crypto.jpbc.signature.bls01;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01HalfSigner;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01HalfSignerTest extends AbstractJPBCCryptoTest {


    public BLS01HalfSignerTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testSignerEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = keyGen(setup());

        // Test
        String message = "Hello World!";
        assertTrue(verify(sign(message, keyPair.getPrivate()), message, keyPair.getPublic()));
    }


    protected BLS01Parameters setup() {
        BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
        setup.init(parameters);

        return setup.generateParameters();
    }

    protected AsymmetricCipherKeyPair keyGen(BLS01Parameters parameters) {
        BLS01KeyPairGenerator keyGen = new BLS01KeyPairGenerator();
        keyGen.init(new BLS01KeyGenerationParameters(null, parameters));

        return keyGen.generateKeyPair();
    }

    protected byte[] sign(String message, CipherParameters privateKey) {
        byte[] bytes = message.getBytes();

        BLS01HalfSigner signer = new BLS01HalfSigner(new SHA256Digest());
        signer.init(true, privateKey);
        signer.update(bytes, 0, bytes.length);

        byte[] signature = null;
        try {
            signature = signer.generateSignature();
        } catch (CryptoException e) {
            fail(e.getMessage());
        }
        return signature;
    }

    protected boolean verify(byte[] signature, String message, CipherParameters publicKey) {
        byte[] bytes = message.getBytes();

        BLS01HalfSigner signer = new BLS01HalfSigner(new SHA256Digest());
        signer.init(false, publicKey);
        signer.update(bytes, 0, bytes.length);

        return signer.verifySignature(signature);
    }

}
