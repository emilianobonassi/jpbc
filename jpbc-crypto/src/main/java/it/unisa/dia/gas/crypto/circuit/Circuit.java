package it.unisa.dia.gas.crypto.circuit;

import java.util.Iterator;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface Circuit extends Iterable<Circuit.Gate> {

    int getN();

    int getQ();

    int getDepth();

    Iterator<Gate> iterator();

    Gate getGateAt(int index);

    Gate getOutputGate();


    interface Gate {

        public static enum Type {INPUT, AND, OR}

        Type getType();

        int getIndex();

        int getDepth();

        int getInputIndexAt(int index);

        Gate getInputAt(int index);

        void set(boolean value);

        boolean isSet();

        Gate evaluate();
    }


}
