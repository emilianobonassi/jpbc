package it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01Signer implements Signer {
    private BLS01KeyParameters keyParameters;
    private Digest digest;

    private Pairing pairing;


    public BLS01Signer(Digest digest) {
        this.digest = digest;
    }

    
    public void init(boolean forSigning, CipherParameters param) {
        if (!(param instanceof BLS01KeyParameters))
            throw new IllegalArgumentException("Invalid parameters. Expected an instance of BLS01KeyParameters.");

        keyParameters = (BLS01KeyParameters) param;

        if (forSigning && !keyParameters.isPrivate())
            throw new IllegalArgumentException("signing requires private key");
        if (!forSigning && keyParameters.isPrivate())
            throw new IllegalArgumentException("verification requires public key");

        this.pairing = PairingFactory.getPairing(keyParameters.getParameters().getParameters());

        // Reset the digest
        digest.reset();
    }

    public boolean verifySignature(byte[] signature) {
        if (keyParameters == null)
            throw new IllegalStateException("BLS engine not initialised");

        BLS01PublicKeyParameters publicKey = (BLS01PublicKeyParameters) keyParameters;

        Element sig = pairing.getG1().newElementFromBytes(signature);

        // Generate the digest
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);

        // Map the hash of the message m to some element of G1
        Element h = pairing.getG1().newElementFromHash(hash, 0, hash.length);

        Element temp1 = pairing.pairing(sig, publicKey.getParameters().getG());
        Element temp2 = pairing.pairing(h, publicKey.getPk());

        return temp1.isEqual(temp2);
    }

    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (keyParameters == null)
            throw new IllegalStateException("BLS engine not initialised");

        BLS01PrivateKeyParameters privateKey = (BLS01PrivateKeyParameters) keyParameters;

        // Generate the digest
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);

        // Map the hash of the message m to some element of G1
        Element h = pairing.getG1().newElementFromHash(hash, 0, hash.length);

        // Generate the signature
        Element sig = h.powZn(privateKey.getSk());

        return sig.toBytes();
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
