#include "pbc_wrapper.h"

#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <assert.h>
#include <stdio.h>
#include <math.h>

#include <gmp.h>

#include <pbc/pbc.h>
#include <pbc/pbc_field.h>
#include <pbc/pbc_pairing.h>
#include <pbc/pbc_utils.h>

// ==============
// sizeof methods
// ==============


int pbc_pairing_sizeof() {
    return sizeof(pairing_t);
}

int pbc_element_sizeof() {
    return sizeof(element_t);
}

int pbc_pairing_pp_sizeof() {
    return sizeof(pairing_pp_t);
}

int pbc_element_pp_sizeof() {
    return sizeof(element_pp_t);
}


// ========================
// curve generation methods
// ========================


int pbc_curvegen_a(const char *fileName, int rbits, int qbits) {
      pbc_param_t par;
      pbc_param_init_a_gen(par, rbits, qbits);
      FILE *pFile = fopen(fileName, "w+");
      pbc_param_out_str(pFile, par);
      pbc_param_clear(par);
      fclose (pFile);

      return 1;
}

int pbc_curvegen_a1(const char *fileName) {
    mpz_t p, q, N;

    mpz_init(p);
    mpz_init(q);
    mpz_init(N);

    // In a real application, p and q must be stored somewhere safe.
    pbc_mpz_randomb(p, 512);
    pbc_mpz_randomb(q, 512);

    mpz_nextprime(p, p);
    mpz_nextprime(q, q);
    mpz_mul(N, p, q);

    pbc_param_t param;
    pbc_param_init_a1_gen(param, N);
    FILE *pFile = fopen(fileName, "w+");
    pbc_param_out_str(pFile, param);
    pbc_param_clear(param);
    fclose (pFile);

    mpz_clear(p);
    mpz_clear(q);
    mpz_clear(N);
    
    return 1;
}


const char* pbc_curvegen_d_file_name;

int pbc_curvegen_d_generate(pbc_cm_t cm, void *data) {
    UNUSED_VAR(data);
    pbc_param_t param;
    pbc_info("gendparam: computing Hilbert polynomial and finding roots...");
    pbc_param_init_d_gen(param, cm);
    pbc_info("gendparam: bits in q = %zu\n", mpz_sizeinbase(cm->q, 2));
    FILE *pFile = fopen(pbc_curvegen_d_file_name, "w+");
    pbc_param_out_str(pFile, param);
    pbc_param_clear(param);
    fclose (pFile);
    return 1;
}

int pbc_curvegen_d(const char *fileName, int discriminant) {
    pbc_curvegen_d_file_name = fileName;
    if (!pbc_cm_search_d(pbc_curvegen_d_generate, NULL, discriminant, 500))
        return 0;

    return 1;
}

int pbc_curvegen_e(const char *fileName, int rbits, int qbits) {
      pbc_param_t par;
      pbc_param_init_e_gen(par, rbits, qbits);
      FILE *pFile = fopen(fileName, "w+");
      pbc_param_out_str(pFile, par);
      pbc_param_clear(par);
      fclose (pFile);

      return 1;
}

int pbc_curvegen_f(const char *fileName, int rbits) {
      pbc_param_t par;
      pbc_param_init_f_gen(par, rbits);
      FILE *pFile = fopen(fileName, "w+");
      pbc_param_out_str(pFile, par);
      pbc_param_clear(par);
      fclose (pFile);

      return 1;
}


const char* pbc_curvegen_g_file_name;

int pbc_curvegen_g_generate(pbc_cm_t cm, void *data) {
    UNUSED_VAR(data);
    pbc_param_t param;
    pbc_info("gendparam: computing Hilbert polynomial and finding roots...");
    pbc_param_init_g_gen(param, cm);
    pbc_info("gendparam: bits in q = %zu\n", mpz_sizeinbase(cm->q, 2));
    FILE *pFile = fopen(pbc_curvegen_g_file_name, "w+");
    pbc_param_out_str(pFile, param);
    pbc_param_clear(param);
    fclose (pFile);
    return 1;
}

int pbc_curvegen_g(const char *fileName, int discriminant) {
    pbc_curvegen_g_file_name = fileName;
    if (!pbc_cm_search_g(pbc_curvegen_g_generate, NULL, discriminant, 500))
        return 0;

    return 1;
}

// ===============
// pairing methods
// ===============

void pbc_pairing_init_inp_buf(pairing_t pairing, const char *buf, size_t len) {
    pairing_init_set_buf (pairing, buf, len);
    //pairing_init_inp_buf (pairing, buf, len);
}

#ifdef __PBC_PAIRING_PP_IO__

    int pbc_is_pairing_pp_io_flag() {
        return 1;
    }

    int pbc_is_pairing_pp_io_available(pairing_t pairing) {
        return pairing->pp_io_available();
    }

    int pbc_pairing_pp_length_in_bytes(pairing_t p) {
        return pairing_pp_length_in_bytes(p);
    }

    int pbc_pairing_pp_init_from_bytes(pairing_pp_t p, pairing_t pairing, unsigned char *data, int offset) {
        return pairing_pp_init_from_bytes(p, data + offset, pairing);
    }

    void pbc_pairing_pp_to_bytes(unsigned char *data, pairing_pp_t p) {
        pairing_pp_to_bytes(data, p);
    }

