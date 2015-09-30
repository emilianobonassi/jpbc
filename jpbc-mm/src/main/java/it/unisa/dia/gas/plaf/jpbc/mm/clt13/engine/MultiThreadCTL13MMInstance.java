package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerAddAccumulator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.Callable;

import static it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils.IndexCallable;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.modNear;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class MultiThreadCTL13MMInstance implements CTL13MMInstance {

    protected SecureRandom random;
    protected CTL13MMPublicParameters values;
    protected CTL13MMSystemParameters parameters;

    protected BigInteger x0;
    protected BigInteger pzt;

    protected long isZeroBound;

    public MultiThreadCTL13MMInstance(SecureRandom random, PairingParameters parameters) {
        this.random = random;
        this.values = new CTL13MMPublicParameters(parameters);

        this.parameters = values.getSystemParameters();
        this.x0 = values.getX0();
        this.pzt = values.getPzt();

        this.isZeroBound = (x0.bitLength() - this.parameters.getBound());
    }


    public CTL13MMSystemParameters getSystemParameters() {
        return parameters;
    }


    public BigInteger reduce(BigInteger value) {
        return value.mod(x0);
    }

    public boolean isZero(BigInteger value, int index) {
        value = modNear(value.multiply(pzt).multiply(values.getYPowAt(parameters.getKappa() - index)), x0);

        return (value.bitLength() < isZeroBound);
    }


    public BigInteger sampleAtLevel(int index) {
        return encodeAt(sampleAtZero(), 0, index);
    }

    public BigInteger sampleAtZero() {
        BigInteger c = BigInteger.ZERO;

        for (int i = 0; i < parameters.getEll(); i++)
            if (random.nextBoolean())
                c = c.add(values.getXspAt(i));

        return c.mod(x0);

    }


    public BigInteger encodeAt(BigInteger value, int startIndex, int endIndex) {
        return modNear(value.multiply(values.getYPowAt(endIndex - startIndex)), x0);
    }

    public BigInteger encodeAt(int degree) {
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getN(); i++) {
            accumulator.accumulate(new IndexCallable<BigInteger>(i) {
                public BigInteger call() throws Exception {
                    return values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .add(getRandom(parameters.getAlpha(), random))
                            .multiply(values.getCrtCoefficientAt(i));
                }
            });
        }

        BigInteger res = accumulator.awaitResult().mod(x0);
        if (degree > 0)
            res = res.multiply(values.getZInvPowAt(degree - 1)).mod(x0);

        return res;
    }

    public BigInteger encodeZero() {
        return encodeZeroAt(0);
    }

    public BigInteger encodeZeroAt(int index) {
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getN(); i++) {
            accumulator.accumulate(new IndexCallable<BigInteger>(i) {
                public BigInteger call() throws Exception {
                    return values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .multiply(values.getCrtCoefficientAt(i));
                }
            });
        }

        BigInteger res = accumulator.awaitResult().mod(x0);
        if (index > 0)
            res = res.multiply(values.getZInvPowAt(index - 1)).mod(x0);

        return res;
    }

    public BigInteger encodeOne() {
        return encodeOneAt(0);
    }

    public BigInteger encodeOneAt(int index) {
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getN(); i++) {
            accumulator.accumulate(new IndexCallable<BigInteger>(i) {
                public BigInteger call() throws Exception {
                    return values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .add(BigInteger.ONE)
                            .multiply(values.getCrtCoefficientAt(i));
                }
            });
        }

        BigInteger res = accumulator.awaitResult().mod(x0);
        if (index > 0)
            res = res.multiply(values.getZInvPowAt(index - 1)).mod(x0);

        return res;
    }

    public BigInteger reRandomize(BigInteger value, final int index) {
        // Re-randomize.
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getTheta(); i++) {
            accumulator.accumulate(new Callable<BigInteger>() {
                public BigInteger call() throws Exception {
                    // TODO : Ensure no duplicates are used.
                    int pos = random.nextInt(parameters.getDeltaSquare());
                    return values.getXsAt(index, pos % parameters.getDelta())
                            .multiply(values.getXsAt(index, parameters.getDelta() + pos / parameters.getDelta()));
                }
            });
        }
        return accumulator.awaitResult().add(value).mod(x0);
    }


    public BigInteger extract(BigInteger value, int index) {
        value = modNear(value.multiply(pzt).multiply(values.getYPowAt(parameters.getKappa() - index)), x0);
//
        return value.shiftRight(
                x0.bitLength() - parameters.getBound()
        );
    }

}
