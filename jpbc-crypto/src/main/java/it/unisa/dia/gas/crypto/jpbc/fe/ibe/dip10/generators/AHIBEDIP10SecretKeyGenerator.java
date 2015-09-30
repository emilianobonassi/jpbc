package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AHIBEDIP10SecretKeyGenerator implements CipherParametersGenerator {
    private KeyGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = keyGenerationParameters;
    }

    public CipherParameters generateKey() {
        if (params instanceof AHIBEDIP10SecretKeyGenerationParameters) {
            AHIBEDIP10SecretKeyGenerationParameters parameters = (AHIBEDIP10SecretKeyGenerationParameters) params;

            AHIBEDIP10PublicKeyParameters pk = parameters.getPublicKey();
            AHIBEDIP10MasterSecretKeyParameters msk = parameters.getMasterSecretKey();
            Pairing pairing = PairingFactory.getPairing(msk.getParameters());

            // Generate the key
            Element r1 = pairing.getZr().newRandomElement();
            Element r2 = pairing.getZr().newRandomElement();

            // K11
            Element K11 = pk.getY1().powZn(r1).mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // K12
            Element K12 = pairing.getG1().newOneElement();
            int idNum = parameters.getLength();
            int uNum = pk.getLength();

            for (int i = 0; i < idNum; i++) {
                K12.mul(pk.getUAt(i).powZn(parameters.getIdAt(i)));
            }

            K12.mul(msk.getX1())
              .powZn(r1)
              .mul(pk.getY1().powZn(msk.getAlpha()))
              .mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // E1s
            Element[] E1s = new Element[uNum - idNum];
            for (int i = 0; i < E1s.length; i++) {
                E1s[i] = pk.getUAt(idNum + i).powZn(r1).mul(ElementUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            // K21
            Element K21 = pk.getY1().powZn(r2).mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // K22
            Element K22 = pairing.getG1().newOneElement();
            for (int i = 0; i < idNum; i++) {
                K22.mul(pk.getUAt(i).powZn(parameters.getIdAt(i)));
            }
            K22.mul(msk.getX1())
              .powZn(r2)
              .mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // E2s
            Element[] E2s = new Element[uNum - idNum];
            for (int i = 0; i < E2s.length; i++) {
                E2s[i] = pk.getUAt(idNum + i).powZn(r2).mul(ElementUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            return new AHIBEDIP10SecretKeyParameters(pk.getParameters(),
                                                K11.getImmutable(), K12.getImmutable(), E1s,
                                                K21.getImmutable(), K22.getImmutable(), E2s,
                                                parameters.getIds());
        } else if (params instanceof AHIBEDIP10DelegateGenerationParameters) {
            AHIBEDIP10DelegateGenerationParameters parameters = (AHIBEDIP10DelegateGenerationParameters) params;

            AHIBEDIP10PublicKeyParameters pk = parameters.getPublicKey();
            AHIBEDIP10SecretKeyParameters sk = parameters.getSecretKey();
            Pairing pairing = PairingFactory.getPairing(pk.getParameters());

            // Generate the key
            Element r1 = pairing.getZr().newRandomElement();
            Element r2 = pairing.getZr().newRandomElement();

            // K11
            Element K11 = sk.getK11()
                    .mul(sk.getK21().powZn(r1))
                    .mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // K12
            Element K12 = sk.getK12()
                    .mul(sk.getK22().powZn(r1))
                    .mul(sk.getE1At(0).powZn(parameters.getId()))
                    .mul(sk.getE2At(0).powZn(r1.duplicate().mul(parameters.getId())))
                    .mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // E1s
            Element[] E1s = new Element[sk.getE1Length() - 1];
            for (int i = 0; i < E1s.length; i++) {
                E1s[i] = sk.getE1At(i+1)
                        .mul(sk.getE2At(i+1).powZn(r1))
                        .mul(ElementUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            // K21
            Element K21 = sk.getK21().powZn(r2)
                    .mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // K12
            Element K22 = sk.getK22().powZn(r2)
                    .mul(sk.getE2At(0).powZn(r2.duplicate().mul(parameters.getId())))
                    .mul(ElementUtils.randomIn(pairing, pk.getY3()));

            // E1s
            Element[] E2s = new Element[sk.getE1Length() - 1];
            for (int i = 0; i < E1s.length; i++) {
                E2s[i] = sk.getE2At(i+1)
                        .powZn(r2)
                        .mul(ElementUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            Element[] ids = new Element[sk.getIds().length + 1];
            System.arraycopy(sk.getIds(), 0, ids, 0, sk.getIds().length);
            ids[sk.getIds().length] = parameters.getId();

            return new AHIBEDIP10SecretKeyParameters(sk.getParameters(),
                                                K11.getImmutable(), K12.getImmutable(), E1s,
                                                K21.getImmutable(), K22.getImmutable(), E2s,
                                                ids);
        }

        throw new IllegalStateException("AHIBEDIP10SecretKeyGenerator not initialized correctly.");
    }
}
