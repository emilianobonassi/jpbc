package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13SecretKeyParameters extends GGHVV13KeyParameters {

    private Circuit circuit;
    private Element[][] M;
    private Map<Integer, Element[]> keys;


    public GGHVV13SecretKeyParameters(GGHVV13Parameters parameters,
                                      Circuit circuit,
                                      Map<Integer, Element[]> keys,
                                      Element[][] M) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = ElementUtils.cloneImmutable(keys);
        this.M = M;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public Element[] getKeyElementsAt(int index) {
        return keys.get(index);
    }

    public Element getMAt(int i, int j) {
        return M[i][j];
    }

}