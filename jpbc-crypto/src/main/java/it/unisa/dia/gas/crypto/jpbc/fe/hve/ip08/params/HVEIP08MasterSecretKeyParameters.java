package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08MasterSecretKeyParameters extends HVEIP08KeyParameters {
    private Element y;
    private List<List<Element>> t,v;

    private List<List<Element>> preT, preV;
    private boolean preProcessed = false;


    public HVEIP08MasterSecretKeyParameters(HVEIP08Parameters parameters, Element y, List<List<Element>> t, List<List<Element>> v) {
        super(true, parameters);

        this.y = y.getImmutable();

        this.t = t;
        this.v = v;
    }


    public Element getY() {
        return y;
    }

    public Element getTAt(int row, int col) {
        return t.get(row).get(col);
    }

    public Element getVAt(int row, int col) {
        return v.get(row).get(col);
    }

    public Element getPreTAt(int row, int col) {
        return preT.get(row).get(col);
    }

    public Element getPreVAt(int row, int col) {
        return preV.get(row).get(col);
    }

    public void preProcess() {
        if (preProcessed)
            return;

        getParameters().preProcess();

        int  n = getParameters().getN();
        preT = new ArrayList<List<Element>>(n);
        preV = new ArrayList<List<Element>>(n);
        for (int i = 0; i < n; i++) {
            int attributeNum = getParameters().getAttributeNumAt(i);

            List<Element> listT = new ArrayList<Element>(attributeNum);
            List<Element> listV = new ArrayList<Element>(attributeNum);
            for (int j = 0; j < attributeNum; j++) {
                listT.add(getTAt(i, j).invert());
                listV.add(getVAt(i, j).invert());
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