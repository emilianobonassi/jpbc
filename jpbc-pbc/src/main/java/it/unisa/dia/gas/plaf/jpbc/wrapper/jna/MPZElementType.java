package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;
import it.unisa.dia.gas.plaf.jpbc.gmp.jna.GMPProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MPZElementType extends Memory {

    public static MPZElementType fromBigInteger(BigInteger bigInteger) {
        MPZElementType element = new MPZElementType();
        element.init();
        element.setFromString(bigInteger.toString(), 10);

        return element;
    }


    public MPZElementType() {
        super(WrapperLibraryProvider.getWrapperLibrary().gmp_mpz_sizeof());
    }


    public void init() {
        GMPProvider.getGmpLibrary().__gmpz_init(this);
    }

    public String toString(int base) {
        return GMPProvider.getGmpLibrary().__gmpz_get_str(null, base, this);
    }

    public BigInteger toBigInteger() {
        return new BigInteger(GMPProvider.getGmpLibrary().__gmpz_get_str(null, 10, this));
    }

    public void setFromString(String s, int base) {
        GMPProvider.getGmpLibrary().__gmpz_set_str(this, s, base);
    }

    public MPZElementType duplicate() {
        MPZElementType copy = new MPZElementType();
        copy.init();

        GMPProvider.getGmpLibrary().__gmpz_set(copy, this);

        return copy;
    }

    @Override
    public String toString() {
        return toString(10);
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            GMPProvider.getGmpLibrary().__gmpz_clear(this);
            super.finalize();
        }
    }

}
