package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutablePBCCurvePointElement extends PBCCurvePointElement {

    public ImmutablePBCCurvePointElement(PBCCurvePointElement pbcElement) {
        super(pbcElement);

        this.immutable = true;
    }


    @Override
    public PBCElement duplicate() {
        return super.duplicate();
    }

    @Override
    public PBCElement getImmutable() {
        return this;
    }

    @Override
    public PBCCurvePointElement set(Element value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement twice() {
        return (PBCCurvePointElement) super.duplicate().twice().getImmutable();
    }

    @Override
    public PBCCurvePointElement mul(int z) {
        return (PBCCurvePointElement) super.duplicate().mul(z).getImmutable();
    }

    @Override
    public PBCCurvePointElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCCurvePointElement square() {
        return (PBCCurvePointElement) super.duplicate().square().getImmutable();
    }

    @Override
    public PBCCurvePointElement invert() {
        return (PBCCurvePointElement) super.duplicate().invert().getImmutable();
    }

    @Override
    public PBCCurvePointElement halve() {
        return (PBCCurvePointElement) super.duplicate().halve().getImmutable();
    }

    @Override
    public PBCCurvePointElement negate() {
        return (PBCCurvePointElement) super.duplicate().negate().getImmutable();
    }

    @Override
    public PBCCurvePointElement add(Element element) {
        return (PBCCurvePointElement) super.duplicate().add(element).getImmutable();
    }

    @Override
    public PBCCurvePointElement sub(Element element) {
        return (PBCCurvePointElement) super.duplicate().sub(element).getImmutable();
    }

    @Override
    public PBCCurvePointElement div(Element element) {
        return (PBCCurvePointElement) super.duplicate().div(element).getImmutable();
    }

    @Override
    public PBCCurvePointElement mul(Element element) {
        return (PBCCurvePointElement) super.duplicate().mul(element).getImmutable();
    }

    @Override
    public PBCCurvePointElement mul(BigInteger n) {
        return (PBCCurvePointElement) super.duplicate().mul(n).getImmutable();
    }

    @Override
    public PBCCurvePointElement mulZn(Element z) {
        return (PBCCurvePointElement) super.duplicate().mulZn(z).getImmutable();
    }

    @Override
    public PBCCurvePointElement sqrt() {
        return (PBCCurvePointElement) super.duplicate().sqrt().getImmutable();
    }

    @Override
    public PBCCurvePointElement pow(BigInteger n) {
        return (PBCCurvePointElement) super.duplicate().pow(n).getImmutable();
    }

    @Override
    public PBCCurvePointElement powZn(Element n) {
        return (PBCCurvePointElement) super.duplicate().powZn(n).getImmutable();
    }

    @Override
    public int setFromBytesCompressed(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesCompressed(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesX(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesX(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }
}