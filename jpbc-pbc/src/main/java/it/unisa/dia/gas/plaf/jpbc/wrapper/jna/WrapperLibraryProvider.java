package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Native;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class WrapperLibraryProvider {

    private static WrapperLibrary wrapperLibrary;

    static {
        try {
            wrapperLibrary = (WrapperLibrary) Native.loadLibrary("jpbc-pbc", WrapperLibrary.class);
        } catch (Throwable e) {
//            e.printStackTrace();
        }
    }


    public static WrapperLibrary getWrapperLibrary() {
        return wrapperLibrary;
    }

    public static boolean isAvailable() {
        return wrapperLibrary != null;
    }

}
