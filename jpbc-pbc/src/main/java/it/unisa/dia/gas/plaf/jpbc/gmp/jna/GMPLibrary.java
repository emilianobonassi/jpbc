package it.unisa.dia.gas.plaf.jpbc.gmp.jna;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface GMPLibrary extends Library {

    void __gmpz_init(Pointer op);

    void __gmpz_set_str(Pointer op, String str, int base);

    String __gmpz_get_str(String str, int base, Pointer op);

    void __gmpz_clear(Pointer op);

    void __gmpz_powm_ui(Pointer out, Pointer in, int pow, Pointer order);

    void __gmpz_set_si(Pointer out, int in);

    void __gmpz_mod(Pointer out, Pointer in, Pointer order);

    void __gmpz_set(Pointer out, Pointer in);

    void __gmpz_add(Pointer out, Pointer a, Pointer b);

    int __gmpz_cmp(Pointer out, Pointer order);

    void __gmpz_sub(Pointer out, Pointer a, Pointer b);

    void __gmpz_mul(Pointer out, Pointer a, Pointer b);

    void __gmpz_mul_si(Pointer out, Pointer a, int b);

    int __gmpz_legendre(Pointer value, Pointer order);

    int __gmpz_cmp_ui(Pointer in, int value);

    void __gmpz_invert(Pointer out, Pointer in, Pointer order);

    void __gmpz_powm(Pointer out, Pointer base, Pointer exp, Pointer order);

    void __gmpz_mul_2exp(Pointer out, Pointer in, int exp);

    void __gmpz_set_ui(Pointer out, int value);

    int __gmpz_odd_p(Pointer value);

    void __gmpz_tdiv_q_2exp(Pointer out, Pointer in, int exp);

    int __gmpz_sgn(Pointer value);
}
