package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCG1Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCG2Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCGTField;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCZrField;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingPPType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCPairingType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PBCPairing extends AbstractPairing {

    protected PBCPairingType pairing;


    public PBCPairing(PairingParameters parameters) {
        if (!WrapperLibraryProvider.isAvailable())
            throw new IllegalStateException("PBC support is not available.");

        // Init pairing...
        pairing = new PBCPairingType(parameters.toString(" "));

        // Init fields
        initFields();
    }


    public boolean isSymmetric() {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_is_symmetric(pairing) == 1;
    }

    public Element pairing(Element in1, Element in2) {
        PBCElement out = (PBCElement) GT.newElement();

        WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_apply(
                out.getValue(),
                ((PBCElement) in1).getValue(),
                ((PBCElement) in2).getValue(),
                pairing
        );
//        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pairing(
//                out.getValue(),
//                ((PBCElement) in1).getValue(),
//                ((PBCElement) in2).getValue()
//        );

        return out;
    }

    public boolean isProductPairingSupported() {
        return true;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        PBCElement out = (PBCElement) GT.newElement();

        Pointer[] in1Pointers = new Pointer[in1.length];
        for (int i = 0; i < in1.length; i++) {
            in1Pointers[i] = ((PBCElement) in1[i]).getValue();
        }

        Pointer[] in2Pointers = new Pointer[in2.length];
        for (int i = 0; i < in2.length; i++) {
            in2Pointers[i] = ((PBCElement) in2[i]).getValue();
        }

        WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_prod(
                out.getValue(),
                in1Pointers,
                in2Pointers,
                in1.length);

        return out;
    }

    public PairingPreProcessing getPairingPreProcessingFromElement(Element in1) {
        return new PBCPairingPreProcessing(in1);
    }

    public PairingPreProcessing getPairingPreProcessingFromBytes(byte[] source) {
        return new PBCPairingPreProcessing(source);
    }

    public PairingPreProcessing getPairingPreProcessingFromBytes(byte[] source, int offset) {
        return new PBCPairingPreProcessing(source, offset);
    }

    public int getPairingPreProcessingLengthInBytes() {
        if (WrapperLibraryProvider.getWrapperLibrary().pbc_is_pairing_pp_io_available(pairing))
            return WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_length_in_bytes(pairing);
        else
            return WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_length_in_bytes_G1(pairing);
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        return WrapperLibraryProvider.getWrapperLibrary().pbc_is_almost_coddh(
                ((PBCElement) a).getValue(),
                ((PBCElement) b).getValue(),
                ((PBCElement) c).getValue(),
                ((PBCElement) d).getValue(),
                pairing
        ) == 1;
    }


    protected void initFields() {
        G1 = new PBCG1Field(pairing);
        G2 = new PBCG2Field(pairing);
        GT = new PBCGTField(pairing);
        Zr = new PBCZrField(pairing);
    }


    public class PBCPairingPreProcessing implements PairingPreProcessing {
        protected PBCPairingPPType pairingPPType;

        protected Pointer in1;


        public PBCPairingPreProcessing(Element in1) {
            this.in1 = ((PBCElement) in1).value;
            this.pairingPPType = new PBCPairingPPType(this.in1, pairing);
        }

        public PBCPairingPreProcessing(byte[] source) {
            if (WrapperLibraryProvider.getWrapperLibrary().pbc_is_pairing_pp_io_available(pairing)) {
                this.pairingPPType = new PBCPairingPPType(pairing, source, 0);
            } else
                fromBytes(source, 0);
        }

        public PBCPairingPreProcessing(byte[] source, int offset) {
            if (WrapperLibraryProvider.getWrapperLibrary().pbc_is_pairing_pp_io_available(pairing)) {
                this.pairingPPType = new PBCPairingPPType(pairing, source, offset);
            } else
                fromBytes(source, offset);
        }

        public Element pairing(Element in2) {
            PBCElement out = (PBCElement) GT.newElement();
            WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_apply(
                    out.value,
                    ((PBCElement) in2).value,
                    pairingPPType
            );

            return out;
        }

        public byte[] toBytes() {
            if (WrapperLibraryProvider.getWrapperLibrary().pbc_is_pairing_pp_io_available(pairing)) {
                byte[] bytes = new byte[WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_length_in_bytes(pairing)];
                WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_pp_to_bytes(bytes, pairingPPType);
                return bytes;
            } else {
                byte[] bytes = new byte[WrapperLibraryProvider.getWrapperLibrary().pbc_element_length_in_bytes(this.in1)];
                WrapperLibraryProvider.getWrapperLibrary().pbc_element_to_bytes(bytes, this.in1);
                return bytes;
            }
        }

        public void fromBytes(byte[] source, int offset) {
            PBCElement tmep = (PBCElement) getG1().newElementFromBytes(source, offset);

            this.in1 = tmep.value;
            this.pairingPPType = new PBCPairingPPType(this.in1, pairing);
        }

    }
}
