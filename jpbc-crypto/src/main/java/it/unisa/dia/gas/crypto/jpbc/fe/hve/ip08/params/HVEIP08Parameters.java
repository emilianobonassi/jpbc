package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08Parameters implements CipherParameters {
    private PairingParameters parameters;
    private Element g;
    private int[] attributeLengths;

    private ElementPowPreProcessing powG;

    private int[] attributeLengthsInBytes;
    private int[] attributeNums;
    private int n;
    private int attributesLengthInBytes;

    private boolean preProcessed = false;


    public HVEIP08Parameters(PairingParameters parameters, Element g, int[] attributeLengths) {
        this.parameters = parameters;
        this.g = g.getImmutable();
        this.n = attributeLengths.length;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);

        this.attributesLengthInBytes = 0;
        this.attributeLengthsInBytes = new int[n];
        this.attributeNums = new int[n];
        for (int i = 0; i < attributeLengths.length; i++) {
            int attributeLength = attributeLengths[i];

            // Optimize this...
            attributeLengthsInBytes[i] = attributeLength / 8 + 1;
            attributesLengthInBytes += attributeLengthsInBytes[i];

            attributeNums[i] = (int) Math.pow(2, attributeLength);
        }
    }


    public PairingParameters getParameters() {
        return parameters;
    }

    public Element getG() {
        return g;
    }

    public ElementPow getElementPowG() {
        return (preProcessed) ? powG : g;
    }

    public int getN() {
        return n;
    }

    public int[] getAttributeLengths() {
        return Arrays.copyOf(attributeLengths, attributeLengths.length);
    }

    public int getAttributesLengthInBytes() {
        return attributesLengthInBytes;
    }

    public int getAttributeLengthInBytesAt(int index) {
        return attributeLengthsInBytes[index];
    }

    public int getAttributeNumAt(int index) {
        return attributeNums[index];
    }

    public void preProcess() {
        if (preProcessed)
            return;

        this.powG = g.getElementPowPreProcessing();
        this.preProcessed = true;
    }

    public boolean isPreProcessed() {
        return preProcessed;
    }

}