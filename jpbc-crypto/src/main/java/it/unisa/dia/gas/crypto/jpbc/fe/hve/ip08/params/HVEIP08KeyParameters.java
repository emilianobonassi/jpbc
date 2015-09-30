package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class HVEIP08KeyParameters extends AsymmetricKeyParameter {
    private HVEIP08Parameters parameters;


    public HVEIP08KeyParameters(boolean isPrivate, HVEIP08Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public HVEIP08Parameters getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HVEIP08KeyParameters)) return false;

        HVEIP08KeyParameters that = (HVEIP08KeyParameters) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}