package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private HVEIP08KeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (HVEIP08KeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        HVEIP08Parameters parameters = param.getParameters();
        parameters.preProcess();

        Pairing pairing = PairingFactory.getPairing(parameters.getParameters());
        Element g = parameters.getG();
        ElementPow powG = parameters.getElementPowG();
        int n = parameters.getN();

        // Init Y
        Element y = pairing.getZr().newElement().setToRandom();
        Element Y = pairing.pairing(g, g).powZn(y);

        // Init
        List<List<Element>> T = new ArrayList<List<Element>>(n);
        List<List<Element>> t = new ArrayList<List<Element>>(n);

        List<List<Element>> V = new ArrayList<List<Element>>(n);
        List<List<Element>> v = new ArrayList<List<Element>>(n);

        for (int i = 0; i < n ; i++) {

            int howMany = parameters.getAttributeNumAt(i);
            List<Element> T_i = new ArrayList<Element>();
            List<Element> t_i = new ArrayList<Element>();

            List<Element> V_i = new ArrayList<Element>();
            List<Element> v_i = new ArrayList<Element>();

            for (int j = 0; j < howMany; j++) {
                Element t_j = pairing.getZr().newElement().setToRandom();
                T_i.add(powG.powZn(t_j).getImmutable());
                t_i.add(t_j.getImmutable());

                Element v_j = pairing.getZr().newElement().setToRandom();
                V_i.add(powG.powZn(v_j).getImmutable());
                v_i.add(v_j.getImmutable());
            }

            T.add(T_i);
            t.add(t_i);

            V.add(V_i);
            v.add(v_i);
        }

        return new AsymmetricCipherKeyPair(
            new HVEIP08PublicKeyParameters(parameters, Y.getImmutable(), T, V),
            new HVEIP08MasterSecretKeyParameters(parameters, y.getImmutable(), t, v)
        );
    }

}