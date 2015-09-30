package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.Arrays;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMElement implements Element {

    protected CTL13MMField field;

    protected BigInteger value;
    protected int index;


    public CTL13MMElement(CTL13MMField field, BigInteger value, int index) {
        this.field = field;
        this.value = value;
        this.index = index;
    }

    public CTL13MMElement(CTL13MMField field, int index) {
        this.field = field;
        this.index = index;
        this.value = field.getInstance().encodeOneAt(index);
    }


    public Field getField() {
        return field;
    }

    public int getLengthInBytes() {
        return field.getLengthInBytes();
    }

    public boolean isImmutable() {
        return false;
    }

    public Element getImmutable() {
        return new CTL13MMImmutableElement(field, value, index);
    }

    public Element duplicate() {
        return new CTL13MMElement(field, value, index);
    }

    public Element set(Element value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element set(int value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element set(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public BigInteger toBigInteger() {
        return value;
    }

    public Element setToRandom() {
        this.value = field.getInstance().sampleAtLevel(index);

        return this;
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(source, offset, source.length - offset));

            this.index = dis.readInt();
            int length = dis.readInt();
            byte[] bytes = new byte[length];
            dis.readFully(bytes);

            this.value = new BigInteger(bytes);
            this.field = (CTL13MMField) field.getPairing().getFieldAt(index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return getLengthInBytes();
    }

    public byte[] toBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(field.getLengthInBytes());
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(index);

            byte[] bytes = value.toByteArray();
            dos.writeInt(bytes.length);

            int valueLengthInBytes = field.getLengthInBytes() - 8;

            if (bytes.length > valueLengthInBytes) {
                // strip the zero prefix
                if (bytes[0] == 0 && bytes.length == valueLengthInBytes + 1) {
                    // Remove it
                    bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
                } else
                    throw new IllegalStateException("result has more than FixedLengthInBytes.");
            } else if (bytes.length < valueLengthInBytes) {
                byte[] result = new byte[valueLengthInBytes];
                System.arraycopy(bytes, 0, result, 0, bytes.length);
                bytes = result;
            }
            dos.write(bytes);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] toCanonicalRepresentation() {
        return field.getInstance().extract(value, index).toByteArray();
    }

    public Element setToZero() {
        this.value = field.getInstance().encodeZeroAt(index);

        return this;
    }

    public boolean isZero() {
        return field.getInstance().isZero(value, index);
    }

    public Element setToOne() {
        if (index > 0)
            setToZero();
        else
            this.value = field.getInstance().encodeOneAt(index);

        return this;
    }

    public boolean isEqual(Element value) {
        return duplicate().sub(value).isZero();
    }

    public boolean isOne() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element twice() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element square() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element invert() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element halve() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element negate() {
        this.value = field.getInstance().reduce(this.value.negate());

        return this;
    }

    public Element add(Element element) {
        CTL13MMElement e = ((CTL13MMElement) element);

        BigInteger b = e.value;
        if (e.index > index) {
            this.value = field.getInstance().encodeAt(value, index, e.index);
            this.index = e.index;
            this.field = (CTL13MMField) field.getPairing().getFieldAt(index);
        } else if (e.index < index) {
            b = field.getInstance().encodeAt(b, e.index, index);
        }

        this.value = field.getInstance().reduce(this.value.add(b));

        return this;
    }

    public Element sub(Element element) {
        CTL13MMElement e = ((CTL13MMElement) element);

        BigInteger b = e.value;
        if (index < e.index) {
            // upgrade to level e.index
            this.value = field.getInstance().encodeAt(value, index, e.index);
            this.index = e.index;
            this.field = (CTL13MMField) field.getPairing().getFieldAt(index);
        } else if (e.index < index) {
            b = field.getInstance().encodeAt(b, e.index, index);
        }

        this.value = field.getInstance().reduce(this.value.subtract(b));

        return this;
    }

    public Element mul(Element element) {
        if (index == 0 && ((CTL13MMElement) element).index == 0) {
            this.value = field.getInstance().reduce(this.value.multiply(((CTL13MMElement) element).value));

            return this;
        }

        return add(element);
    }

    public Element mul(int z) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element mul(BigInteger n) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element mulZn(Element z) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element div(Element element) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element pow(BigInteger n) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element powZn(Element n) {
        if (((CTL13MMElement) n).index != 0)
            throw new IllegalArgumentException("Level must be zero!");

        this.value = field.getInstance().reduce(value.multiply(((CTL13MMElement) n).value));

        if (index > 0)
            this.value = field.getInstance().reRandomize(value, index);

        return this;
    }

    public ElementPowPreProcessing getElementPowPreProcessing() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public Element sqrt() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public boolean isSqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int sign() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    @Override
    public String toString() {
        return String.format("{%s,%d}", value.toString(), index);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CTL13MMElement && isEqual((Element) obj);
    }
}
