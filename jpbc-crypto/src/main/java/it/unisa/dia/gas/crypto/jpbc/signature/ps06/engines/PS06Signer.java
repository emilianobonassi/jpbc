package it.unisa.dia.gas.crypto.jpbc.signature.ps06.engines;

import it.unisa.dia.gas.crypto.jpbc.signature.ps06.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PS06Signer implements Signer {
    private PS06KeyParameters params;
    private Digest digest;

    private Pairing pairing;


    public PS06Signer(Digest digest) {
        this.digest = digest;
    }

    
    public void init(boolean forSigning, CipherParameters param) {
        if (!(param instanceof PS06KeyParameters))
            throw new IllegalArgumentException("Invalid parameters. Expected an instance of PS06KeyParameters.");

        params = (PS06KeyParameters) param;

        if (forSigning && !params.isPrivate())
            throw new IllegalArgumentException("signing requires private key");
        if (!forSigning && params.isPrivate())
            throw new IllegalArgumentException("verification requires public key");

        this.pairing = PairingFactory.getPairing(params.getParameters().getCurveParams());

        // Reset the digest
        digest.reset();
    }

    public boolean verifySignature(byte[] signature) {
        if (params == null)
            throw new IllegalStateException("PS06 engine not initialised");

        PS06VerifyParameters verifyParameters = (PS06VerifyParameters) params;
        PS06Parameters parameters = verifyParameters.getParameters();
        PS06PublicKeyParameters publicKey = verifyParameters.getPublicKey();
        String identity = verifyParameters.getIdentity();

        // load values
        int offset = 0;
        Element V = pairing.getG1().newElement();
        offset += V.setFromBytes(signature, offset);
        Element Ru = pairing.getG1().newElement();
        offset += Ru.setFromBytes(signature, offset);
        Element Rm = pairing.getG1().newElement();
        Rm.setFromBytes(signature, offset);

        // compute the digest
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);
        BigInteger message = new BigInteger(hash);

        // compute left part
        Element left = pairing.pairing(V, parameters.getG());

        // compute right part
        Element r1 = pairing.pairing(publicKey.getG2(), publicKey.getG1());

        Element idEncoding = publicKey.getuPrime();
        for (int i = 0; i < identity.length(); i++) {
            if (identity.charAt(i) == '1')
                idEncoding = idEncoding.mul(publicKey.getUAt(i));
        }
        Element r2 = pairing.pairing(idEncoding, Ru);

        Element msgEncoding = publicKey.getmPrime();
        for (int i = 0; i < message.bitLength(); i++) {
            if (message.testBit(i)) {
                msgEncoding = msgEncoding.mul(publicKey.getMAt(i));
            }
        }

        Element r3 = pairing.pairing(msgEncoding, Rm);

        return left.isEqual(r1.mul(r2).mul(r3));
    }

    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (params == null)
            throw new IllegalStateException("PS06 engine not initialised");

        // get params
        PS06SignParameters signParameters = (PS06SignParameters) params;
        PS06SecretKeyParameters secretKey = signParameters.getSecretKey();
        PS06PublicKeyParameters publicKey = secretKey.getPublicKey();

        // Compute the digest
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);
        BigInteger message = new BigInteger(hash);

        // Compute the signature
        Element r = pairing.getZr().newRandomElement();

        Element msgEncoding = publicKey.getmPrime();
        for (int i = 0; i < message.bitLength(); i++) {
            if (message.testBit(i))
                msgEncoding = msgEncoding.mul(publicKey.getMAt(i));
        }
        Element V = secretKey.getD1().mul(msgEncoding.powZn(r));

        Element Ru = secretKey.getD2();
        Element Rm = publicKey.getParameters().getG().powZn(r);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            bytes.write(V.toBytes());
            bytes.write(Ru.toBytes());
            bytes.write(Rm.toBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes.toByteArray();
    }

    public void reset() {
        digest.reset();
    }

    public void update(byte b) {
        digest.update(b);
    }

    public void update(byte[] in, int off, int len) {
        digest.update(in, off, len);
    }

}
