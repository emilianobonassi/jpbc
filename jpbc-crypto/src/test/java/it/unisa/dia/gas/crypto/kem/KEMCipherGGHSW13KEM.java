package it.unisa.dia.gas.crypto.kem;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.circuit.DefaultCircuit;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.engines.GGHSW13KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators.GGHSW13KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators.GGHSW13ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators.GGHSW13SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.*;
import it.unisa.dia.gas.crypto.kem.cipher.engines.KEMCipher;
import it.unisa.dia.gas.crypto.kem.cipher.params.KEMCipherDecryptionParameters;
import it.unisa.dia.gas.crypto.kem.cipher.params.KEMCipherEncryptionParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import static it.unisa.dia.gas.crypto.circuit.Circuit.Gate.Type.*;
import static org.junit.Assert.assertEquals;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class KEMCipherGGHSW13KEM {
    protected KEMCipher kemCipher;
    protected AlgorithmParameterSpec iv;

    protected AsymmetricCipherKeyPair keyPair;


    public KEMCipherGGHSW13KEM() throws GeneralSecurityException {
        this.kemCipher = new KEMCipher(
                Cipher.getInstance("AES/CBC/PKCS7Padding", "BC"),
                new GGHSW13KEMEngine()
        );

        // build the initialization vector.  This example is all zeros, but it
        // could be any value or generated using a random number generator.
        iv = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }


    public AsymmetricCipherKeyPair setup(int n) {
        GGHSW13KeyPairGenerator setup = new GGHSW13KeyPairGenerator();
        setup.init(new GGHSW13KeyPairGenerationParameters(
                new SecureRandom(),
                new GGHSW13ParametersGenerator().init(
                        PairingFactory.getPairing("params/mm/ctl13/toy.properties"),
                        n).generateParameters()
        ));

        return (keyPair = setup.generateKeyPair());
    }


    public byte[] initEncryption(String assignment) {
        try {
            return kemCipher.init(
                    true,
                    new KEMCipherEncryptionParameters(
                            128,
                            new GGHSW13EncryptionParameters(
                                    (GGHSW13PublicKeyParameters) keyPair.getPublic(),
                                    assignment
                            )
                    ),
                    iv
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(String message) {
        try {
            return kemCipher.doFinal(message.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public CipherParameters keyGen(Circuit circuit) {
        GGHSW13SecretKeyGenerator keyGen = new GGHSW13SecretKeyGenerator();
        keyGen.init(new GGHSW13SecretKeyGenerationParameters(
                (GGHSW13PublicKeyParameters) keyPair.getPublic(),
                (GGHSW13MasterSecretKeyParameters) keyPair.getPrivate(),
                circuit
        ));

        return keyGen.generateKey();
    }

    public byte[] decrypt(CipherParameters secretKey, byte[] encapsulation, byte[] ciphertext) {
        try {
            kemCipher.init(
                    false,
                    new KEMCipherDecryptionParameters(secretKey, encapsulation, 128),
                    iv
            );
            return kemCipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            // Setup
            int n = 4;
            KEMCipherGGHSW13KEM engine = new KEMCipherGGHSW13KEM();
            engine.setup(n);

            // Encrypt
            String message = "Hello World!!!";
            byte[] encapsulation = engine.initEncryption("1101");
            byte[] ciphertext = engine.encrypt(message);

            // Decrypt
            int q = 3;
            Circuit circuit = new DefaultCircuit(n, q, 3, new DefaultCircuit.DefaultGate[]{
                    new DefaultCircuit.DefaultGate(INPUT, 0, 1),
                    new DefaultCircuit.DefaultGate(INPUT, 1, 1),
                    new DefaultCircuit.DefaultGate(INPUT, 2, 1),
                    new DefaultCircuit.DefaultGate(INPUT, 3, 1),

                    new DefaultCircuit.DefaultGate(AND, 4, 2, new int[]{0, 1}),
                    new DefaultCircuit.DefaultGate(OR, 5, 2, new int[]{2, 3}),

                    new DefaultCircuit.DefaultGate(AND, 6, 3, new int[]{4, 5}),
            });
            byte[] plaintext = engine.decrypt(engine.keyGen(circuit), encapsulation, ciphertext);

            assertEquals(true, message.equals(new String(plaintext)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ExecutorServiceUtils.shutdown();
        }
    }

}