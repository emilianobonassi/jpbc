package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCTypeGCurveGenerator extends PBCCurveGenerator {
    protected int discriminant;


    public PBCTypeGCurveGenerator(int discriminant) {
        this.discriminant = discriminant;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_g(fileName, discriminant);
    }
}