#else

    int pbc_is_pairing_pp_io_flag() {
        return 0;
    }

    int pbc_is_pairing_pp_io_available(pairing_t pairing) {
        return 0;
    }

#endif

void pbc_pairing_pp_init(pairing_pp_t p, element_t in1, pairing_t pairing) {
    pairing_pp_init(p, in1, pairing);
}

void pbc_pairing_pp_clear(pairing_pp_t p) {
    pairing_pp_clear(p);
}

void pbc_pairing_pp_apply(element_t out, element_t in2, pairing_pp_t p) {
    pairing_pp_apply(out, in2, p);
}

void pbc_pairing_apply(element_t out, element_t in1, element_t in2, pairing_t pairing) {
    pairing_apply(out, in1, in2, pairing);
}

void pbc_pairing_prod(element_t out, element_ptr in1[], element_ptr in2[], int n) {
    element_t in1a[n];
    element_t in2a[n];

    int i = 0;
    for (i = 0; i < n; i++) {
        in1a[i][0] = *in1[i];
        in2a[i][0] = *in2[i];
    }

    element_prod_pairing(out, in1a, in2a, n);
}

int pbc_pairing_is_symmetric(pairing_t pairing) {
    return pairing_is_symmetric(pairing);
}

int pbc_pairing_length_in_bytes_G1(pairing_t pairing) {
    return pairing_length_in_bytes_G1(pairing);
}

int pbc_pairing_length_in_bytes_x_only_G1(pairing_t pairing) {
    return pairing_length_in_bytes_x_only_G1(pairing);
}

int pbc_pairing_length_in_bytes_compressed_G1(pairing_t pairing) {
    return pairing_length_in_bytes_compressed_G1(pairing);
}

int pbc_pairing_length_in_bytes_G2(pairing_t pairing) {
    return pairing_length_in_bytes_G2(pairing);
}

int pbc_pairing_length_in_bytes_compressed_G2(pairing_t pairing) {
    return pairing_length_in_bytes_compressed_G2(pairing);
}

int pbc_pairing_length_in_bytes_x_only_G2(pairing_t pairing) {
    return pairing_length_in_bytes_x_only_G2(pairing);
}

int pbc_pairing_length_in_bytes_GT(pairing_t pairing) {
    return pairing_length_in_bytes_GT(pairing);
}

int pbc_pairing_length_in_bytes_Zr(pairing_t pairing) {
    return pairing_length_in_bytes_Zr(pairing);
}

void pbc_pairing_clear(pairing_t pairing) {
    pairing_clear(pairing);
}

int pbc_is_almost_coddh(element_t a, element_t b, element_t c, element_t d, pairing_t pairing) {
    return is_almost_coddh(a, b, c, d, pairing);
}


// ===============
// element methods
// ===============


void pbc_element_init_G1(element_t element, pairing_t pairing) {
    element_init_G1(element, pairing);
}

void pbc_element_init_G2(element_t element, pairing_t pairing) {
    element_init_G2(element, pairing);
}

void pbc_element_init_GT(element_t element, pairing_t pairing) {
    element_init_GT(element, pairing);
}

void pbc_element_init_Zr(element_t element, pairing_t pairing) {
    element_init_Zr(element, pairing);
}

void pbc_element_init_same_as(element_t e, element_t e2) {
    element_init_same_as(e, e2);
}

int pbc_element_snprint(char *s, size_t n, element_t e) {
    return element_snprint(s, n, e);
}

int pbc_element_set_str(element_t e, char *s, int base) {
    return element_set_str(e, s, base);
}

void pbc_element_set0(element_t e) {
    element_set0(e);
}

void pbc_element_set1(element_t e) {
    element_set1(e);
}

void pbc_element_set_si(element_t e, signed long int i) {
    element_set_si(e, i);
}

void pbc_element_set_mpz(element_t e, mpz_t z) {
    element_set_mpz(e, z);
}

void pbc_element_set(element_t e, element_t a) {
    element_set(e, a);
}

void pbc_element_add_ui(element_t n, element_t a, unsigned long int b) {
    element_add_ui(n, a, b);
}

void pbc_element_to_mpz(mpz_t z, element_t e) {
    element_to_mpz(z, e);
}

void pbc_element_from_hash(element_t e, void *data, int len) {
    element_from_hash(e, data, len);
}

void pbc_element_add(element_t n, element_t a, element_t b) {
    element_add(n, a, b);
}

void pbc_element_sub(element_t n, element_t a, element_t b) {
    element_sub(n, a, b);
}

void pbc_element_mul(element_t n, element_t a, element_t b) {
    element_mul(n, a, b);
}

void pbc_element_mul_mpz(element_t n, element_t a, mpz_t z) {
    element_mul_mpz(n, a, z);
}

void pbc_element_mul_si(element_t n, element_t a, signed long int z) {
    element_mul_si(n, a, z);
}

