package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.generators;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params.GGHVV13SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13SecretKeyGenerator {
    private GGHVV13SecretKeyGenerationParameters param;

    private Pairing pairing;
    private Circuit circuit;

    public void init(KeyGenerationParameters param) {
        this.param = (GGHVV13SecretKeyGenerationParameters) param;

        this.pairing = this.param.getMasterSecretKeyParameters().getParameters().getPairing();
        this.circuit = this.param.getCircuit();
    }

    public CipherParameters generateKey() {
        GGHVV13MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        GGHVV13PublicKeyParameters pk = param.getPublicKeyParameters();

        Circuit circuit = this.circuit;
        int n = circuit.getN();

        // sample the randomness
        Element[] rs = new Element[n + circuit.getQ()];
        for (int i = 0; i < rs.length; i++)
            rs[i] = pairing.getZr().newRandomElement().getImmutable();

        Element[] zs = new Element[n];
        for (int i = 0; i < zs.length; i++)
            zs[i] = pairing.getZr().newRandomElement().getImmutable();

        // compute the matrix M

        Element[][] M = new Element[n + 1][n];

        // first row
        for (int j = 0; j < n; j++)
            M[0][j] = pairing.getG1().newElement().powZn(zs[j].negate()).getImmutable();

        // the rest fo the rows

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i+1][j] = pk.getHAt(i).duplicate().powZn(zs[j]);
                if (i == j)
                    M[i+1][j].mul(pairing.getG1().newElement().powZn(rs[i]));
                M[i+1][j] = M[i+1][j].getImmutable();
            }
        }

        // encode the circuit
        Map<Integer, Element[]> keys = new HashMap<Integer, Element[]>();

        Element ePrime = pairing.getFieldAt(circuit.getDepth()).newElement().powZn(msk.getAlpha().sub(rs[rs.length - 1]));
        keys.put(-1, new Element[]{ePrime});

        for (Circuit.Gate gate : circuit) {
            int index = gate.getIndex();
            int depth = gate.getDepth();

            switch (gate.getType()) {
                case INPUT:
                    break;

                case OR:
                    Element a = pairing.getZr().newRandomElement();
                    Element b = pairing.getZr().newRandomElement();

                    Element e1 = pairing.getG1().newElement().powZn(a);
                    Element e2 = pairing.getG1().newElement().powZn(b);

                    Element e3 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(a.mul(rs[gate.getInputIndexAt(0)]))
                    );
                    Element e4 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(b.mul(rs[gate.getInputIndexAt(1)]))
                    );

                    keys.put(index, new Element[]{e1, e2, e3, e4});
                    break;

                case AND:
                    a = pairing.getZr().newRandomElement();
                    b = pairing.getZr().newRandomElement();

                    e1 = pairing.getG1().newElement().powZn(a);
                    e2 = pairing.getG1().newElement().powZn(b);

                    e3 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(a.mul(rs[gate.getInputIndexAt(0)]))
                                    .sub(b.mul(rs[gate.getInputIndexAt(1)]))
                    );

                    keys.put(index, new Element[]{e1, e2, e3});
                    break;
            }
        }

        return new GGHVV13SecretKeyParameters(
                param.getPublicKeyParameters().getParameters(), circuit, keys, M
        );
    }

}