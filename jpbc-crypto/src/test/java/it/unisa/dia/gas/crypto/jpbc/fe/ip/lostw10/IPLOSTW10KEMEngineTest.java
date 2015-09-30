package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro
 */
public class IPLOSTW10KEMEngineTest extends AbstractJPBCCryptoTest {


    public IPLOSTW10KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testIPLOSTW10KEMEngine() {
        int n = 10;

        AsymmetricCipherKeyPair keyPair = setup(createParameters(n));

        Element[][] vectors = createOrthogonalVectors(keyPair.getPublic(), n);
        byte[][] ct = encaps(keyPair.getPublic(), vectors[0]);
        assertEquals(true, Arrays.equals(ct[0], decaps(keyGen(keyPair.getPrivate(), vectors[1]), ct[1])));

        vectors = createNonOrthogonalVectors(keyPair.getPublic(), n);
        ct = encaps(keyPair.getPublic(), vectors[0]);
        assertEquals(false, Arrays.equals(ct[0], decaps(keyGen(keyPair.getPrivate(), vectors[1]), ct[1])));
    }


    protected IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(parameters, n).generateParameters();
    }

    protected Element[][] createOrthogonalVectors(CipherParameters publicKey, int n) {
        Pairing pairing = PairingFactory.getPairing(((IPLOSTW10PublicKeyParameters) publicKey).getParameters().getParameters());

        Element[][] result = new Element[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i += 2) {
            if (random.nextBoolean()) {
                result[0][i] = pairing.getZr().newZeroElement();
                result[0][i + 1] = pairing.getZr().newZeroElement();

                result[1][i] = pairing.getZr().newRandomElement();
                result[1][i + 1] = pairing.getZr().newRandomElement();
            } else {
                result[0][i] = pairing.getZr().newOneElement();
                result[0][i + 1] = pairing.getZr().newRandomElement();

                result[1][i] = result[0][i + 1].duplicate().negate();
                result[1][i + 1] = pairing.getZr().newOneElement();
            }
        }
        return result;
    }

    protected Element[][] createNonOrthogonalVectors(CipherParameters publicKey, int n) {
        Pairing pairing = PairingFactory.getPairing(((IPLOSTW10PublicKeyParameters) publicKey).getParameters().getParameters());

        Element[][] result = new Element[2][n];
        for (int i = 0; i < n; i += 2) {
            result[0][i] = pairing.getZr().newOneElement();
            result[0][i + 1] = pairing.getZr().newRandomElement();

            result[1][i] = pairing.getZr().newOneElement().sub(result[0][i + 1]);
            result[1][i + 1] = pairing.getZr().newOneElement();
        }
        return result;
    }


    protected AsymmetricCipherKeyPair setup(IPLOSTW10Parameters parameters) {
        IPLOSTW10KeyPairGenerator setup = new IPLOSTW10KeyPairGenerator();
        setup.init(new IPLOSTW10KeyGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, Element[] x) {
        try {
            KeyEncapsulationMechanism kem = new IPLOSTW10KEMEngine();
            kem.init(true, new IPLOSTW10EncryptionParameters((IPLOSTW10PublicKeyParameters) publicKey, x));

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);

            byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
            byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

            return new byte[][]{key, ct};
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    protected CipherParameters keyGen(CipherParameters masterSecretKey, Element[] y) {
        // Init the Generator
        IPLOSTW10SecretKeyGenerator keyGen = new IPLOSTW10SecretKeyGenerator();
        keyGen.init(new IPLOSTW10SecretKeyGenerationParameters(
                (IPLOSTW10MasterSecretKeyParameters) masterSecretKey,
                y
        ));

        // Generate the key
        return keyGen.generateKey();
    }


    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new IPLOSTW10KEMEngine();

            kem.init(false, secretKey);
            byte[] key = kem.processBlock(ciphertext, 0, ciphertext.length);

            assertNotNull(key);
            assertNotSame(0, key.length);

            return key;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return null;
    }


}

