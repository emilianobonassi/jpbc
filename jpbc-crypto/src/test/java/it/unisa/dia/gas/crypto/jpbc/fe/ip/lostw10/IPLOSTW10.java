package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.fe.PredicateOnlyEncryptionScheme;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10 {


    public IPLOSTW10() {
    }


    public AsymmetricCipherKeyPair setup(int n) {
        IPLOSTW10KeyPairGenerator setup = new IPLOSTW10KeyPairGenerator();
        setup.init(new IPLOSTW10KeyGenerationParameters(
                new SecureRandom(),
                createParameters(n)
        ));

        return setup.generateKeyPair();
    }

    public IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(
                PairingFactory.getPairingParameters("params/curves/a.properties"),
                n).generateParameters();
    }

    public byte[] encrypt(CipherParameters publicKey, Element[] x) {
        try {
            PredicateOnlyEncryptionScheme engine = new IPLOSTW10PredicateOnlyEngine();
            engine.init(true, new IPLOSTW10EncryptionParameters((IPLOSTW10PublicKeyParameters) publicKey, x));

            return engine.process();
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    public CipherParameters keyGen(CipherParameters privateKey, Element[] y) {
        IPLOSTW10SecretKeyGenerator keyGen = new IPLOSTW10SecretKeyGenerator();
        keyGen.init(new IPLOSTW10SecretKeyGenerationParameters(
                (IPLOSTW10MasterSecretKeyParameters) privateKey,
                y
        ));

        return keyGen.generateKey();
    }

    public boolean test(CipherParameters secretKey, byte[] ciphertext) {
        try {
            PredicateOnlyEncryptionScheme engine = new IPLOSTW10PredicateOnlyEngine();
            engine.init(false, secretKey);

            return engine.evaluate(ciphertext);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }


    public Element[][] createOrthogonalVectors(CipherParameters publicKey, int n) {
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

    public Element[][] createNonOrthogonalVectors(CipherParameters publicKey, int n) {
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



    public static void main(String[] args) {
        IPLOSTW10 iplostw10 = new IPLOSTW10();

        int n = 2;

        // Setup
        AsymmetricCipherKeyPair keyPair = iplostw10.setup(n);

        // Test for orthogonal vectors
        Element[][] vectors = iplostw10.createOrthogonalVectors(keyPair.getPublic(), n);
        assertEquals(true,
                iplostw10.test(
                        iplostw10.keyGen(keyPair.getPrivate(), vectors[1]),
                        iplostw10.encrypt(keyPair.getPublic(), vectors[0])
                )
        );

        // Test for non-orthogonal vectors
        vectors = iplostw10.createNonOrthogonalVectors(keyPair.getPublic(), n);
        assertEquals(false,
                iplostw10.test(
                        iplostw10.keyGen(keyPair.getPrivate(), vectors[1]),
                        iplostw10.encrypt(keyPair.getPublic(), vectors[0])
                )
        );
    }


}