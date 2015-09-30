package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCTypeFCurveGenerator extends PBCCurveGenerator {
    protected int rbits;


    public PBCTypeFCurveGenerator(int rbits) {
        this.rbits = rbits;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_f(fileName, rbits);
    }
}