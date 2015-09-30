package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class IPLOSTW10KeyParameters extends AsymmetricKeyParameter {
    private IPLOSTW10Parameters parameters;


    public IPLOSTW10KeyParameters(boolean isPrivate, IPLOSTW10Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public IPLOSTW10Parameters getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPLOSTW10KeyParameters)) return false;

        IPLOSTW10KeyParameters that = (IPLOSTW10KeyParameters) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}