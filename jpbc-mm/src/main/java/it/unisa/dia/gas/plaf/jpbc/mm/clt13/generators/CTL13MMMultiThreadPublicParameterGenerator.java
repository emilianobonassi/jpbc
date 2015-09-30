package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerAddAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerMulAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.context.ContextExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.context.ContextRunnable;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils.IndexCallable;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class CTL13MMMultiThreadPublicParameterGenerator extends CTL13MMPublicParameterGenerator {


    public CTL13MMMultiThreadPublicParameterGenerator(SecureRandom random, CTL13MMSystemParameters parameters) {
        super(random, parameters);
    }

    public CTL13MMMultiThreadPublicParameterGenerator(SecureRandom random, PairingParameters parameters) {
        super(random, parameters);
    }

    public CTL13MMMultiThreadPublicParameterGenerator(SecureRandom random, CTL13MMSystemParameters parameters, boolean storeGeneratedInstance) {
        super(random, parameters, storeGeneratedInstance);
    }

    public CTL13MMMultiThreadPublicParameterGenerator(SecureRandom random, PairingParameters parameters, boolean storeGeneratedInstance) {
        super(random, parameters, storeGeneratedInstance);
    }


    protected void generateInternal(MutablePairingParameters mapParameters) {
        ContextExecutor executor = new ContextExecutor(mapParameters);
        executor.submit(new ContextRunnable("x0+ps") {
            public void run() {
                // Generate CRT modulo x0
                Accumulator<BigInteger> x0 = new BigIntegerMulAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    x0.accumulate(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            BigInteger value = BigInteger.probablePrime(parameters.getEta(), random);
                            putBigIntegerAt("ps", i, value);

                            return value;
                        }

                    });
                }
                x0.awaitTermination();

                putBoolean("ps", true);
                putBigInteger("x0", x0.getResult());
            }

        }).submit(new ContextRunnable("gs") {
            public void run() {
                // Generate g_i's
                Pool executor = new PoolExecutor();
                for (int i = 0; i < parameters.getN(); i++) {
                    executor.submit(new ExecutorServiceUtils.IndexRunnable(i) {
                        public void run() {
                            putBigIntegerAt("gs", i, BigInteger.probablePrime(parameters.getAlpha(), random));
                        }
                    });
                }
                executor.awaitTermination();

                putBoolean("gs", true);
            }
        }).submit(new ContextRunnable("crtCoefficients") {
            public void run() {
                // Generate CRT Coefficients
                final BigInteger x0 = getBigInteger("x0");
                getObject("ps");

                Pool executor = new PoolExecutor();
                for (int i = 0; i < parameters.getN(); i++) {
                    executor.submit(new ExecutorServiceUtils.IndexRunnable(i) {
                        public void run() {
                            BigInteger temp = x0.divide(getBigIntegerAt("ps", i));
                            temp = temp.modInverse(getBigIntegerAt("ps", i)).multiply(temp);

                            putBigIntegerAt("crtCoefficients", i, temp);
                        }
                    });
                }
                executor.awaitTermination();

                putBoolean("crtCoefficients", true);
            }
        }).submit(new ContextRunnable("z+zInv") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");

                // Generate z
                BigInteger z, zInv;
                do {
                    z = getRandom(x0, random);
                    zInv = z.modInverse(x0);
                } while (zInv.equals(BigInteger.ZERO));

                putBigInteger("z", z);
                putBigInteger("zInv", zInv);

                BigInteger temp = BigInteger.ONE;
                for (int i = 0; i < parameters.getKappa(); i++) {
                    temp = temp.multiply(zInv).mod(x0);
                    putBigIntegerAt("zInvPow", i, temp);
                }
            }
        }).submit(new ContextRunnable("xsp") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                getObject("crtCoefficients");
                getObject("gs");

                // Generate xp_i's
                for (int i = 0; i < parameters.getEll(); i++) {

                    Accumulator<BigInteger> xsp = new BigIntegerAddAccumulator();
                    for (int j = 0; j < parameters.getN(); j++) {
                        xsp.accumulate(new IndexCallable<BigInteger>(i) {
                            public BigInteger call() throws Exception {
                                return getBigIntegerAt("gs", i)
                                        .multiply(getRandom(parameters.getRho(), random))
                                        .add(getRandom(parameters.getAlpha(), random))
                                        .multiply(getBigIntegerAt("crtCoefficients", i));
                            }

                        });
                    }
                    putBigIntegerAt("xsp", i, xsp.awaitResult().mod(x0));

                }

                putBoolean("xsp", true);
            }
        }).submit(new ContextRunnable("y") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                BigInteger zInv = getBigInteger("zInv");
                getObject("crtCoefficients");
                getObject("gs");

                // Generate y = encodeOneAt(1)
                Accumulator<BigInteger> y = new BigIntegerAddAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    y.accumulate(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return getBigIntegerAt("gs", i).multiply(getRandom(parameters.getRho(), random))
                                    .add(BigInteger.ONE)
                                    .multiply(getBigIntegerAt("crtCoefficients", i));
                        }
                    });
                }

                BigInteger finalY = y.awaitResult().multiply(zInv).mod(x0);
                putBigInteger("y", finalY);

                BigInteger yPow = BigInteger.ONE;
                putBigIntegerAt("yPow", 0, yPow);
                for (int i = 1; i <= parameters.getKappa(); i++) {
                    yPow = yPow.multiply(finalY).mod(x0);
                    putBigIntegerAt("yPow", i, yPow);
                }
            }
        }).submit(new ContextRunnable("pzt") {
            public void run() {
                final BigInteger x0 = getBigInteger("x0");
                BigInteger z = getBigInteger("z");
                getObject("gs");
                getObject("ps");

                // Generate zero-tester pzt
                final BigInteger zPowKappa = z.modPow(BigInteger.valueOf(parameters.getKappa()), x0);

                Accumulator<BigInteger> pzt = new BigIntegerAddAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    pzt.accumulate(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return getRandom(parameters.getBeta(), random)
                                    .multiply(getBigIntegerAt("gs", i).modInverse(getBigIntegerAt("ps", i)).multiply(zPowKappa).mod(getBigIntegerAt("ps", i)))
                                    .multiply(x0.divide(getBigIntegerAt("ps", i)));
                        }
                    });
                }

                putBigInteger("pzt", pzt.awaitResult().mod(x0));
            }
        }).submit(new ContextRunnable("xs") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                getObject("crtCoefficients");
                getObject("gs");

                for (int level = 1; level <= parameters.getKappa(); level++) {
                    String xsLevel = "xs" + level;

                    // Quadratic re-randomization stuff
                    for (int i = 0; i < parameters.getDelta(); i++) {
                        // xs[i] = encodeZero();
                        Accumulator<BigInteger> xs = new BigIntegerAddAccumulator();
                        for (int j = 0; j < parameters.getN(); j++) {
                            xs.accumulate(new IndexCallable<BigInteger>(j) {
                                public BigInteger call() throws Exception {
                                    return getBigIntegerAt("gs", i).multiply(getRandom(parameters.getRho(), random))
                                            .multiply(getBigIntegerAt("crtCoefficients", i));
                                }
                            });
                        }
                        putBigIntegerAt(xsLevel, i, xs.awaitResult().mod(x0));

                        // xs[parameters.getDelta() + i] = encodeAt(1);
                        xs = new BigIntegerAddAccumulator();
                        for (int j = 0; j < parameters.getN(); j++) {
                            xs.accumulate(new IndexCallable<BigInteger>(j) {
                                public BigInteger call() throws Exception {
                                    return getBigIntegerAt("gs", i).multiply(getRandom(parameters.getRho(), random))
                                            .add(getRandom(parameters.getAlpha(), random))
                                            .multiply(getBigIntegerAt("crtCoefficients", i));
                                }
                            });
                        }
                        putBigIntegerAt(
                                xsLevel,
                                parameters.getDelta() + i,
                                xs.awaitResult().multiply(getBigIntegerAt("zInvPow", level - 1)).mod(x0)
                        );
                    }

                }

                putBoolean("xs", true);
            }
        });

        long start = System.currentTimeMillis();
        executor.awaitTermination();
        long end = System.currentTimeMillis();
        System.out.println("end = " + (end - start));
    }


    public static void main(String[] args) {
        String params = "./params/mm/ctl13/toy.properties";
        if (args.length > 0)
            params = args[0];

        CTL13MMMultiThreadPublicParameterGenerator gen = new CTL13MMMultiThreadPublicParameterGenerator(
                new SecureRandom(),
                PairingFactory.getInstance().loadParameters(params)
        );
        gen.generate();
    }

}
