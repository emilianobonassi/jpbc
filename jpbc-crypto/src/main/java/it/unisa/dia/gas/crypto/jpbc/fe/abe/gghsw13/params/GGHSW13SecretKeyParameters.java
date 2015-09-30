package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13SecretKeyParameters extends GGHSW13KeyParameters {

    private Circuit circuit;
    private Map<Integer, Element[]> keys;


    public GGHSW13SecretKeyParameters(GGHSW13Parameters parameters, Circuit circuit, Map<Integer, Element[]> keys) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = keys;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public Element[] getKeyElementsAt(int index) {
        return keys.get(index);
    }
}