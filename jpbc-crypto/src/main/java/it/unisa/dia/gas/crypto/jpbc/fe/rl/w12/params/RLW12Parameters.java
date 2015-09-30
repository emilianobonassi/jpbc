package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import it.unisa.dia.gas.crypto.dfa.DFA;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class RLW12Parameters implements CipherParameters {
    private PairingParameters parameters;
    private Element g;
    private DFA.Alphabet alphabet;


    public RLW12Parameters(PairingParameters parameters, Element g, DFA.Alphabet alphabet) {
        this.parameters = parameters;
        this.g = g.getImmutable();
        this.alphabet = alphabet;
    }


    public PairingParameters getParameters() {
        return parameters;
    }

    public Element getG() {
        return g;
    }

    public int getAlphabetSize() {
        return alphabet.getSize();
    }

    public int getCharacterIndex(Character character) {
        return alphabet.getIndex(character);
    }

}