void pbc_element_mul_zn(element_t c, element_t a, element_t z) {
    element_mul_zn(c, a, z);
}

void pbc_element_div(element_t n, element_t a, element_t b) {
    element_div(n, a, b);
}

void pbc_element_double(element_t n, element_t a) {
    element_double(n, a);
}

void pbc_element_halve(element_t n, element_t a) {
    element_halve(n, a);
}

void pbc_element_square(element_t n, element_t a) {
    element_square(n, a);
}

void pbc_element_pow_mpz(element_t x, element_t a, mpz_t n) {
    element_pow_mpz(x, a, n);
}

void pbc_element_pow_zn(element_t x, element_t a, element_t n) {
    element_pow_zn(x, a, n);
}

void pbc_element_neg(element_t n, element_t a) {
    element_neg(n, a);
}

void pbc_element_invert(element_t n, element_t a) {
    element_invert(n, a);
}

void pbc_element_random(element_t e) {
    element_random(e);
}

int pbc_element_is1(element_t n) {
    return element_is1(n);
}

int pbc_element_is0(element_t n) {
    return element_is0(n);
}

int pbc_element_cmp(element_t a, element_t b) {
    return element_cmp(a, b);
}

int pbc_element_is_sqr(element_t a) {
    return element_is_sqr(a);
}

int pbc_element_sgn(element_t a) {
    return element_sgn(a);
}

int pbc_element_sign(element_t a) {
    return element_sign(a);
}

void pbc_element_sqrt(element_t a, element_t b) {
    element_sqrt(a, b);
}

int pbc_element_length_in_bytes(element_t e) {
    return element_length_in_bytes(e);
}

int pbc_element_to_bytes(unsigned char *data, element_t e) {
    return element_to_bytes(data, e);
}

int pbc_element_from_bytes(element_t e, unsigned char *data) {
    return element_from_bytes(e, data);
}

void pbc_element_clear(element_t element) {
    element_clear(element);
}

void pbc_element_multi_double(element_ptr n[], element_ptr a[], int m) {
    element_t in1a[m];
    element_t in2a[m];

    int i;
    for (i = 0; i < m; i++) {
        in1a[i][0] = *n[i];
        in2a[i][0] = *a[i];
    }

    element_multi_double(in1a, in2a, m);
}

void pbc_element_multi_double2(element_ptr a[], int m) {
    element_t in1a[m];

    int i;
    for (i = 0; i < m; i++) {
        in1a[i][0] = *a[i];
    }

    element_multi_double(in1a, in1a, m);
}


void pbc_element_multi_add(element_ptr n[], element_ptr a[], element_ptr b[], int m) {
    element_t _n[m];
    element_t _a[m];
    element_t _b[m];

    int i;
    for (i = 0; i < m; i++) {
        _n[i][0] = *n[i];
        _a[i][0] = *a[i];
        _b[i][0] = *b[i];
    }

    element_multi_add(_n, _a, _b, m);
}

void pbc_element_pairing(element_t out, element_t in1, element_t in2) {
    element_pairing(out, in1, in2);
}


void pbc_curve_x_coord(element_t out, element_t element) {
    element_ptr x = element_x(element);

    element_init_same_as(out, x);
    element_set(out, x);
    out[0] = *x;
}

void pbc_curve_y_coord(element_t out, element_t element) {
    element_ptr y = element_y(element);

    element_init_same_as(out, y);
    element_set(out, y);
    out[0] = *y;
}


void pbc_element_pp_init(element_pp_t p, element_t in) {
    element_pp_init(p, in);
}

void pbc_element_pp_clear(element_pp_t p) {
    element_pp_clear(p);
}

void pbc_element_pp_pow(element_t out, mpz_ptr power, element_pp_t p) {
    element_pp_pow(out, power, p);
}

void pbc_element_pp_pow_zn(element_t out, element_t power, element_pp_t p) {
    element_pp_pow_zn(out, power, p);
}

/*
int pbc_element_pp_length(element_pp_t p) {
    struct element_base_table *base_table = p->data;

    int lookup_size = 1 << base_table->k;
    int length = element_length_in_bytes(base_table->table[0][0]);

    return length * base_table->num_lookups * lookup_size;
}

void pbc_element_pp_to_bytes(unsigned char *data, element_pp_t p) {
    struct element_base_table *base_table = p->data;

    int lookup_size = 1 << base_table->k;
    element_t *lookup;
    int i, j;
    int offset = 0;
    element_t **epp = base_table->table;
    int length = element_length_in_bytes(epp[0][0]);

    for (i = 0; i < base_table->num_lookups; i++) {
        lookup = epp[i];
        for (j = 0; j < lookup_size; j++) {
            offset += element_to_bytes(data[offset], lookup[j]);
        }
    }
}
*/

// =============
// field methods
// =============

void pbc_field_order(element_t element, mpz_t order) {
    //gmp_fprintf(stderr, "element->field->order = %Zd\n", element->field->order);
    mpz_set(order, element->field->order);
    //gmp_fprintf(stderr, "order = %Zd\n", order);
}
