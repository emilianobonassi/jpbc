package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulatorFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08KEMEngine extends PairingKeyEncapsulationMechanism {

    private int n;

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof HVEIP08EncryptionParameters))
                throw new IllegalArgumentException("HVEIP08EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof HVEIP08SecretKeyParameters))
                throw new IllegalArgumentException("HVEIP08SecretKeyParameters are required for decryption.");
        }

        HVEIP08KeyParameters hveKey = (HVEIP08KeyParameters) key;
        this.pairing = PairingFactory.getPairing(hveKey.getParameters().getParameters());
        this.n = hveKey.getParameters().getN();

        this.keyBytes = pairing.getGT().getLengthInBytes();
        this.outBytes = ((2 * n + 1) * pairing.getG1().getLengthInBytes()) + (2 * pairing.getGT().getLengthInBytes());
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof HVEIP08SecretKeyParameters) {
            // decaps

            // Convert bytes to Elements...
            int offset = inOff;

            // Load Omega
            Element Omega = pairing.getGT().newElement();
            offset += Omega.setFromBytes(in, offset);

            // Load C0
            Element C0 = pairing.getG1().newElement();
            offset += C0.setFromBytes(in, offset);

            HVEIP08SecretKeyParameters secretKey = (HVEIP08SecretKeyParameters) key;
            if (secretKey.isAllStar()) {
                return Omega.mul(pairing.pairing(C0, secretKey.getK())).toBytes();
            } else {
                // load Xs, Ws
                List<Element> X = new ArrayList<Element>();
                List<Element> W = new ArrayList<Element>();
                for (int i = 0; i < n; i++) {
                    Element x = pairing.getG1().newElement();
                    offset += x.setFromBytes(in, offset);
                    X.add(x);

                    Element w = pairing.getG1().newElement();
                    offset += w.setFromBytes(in, offset);
                    W.add(w);
                }

                PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(pairing, Omega);
                if (secretKey.isPreProcessed()) {
                    for (int i = 0; i < secretKey.getParameters().getN(); i++) {
                        if (!secretKey.isStar(i)) {
                            combiner.addPairing(secretKey.getPreYAt(i), X.get(i));
                            combiner.addPairing(secretKey.getPreLAt(i), W.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < secretKey.getParameters().getN(); i++) {
                        if (!secretKey.isStar(i)) {
                            combiner.addPairing(secretKey.getYAt(i), X.get(i));
                            combiner.addPairing(secretKey.getLAt(i), W.get(i));
                        }
                    }
                }
                Element result = combiner.awaitResult();

                return result.toBytes();
            }
        } else {
            // encap
            Element M = pairing.getGT().newRandomElement().getImmutable();

            HVEIP08EncryptionParameters encParams = (HVEIP08EncryptionParameters) key;
            HVEIP08PublicKeyParameters pk = encParams.getPublicKey();

            Element s = pairing.getZr().newRandomElement().getImmutable();

            Element Omega = M.mul(pk.getY().powZn(s.negate()));
            Element C0 = pk.getParameters().getElementPowG().powZn(s);

            List<Element> elements = new ArrayList<Element>();

            for (int i = 0; i < n; i++) {
                Element si = pairing.getZr().newElement().setToRandom();
                Element sMinusSi = s.sub(si);

                int j = encParams.getAttributeAt(i);

                // Populate elements
                elements.add(pk.getElementPowTAt(i, j).powZn(sMinusSi));  // X_i
                elements.add(pk.getElementPowVAt(i, j).powZn(si));        // W_i
            }

            // Convert the Elements to byte arrays
            ByteArrayOutputStream outputStream;
            try {
                outputStream = new ByteArrayOutputStream(getOutputBlockSize());
                outputStream.write(M.toBytes());
                outputStream.write(Omega.toBytes());
                outputStream.write(C0.toBytes());

                for (Element element : elements)
                    outputStream.write(element.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return outputStream.toByteArray();
        }
    }


}