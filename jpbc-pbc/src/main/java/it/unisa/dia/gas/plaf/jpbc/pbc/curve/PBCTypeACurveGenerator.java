package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCTypeACurveGenerator extends PBCCurveGenerator {
    protected int rbits, qbits;


    public PBCTypeACurveGenerator(int rbits, int qbits) {
        this.rbits = rbits;
        this.qbits = qbits;
    }


    protected void pbcGenerate(String fileName) {
        WrapperLibraryProvider.getWrapperLibrary().pbc_curvegen_a(fileName, rbits, qbits);
    }
}
