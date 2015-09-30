package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMField implements Field<CTL13MMElement> {

    private CTL13MMPairing pairing;
    private CTL13MMInstance instance;
    private int index;
    private int lengthInBytes;


    public CTL13MMField(CTL13MMPairing pairing) {
        this(pairing, 0);
    }

    public CTL13MMField(CTL13MMPairing pairing, int index) {
        this.pairing = pairing;
        this.instance = pairing.getCTL13MMInstance();
        this.index = index;

        this.lengthInBytes =
                ((pairing.getCTL13MMInstance().getSystemParameters().getN() *
                pairing.getCTL13MMInstance().getSystemParameters().getEta() + 7) / 8) + 8;
    }


    public CTL13MMElement newElement() {
        return new CTL13MMElement(this, index);
    }

    public CTL13MMElement newElement(int value) {
        switch (value) {
            case 0:
                return newZeroElement();
            case 1:
                return newOneElement();
            default:
                throw new IllegalStateException("Not Implemented yet!");
        }
    }

    public CTL13MMElement newElement(BigInteger value) {
        if (BigInteger.ZERO.equals(value))
            return newZeroElement();
        if (BigInteger.ONE.equals(value))
            return newOneElement();

        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement newElement(CTL13MMElement ctl13MMElement) {
        if (ctl13MMElement.index == index)
            return new CTL13MMElement(this, ctl13MMElement.value, index);

        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement newElementFromHash(byte[] source, int offset, int length) {
        CTL13MMElement element = newElement();
        element.setFromHash(source, offset, length);

        return element;
    }

    public CTL13MMElement newElementFromBytes(byte[] source) {
        CTL13MMElement element = newElement();
        element.setFromBytes(source);

        return element;
    }

    public CTL13MMElement newElementFromBytes(byte[] source, int offset) {
        CTL13MMElement element = newElement();
        element.setFromBytes(source, offset);

        return element;
    }

    public CTL13MMElement newZeroElement() {
        return (CTL13MMElement) newElement().setToZero();
    }

    public CTL13MMElement newOneElement() {
        return (CTL13MMElement) newElement().setToOne();
    }

    public CTL13MMElement newRandomElement() {
        return new CTL13MMElement(this, instance.encodeAt(instance.sampleAtZero(), 0, index), index);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isOrderOdd() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public CTL13MMElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getLengthInBytes(Element ctl13MMElement) {
        return lengthInBytes;
    }

    public int getCanonicalRepresentationLengthInBytes() {
        return instance.getSystemParameters().getBound() / 8;
    }

    public Element[] twice(Element[] elements) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element[] add(Element[] a, Element[] b) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public ElementPowPreProcessing getElementPowPreProcessingFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!");
    }


    public CTL13MMInstance getInstance() {
        return instance;
    }

    public CTL13MMPairing getPairing() {
        return pairing;
    }

    public int getIndex() {
        return index;
    }
}
