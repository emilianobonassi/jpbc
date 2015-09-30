package it.unisa.dia.gas.crypto.jpbc.wtor.gvw13.params;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WTORGVW13PublicKeyParameters extends WTORGVW13KeyParameters {

    private Element left;
    private Element right;
    private int level;

    public WTORGVW13PublicKeyParameters(WTORGVW13Parameters parameters, Element left, Element right, int level) {
        super(false, parameters);

        this.left = left;
        this.right = right;
        this.level = level;
    }

    public Element getLeft() {
        return left;
    }

    public Element getRight() {
        return right;
    }

    public int getLevel() {
        return level;
    }
}
