package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12;

import it.unisa.dia.gas.crypto.dfa.DFA;
import it.unisa.dia.gas.crypto.dfa.DefaultDFA;
import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines.RLW12KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators.RLW12KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators.RLW12ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators.RLW12SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.*;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro
 */
public class RLW12KEMEngineTest extends AbstractJPBCCryptoTest {


    public RLW12KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testRLW12KEMEngine() {
        DefaultDFA dfa = new DefaultDFA(2);
        dfa.addFinalState(0);
        dfa.addTransition(0, '0', 1);
        dfa.addTransition(0, '1', 0);
        dfa.addTransition(1, '0', 0);
        dfa.addTransition(1, '1', 1);

        DefaultDFA.DefaultAlphabet alphabet = new DefaultDFA.DefaultAlphabet();
        alphabet.addLetter('0', '1');

        AsymmetricCipherKeyPair keyPair = setup(createParameters(alphabet));
        CipherParameters secretKey = keyGen(keyPair.getPublic(), keyPair.getPrivate(), dfa);

        String w = "00111100";
        assertTrue(dfa.accept(w));
        byte[][] ct = encaps(keyPair.getPublic(), w);
        assertEquals(true, Arrays.equals(ct[0], decaps(secretKey, ct[1])));

        w = "01111100";
        assertFalse(dfa.accept(w));
        ct = encaps(keyPair.getPublic(), "01111100");
        assertEquals(false, Arrays.equals(ct[0], decaps(secretKey, ct[1])));
    }


    protected RLW12Parameters createParameters(DFA.Alphabet alphabet) {
        return new RLW12ParametersGenerator().init(parameters, alphabet).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(RLW12Parameters parameters) {
        RLW12KeyPairGenerator setup = new RLW12KeyPairGenerator();
        setup.init(new RLW12KeyPairGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new RLW12KEMEngine();
            kem.init(true, new RLW12EncryptionParameters((RLW12PublicKeyParameters) publicKey, w));

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

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, DFA dfa) {
        // Init the Generator
        RLW12SecretKeyGenerator keyGen = new RLW12SecretKeyGenerator();
        keyGen.init(new RLW12SecretKeyGenerationParameters(
                (RLW12PublicKeyParameters) publicKey,
                (RLW12MasterSecretKeyParameters) masterSecretKey,
                dfa
        ));

        // Generate the key
        return keyGen.generateKey();
    }


    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new RLW12KEMEngine();

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

