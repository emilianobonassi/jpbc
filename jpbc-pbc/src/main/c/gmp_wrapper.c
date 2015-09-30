#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <assert.h>
#include <stdio.h>
#include <math.h>

#include <gmp.h>


int gmp_mpz_sizeof() {
    return sizeof(mpz_t);
}

int gmp_mpz_sign(mpz_ptr value) {
    return mpz_sgn(value);
}


void gmp_set_ui(mpz_ptr out, int value, mpz_ptr order) {
    mpz_set_ui(out, value);
    mpz_mod(out, out, order);
}

void gmp_twice(mpz_ptr out, mpz_ptr order) {
    mpz_mul_2exp(out, out, 1);
    if (mpz_cmp(out, order) >= 0) {
        mpz_sub(out, out, order);
    }

}

void gmp_add(mpz_ptr out, mpz_ptr in, mpz_ptr order) {
    mpz_add(out, out, in);

    if (mpz_cmp(out, order) >= 0) {
        mpz_sub(out, out, order);
    }
}

void gmp_sub(mpz_ptr out, mpz_ptr in, mpz_ptr order) {
    mpz_sub(out, out, in);
    if (mpz_sgn(out) < 0) {
        mpz_add(out, out, order);
    }
}

void gmp_mul(mpz_ptr out, mpz_ptr in, mpz_ptr order) {
    mpz_mul(out, out, in);
    mpz_mod(out, out, order);
}

void gmp_mul_ui(mpz_ptr out, int in, mpz_ptr order) {
    mpz_mul_ui(out, out, in);
    mpz_mod(out, out, order);
}

void gmp_negate(mpz_ptr value, mpz_ptr order) {
  if (mpz_cmp_ui(value, 0) == 0) {
    mpz_set_ui(value, 0);
  } else {
    mpz_sub(value, order, value);
  }
}

void gmp_halve(mpz_ptr value, mpz_ptr order) {
  if (mpz_odd_p(value)) {
    mpz_add(value, value, order);
    mpz_tdiv_q_2exp(value, value, 1);
  } else {
    mpz_tdiv_q_2exp(value, value, 1);
  }

}

//int gmp_is_sqr(mpz_ptr value) {
//    if (mpz_cmp_ui(value, 0) == 0)
//        return true;
//    return mpz_legendre(value, order) == 1;
//}
