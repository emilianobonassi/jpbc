package it.unisa.dia.gas.plaf.jpbc.gmp.jna;

import com.sun.jna.Native;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GMPProvider {

    private static GMPLibrary gmpLibrary;

    static {
        try {
            gmpLibrary = (GMPLibrary) Native.loadLibrary("gmp", GMPLibrary.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static GMPLibrary getGmpLibrary() {
        return gmpLibrary;
    }

    public static boolean isAvailable() {
        return gmpLibrary != null;
    }

}
