package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.Arrays;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementPPType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCElement implements Element {
    protected Pointer value;
    protected PBCField field;
    protected PBCElementPPType elementPPType;
    protected boolean immutable;


    public PBCElement(Pointer value, PBCField field) {
        this.value = value;
        this.field = field;
        this.immutable = false;
    }

    public PBCElement(PBCElement pbcElement) {
        PBCElement duplicate = pbcElement.duplicate();

        this.value = duplicate.value;
        this.field = duplicate.field;
        this.immutable = false;
    }


    public Field getField() {
        return field;
    }

    public int getLengthInBytes() {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(this.value);
    }

    public boolean isImmutable() {
        return immutable;
    }

    public PBCElement getImmutable() {
        return new ImmutablePBCElement(this);
    }

    public PBCElement duplicate() {
        return (PBCElement) field.newElement(this);
    }

    public PBCElement set(Element value) {
        PBCElement pbcElement = (PBCElement) value;

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_set(this.value, pbcElement.value);
        this.elementPPType = pbcElement.elementPPType; 

        return this;
    }

    public PBCElement set(int value) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_set_si(this.value, value);

        return this;
    }

    public PBCElement set(BigInteger value) {
        MPZElementType z = MPZElementType.fromBigInteger(value);
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_set_mpz(this.value, z);

        return this;
    }

    public PBCElement setToRandom() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_random(value);

        return this;
    }

    public PBCElement setFromHash(byte[] source, int offset, int length) {
        Memory memory = new Memory(length);
        memory.write(0, source, offset, length);

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_from_hash(value, memory, source.length);

        return this;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        int lengthInBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(value);

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_from_bytes(value, Arrays.copyOf(source, offset, lengthInBytes));

        return lengthInBytes;
    }

    public PBCElement setToZero() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_set0(value);

        return this;
    }

    public boolean isZero() {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_element_is0(this.value) == 1;
    }

    public PBCElement setToOne() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_set1(value);
        
        return this;
    }

    public boolean isOne() {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_element_is1(this.value) == 1;
    }

    public PBCElement twice() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_double(this.value, this.value);

        return this;
    }

    public PBCElement square() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_square(this.value, this.value);

        return this;
    }

    public PBCElement invert() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_invert(value, value);

        return this;
    }

    public PBCElement halve() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_halve(this.value, this.value);

        return this;
    }

    public PBCElement negate() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_neg(this.value, this.value);

        return this;
    }

    public PBCElement add(Element element) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_add(value, value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement sub(Element element) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_sub(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement div(Element element) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_div(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement mul(Element element) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_mul(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement mul(int z) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_mul_si(this.value, this.value, z);

        return this;
    }

    public PBCElement mul(BigInteger n) {
        MPZElementType z = MPZElementType.fromBigInteger(n);
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_mul_mpz(this.value, this.value, z);

        return this;
    }

    public PBCElement mulZn(Element z) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_mul_zn(value, value, ((PBCElement) z).value);
                
        return this;
    }

    public PBCElement pow(BigInteger n) {
        MPZElementType z = MPZElementType.fromBigInteger(n);
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pow_mpz(this.value, this.value, z);

        return this;
    }

    public PBCElement powZn(Element n) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pow_zn(value, value, ((PBCElement) n).value);

        return this;
    }

    public PBCElement sqrt() {
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_sqrt(value, value);

        return this;
    }

    public boolean isSqr() {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_element_is_sqr(value) == 1;
    }

    public int sign() {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_element_sign(value);
    }

    public BigInteger toBigInteger() {
        MPZElementType mpzElement = new MPZElementType();
        mpzElement.init();

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_to_mpz(mpzElement, value);

        return new BigInteger(mpzElement.toString(10));
    }

    public byte[] toBytes() {
        int numBytes = WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(value);
        byte[] bytes = new byte[numBytes];

        WrapperLibraryProvider.getWrapperLibrary().pbc_element_to_bytes(bytes, value);

        return bytes;
    }

    public byte[] toCanonicalRepresentation() {
        return toBytes();
    }

    public boolean isEqual(Element e) {
        return this == e || (e instanceof PBCElement && WrapperLibraryProvider.getWrapperLibrary().pbc_element_cmp(value, ((PBCElement) e).value) == 0);

    }

    public ElementPowPreProcessing getElementPowPreProcessing() {
        return new PBCElementPowPreProcessing(field, value);
    }

    @Override
    public String toString() {
        Memory memory = new Memory(getField().getLengthInBytes()*3);
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_snprint(memory, getField().getLengthInBytes()*3, value);
        return memory.getString(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Element && isEqual((Element) obj);
    }

    public Pointer getValue() {
        return value;
    }
}
