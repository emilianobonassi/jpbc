package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.DefaultCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.MultiThreadCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMMultiThreadPublicParameterGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMPublicParameterGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulatorFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.DefaultPairingPreProcessing;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMPairing implements Pairing {

    protected SecureRandom random;
    protected CTL13MMInstance instance;
    protected CTL13MMField[] fields;


    public CTL13MMPairing(SecureRandom random, CTL13MMInstance instance) {
        this.random = random;
        this.instance = instance;
        this.fields = new CTL13MMField[instance.getSystemParameters().getKappa() + 1];
    }


    public CTL13MMPairing(SecureRandom random, PairingParameters parameters) {
        this.random = random;
        this.instance = initInstance(parameters);
        this.fields = new CTL13MMField[instance.getSystemParameters().getKappa() + 1];
    }


    public boolean isSymmetric() {
        return true;
    }

    public Field getG1() {
        return getFieldAt(1);
    }

    public Field getG2() {
        return getFieldAt(1);
    }

    public Field getGT() {
        return getFieldAt(2);
    }

    public Field getZr() {
        return getFieldAt(0);
    }

    public int getDegree() {
        return instance.getSystemParameters().getKappa();
    }

    public Field getFieldAt(int index) {
        if (fields[index] == null)
            fields[index] = new CTL13MMField(this, index);

        return fields[index];
    }

    public Element pairing(Element in1, Element in2) {
        CTL13MMElement a = (CTL13MMElement) in1;
        CTL13MMElement b = (CTL13MMElement) in2;

        int index = a.index + b.index;
        BigInteger value = instance.reduce(a.value.multiply(b.value));

        return new CTL13MMElement((CTL13MMField) getFieldAt(index), value, index);
    }

    public boolean isProductPairingSupported() {
        return false;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        if (in1.length != in2.length)
            throw new IllegalArgumentException("Array lengths mismatch.");

        PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(this);
        for(int i = 0; i < in1.length; i++)
            combiner.addPairing(in1[i], in2[i]);
        return combiner.awaitResult();
    }

    public PairingPreProcessing getPairingPreProcessingFromElement(Element in1) {
        return new DefaultPairingPreProcessing(this, in1);
    }

    public int getPairingPreProcessingLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PairingPreProcessing getPairingPreProcessingFromBytes(byte[] source) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PairingPreProcessing getPairingPreProcessingFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getFieldIndex(Field field) {
        for (int i = 0; i < fields.length; i++)
            if (fields[i] == field)
                return i;

        return -1;
    }

    public CTL13MMInstance getCTL13MMInstance() {
        return instance;
    }


    protected CTL13MMInstance initInstance(PairingParameters parameters) {
        String instanceType = parameters.getString("instanceType", "mt");

        if ("mt".equals(instanceType))
            return new MultiThreadCTL13MMInstance(random,
                    new CTL13MMMultiThreadPublicParameterGenerator(
                            random,
                            parameters
                    ).generate()
            );

        return new DefaultCTL13MMInstance(random,
                new CTL13MMPublicParameterGenerator(
                        random,
                        parameters
                ).generate()
        );
    }

}
