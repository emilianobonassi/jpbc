package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import junit.framework.TestCase;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WrapperLibraryTest extends TestCase {

    public void testSize() {
//        System.out.println(System.getProperty("java.library.path"));

        // Check for link library
        if (!WrapperLibraryProvider.isAvailable())
            return;
                
        assertTrue(WrapperLibraryProvider.isAvailable());

//        System.out.println("pbc_pairing_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_sizeof());
//        System.out.println("pbc_pairing_pp_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_sizeof());
//        System.out.println("gmp_mpz_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().gmp_mpz_sizeof());
//        System.out.println("pbc_element_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_element_sizeof());
//        System.out.println("pbc_element_pp_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_sizeof());

        assertNotSame(0, WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_sizeof());
        assertNotSame(0, WrapperLibraryProvider.getWrapperLibrary().pbc_element_sizeof());
        assertNotSame(0, WrapperLibraryProvider.getWrapperLibrary().gmp_mpz_sizeof());
        assertNotSame(0, WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_sizeof());
        assertNotSame(0, WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_sizeof());
    }

}
