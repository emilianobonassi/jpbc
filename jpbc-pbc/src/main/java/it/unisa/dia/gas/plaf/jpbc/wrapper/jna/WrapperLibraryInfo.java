package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WrapperLibraryInfo {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));

        // Check for link library
        if (!WrapperLibraryProvider.isAvailable()) {
            System.out.println("PBC is not available.");
            return;
        }
        System.out.println("PBC is available.");

        System.out.println("pbc_is_pairing_pp_io_flag() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_is_pairing_pp_io_flag());
        System.out.println("pbc_pairing_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_sizeof());
        System.out.println("pbc_pairing_pp_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_sizeof());
        System.out.println("gmp_mpz_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().gmp_mpz_sizeof());
        System.out.println("pbc_element_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_element_sizeof());
        System.out.println("pbc_element_pp_sizeof() = " + WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_sizeof());
    }

}
