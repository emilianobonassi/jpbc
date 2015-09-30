package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08PublicKeyParameters extends HVEIP08KeyParameters {
    private Element Y;
    private List<List<Element>> T, V;

    private List<List<ElementPow>> preT, preV;
    private boolean preProcessed = false;


    public HVEIP08PublicKeyParameters(HVEIP08Parameters parameters,
                                      Element Y,
                                      List<List<Element>> T,
                                      List<List<Element>> V) {
        super(false, parameters);

        this.Y = Y.getImmutable();

        this.T = T;
        this.V = V;
    }


    public Element getY() {
        return Y;
    }

    public Element getTAt(int row, int col) {
        return T.get(row).get(col);
    }

    public Element getVAt(int row, int col) {
        return V.get(row).get(col);
    }

    public ElementPow getElementPowTAt(int row, int col) {
        return preProcessed ? preT.get(row).get(col) : T.get(row).get(col);
    }

    public ElementPow getElementPowVAt(int row, int col) {
        return preProcessed ? preV.get(row).get(col) : V.get(row).get(col);
    }

    public void preProcess() {
        if (preProcessed)
            return;

        getParameters().preProcess();

        int  n = getParameters().getN();
        preT = new ArrayList<List<ElementPow>>(n);
        preV = new ArrayList<List<ElementPow>>(n);
        for (int i = 0; i < n; i++) {
            int attributeNum = getParameters().getAttributeNumAt(i);

            List<ElementPow> listT = new ArrayList<ElementPow>(attributeNum);
            List<ElementPow> listV = new ArrayList<ElementPow>(attributeNum);
            for (int j = 0; j < attributeNum; j++) {
                listT.add(getTAt(i, j).getElementPowPreProcessing());
                listV.add(getVAt(i, j).getElementPowPreProcessing());
            }

            preT.add(listT);
            preV.add(listV);
        }
        preProcessed = true;
    }


    public boolean isPreProcessed() {
        return preProcessed;
    }
}