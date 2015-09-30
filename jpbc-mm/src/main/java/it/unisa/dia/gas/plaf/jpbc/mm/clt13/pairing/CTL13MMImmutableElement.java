package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMImmutableElement extends CTL13MMElement {

    public CTL13MMImmutableElement(CTL13MMField field, BigInteger value, int index) {
        super(field, value, index);
    }

    public boolean isImmutable() {
        return true;
    }

    public Element getImmutable() {
        return this;
    }

    public Element duplicate() {
        return new CTL13MMElement(field, value, index);
    }

    public Element set(Element value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    public Element twice() {
        return duplicate().twice().getImmutable();
    }

    public Element square() {
        return duplicate().square().getImmutable();
    }

    public Element invert() {
        return duplicate().invert().getImmutable();
    }

    public Element halve() {
        return duplicate().halve().getImmutable();
    }

    public Element negate() {
        return duplicate().negate().getImmutable();
    }

    public Element add(Element element) {
        return duplicate().add(element).getImmutable();
    }

    public Element sub(Element element) {
        return duplicate().sub(element).getImmutable();
    }

    public Element mul(Element element) {
        return duplicate().mul(element).getImmutable();
    }

    public Element mul(int z) {
        return duplicate().mul(z).getImmutable();
    }

    public Element mul(BigInteger n) {
        return duplicate().mul(n).getImmutable();
    }

    public Element mulZn(Element z) {
        return duplicate().mulZn(z).getImmutable();
    }

    public Element div(Element element) {
        return duplicate().div(element).getImmutable();
    }

    public Element pow(BigInteger n) {
        return duplicate().pow(n).getImmutable();
    }

    public Element powZn(Element n) {
        return duplicate().powZn(n).getImmutable();
    }

    public Element sqrt() {
        return duplicate().sqrt().getImmutable();
    }

}
