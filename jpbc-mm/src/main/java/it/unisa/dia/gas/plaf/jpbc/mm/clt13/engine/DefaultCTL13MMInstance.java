package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.modNear;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class DefaultCTL13MMInstance implements CTL13MMInstance {

    protected SecureRandom random;
    protected CTL13MMPublicParameters publicParameters;
    protected CTL13MMSystemParameters systemParameters;

    protected BigInteger x0;
    protected BigInteger y;       // level-one random encoding of 1
    protected BigInteger pzt;     // zero-tester
    protected BigInteger zInv;

    protected long isZeroBound;


    public DefaultCTL13MMInstance(SecureRandom random, PairingParameters pairingParameters) {
        this.random = random;
        this.publicParameters = new CTL13MMPublicParameters(pairingParameters);

        this.systemParameters = publicParameters.getSystemParameters();
        this.x0 = publicParameters.getX0();
        this.y = publicParameters.getY();
        this.zInv = publicParameters.getZInv();
        this.pzt = publicParameters.getPzt();

        this.isZeroBound = (x0.bitLength() - this.systemParameters.getBound());
    }


    public CTL13MMSystemParameters getSystemParameters() {
        return systemParameters;
    }


    public BigInteger reduce(BigInteger value) {
        return value.mod(x0);
    }

    public boolean isZero(BigInteger value, int index) {
        value = modNear(value.multiply(pzt), x0);
        for (long i = systemParameters.getKappa() - index; i > 0; i--)
            value = modNear(value.multiply(y), x0);

        return (value.bitLength() < isZeroBound);
    }


    public BigInteger sampleAtLevel(int index) {
        return encodeAt(sampleAtZero(), 0, index);
    }

    public BigInteger sampleAtZero() {
        BigInteger c = BigInteger.ZERO;

        for (int i = 0; i < systemParameters.getEll(); i++)
            if (random.nextBoolean())
                c = c.add(publicParameters.getXspAt(i));

        return c.mod(x0);

    }


    public BigInteger encodeAt(BigInteger value, int startIndex, int endIndex) {
        for (int i = endIndex; i > startIndex; i--)
            value = modNear(value.multiply(y), x0);
        return value;
    }

    public BigInteger encodeAt(int degree) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < systemParameters.getN(); i++) {
            res = res.add(
                    publicParameters.getGsAt(i).multiply(getRandom(systemParameters.getRho(), random)).add(getRandom(systemParameters.getAlpha(), random))
                            .multiply(publicParameters.getCrtCoefficientAt(i))
            );
        }

        res = res.mod(x0);
        for (int j = degree; j > 0; j--)
            res = res.multiply(zInv).mod(x0);

        return res;
    }

    public BigInteger encodeZero() {
        return encodeZeroAt(0);
    }

    public BigInteger encodeZeroAt(int index) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < systemParameters.getN(); i++)
            res = res.add(
                    publicParameters.getGsAt(i).multiply(getRandom(systemParameters.getRho(), random))
                            .multiply(publicParameters.getCrtCoefficientAt(i))
            );

        res = res.mod(x0);
        for (int j = index; j > 0; j--)
            res = res.multiply(zInv).mod(x0);

        return res;
    }

    public BigInteger encodeOne() {
        return encodeOneAt(0);
    }

    public BigInteger encodeOneAt(int index) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < systemParameters.getN(); i++) {
            res = res.add(
                    publicParameters.getGsAt(i).multiply(getRandom(systemParameters.getRho(), random))
                            .add(BigInteger.ONE)
                            .multiply(publicParameters.getCrtCoefficientAt(i))
            );
        }

        res = res.mod(x0);
        for (int j = index; j > 0; j--)
            res = res.multiply(zInv).mod(x0);

        return res;
    }

    public BigInteger reRandomize(BigInteger value, int index) {
        for (int i = 0; i < systemParameters.getTheta(); i++) {
            // TODO : Ensure no duplicates are used.
            int pos = random.nextInt(systemParameters.getDeltaSquare());
            value = value.add(
                    publicParameters.getXsAt(index, pos % systemParameters.getDelta())
                            .multiply(publicParameters.getXsAt(index, systemParameters.getDelta() + pos / systemParameters.getDelta()))
            );
        }

        return value.mod(x0);
    }


    public BigInteger extract(BigInteger value, int index) {
        value = modNear(value.multiply(pzt), x0);
        for (long i = systemParameters.getKappa() - index; i > 0; i--)
            value = modNear(value.multiply(y), x0);

        return value.shiftRight(
                x0.bitLength()- systemParameters.getBound()
        );
    }


}
