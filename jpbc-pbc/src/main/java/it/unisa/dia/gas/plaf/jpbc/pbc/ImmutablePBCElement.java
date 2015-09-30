package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutablePBCElement extends PBCElement {

    public ImmutablePBCElement(PBCElement pbcElement) {
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
    public PBCElement set(Element value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCElement twice() {
        return super.duplicate().twice().getImmutable();    
    }

    @Override
    public PBCElement mul(int z) {
        return super.duplicate().mul(z).getImmutable();
    }

    @Override
    public PBCElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public PBCElement setFromHash(byte[] source, int offset, int length) {
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
    public PBCElement square() {
        return super.duplicate().square().getImmutable();    
    }

    @Override
    public PBCElement invert() {
        return super.duplicate().invert().getImmutable();    
    }

    @Override
    public PBCElement halve() {
        return super.duplicate().halve().getImmutable();    
    }

    @Override
    public PBCElement negate() {
        return super.duplicate().negate().getImmutable();    
    }

    @Override
    public PBCElement add(Element element) {
        return super.duplicate().add(element).getImmutable();
    }

    @Override
    public PBCElement sub(Element element) {
        return super.duplicate().sub(element).getImmutable();
    }

    @Override
    public PBCElement div(Element element) {
        return super.duplicate().div(element).getImmutable();
    }

    @Override
    public PBCElement mul(Element element) {
        return super.duplicate().mul(element).getImmutable();
    }

    @Override
    public PBCElement mul(BigInteger n) {
        return super.duplicate().mul(n).getImmutable();
    }

    @Override
    public PBCElement mulZn(Element z) {
        return super.duplicate().mulZn(z).getImmutable();
    }

    @Override
    public PBCElement sqrt() {
        return super.duplicate().sqrt().getImmutable();    
    }

    @Override
    public PBCElement pow(BigInteger n) {
        return super.duplicate().pow(n).getImmutable();
    }

    @Override
    public PBCElement powZn(Element n) {
        return super.duplicate().powZn(n).getImmutable();
    }
    
}
