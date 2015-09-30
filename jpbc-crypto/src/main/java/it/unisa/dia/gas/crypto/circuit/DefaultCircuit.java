package it.unisa.dia.gas.crypto.circuit;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class DefaultCircuit implements Circuit {

    private int n, q;
    private int depth;
    private Gate[] gates;


    public DefaultCircuit(int n, int q, int depth, DefaultGate[] gates) {
        this.n = n;
        this.q = q;
        this.depth = depth;

        this.gates = gates;
        for (DefaultGate gate : gates)
            gate.setCircuit(this);
    }


    public int getN() {
        return n;
    }

    public int getQ() {
        return q;
    }

    public int getDepth() {
        return depth;
    }


    public Iterator<Gate> iterator() {
        return Arrays.asList(gates).iterator();
    }

    public Gate getGateAt(int index) {
        return gates[index];
    }

    public Gate getOutputGate() {
        return gates[n + q - 1];
    }


    public static class DefaultGate implements Circuit.Gate {

        private Circuit circuit;

        private Type type;
        private int index;
        private int depth;
        private int[] inputs;

        private boolean value;


        public DefaultGate(Type type, int index, int depth) {
            this.type = type;
            this.index = index;
            this.depth = depth;
        }

        public DefaultGate(Type type, int index, int depth, int[] inputs) {
            this.type = type;
            this.index = index;
            this.depth = depth;
            this.inputs = Arrays.copyOf(inputs, inputs.length);
        }


        public Type getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }

        public int getDepth() {
            return depth;
        }

        public int getInputIndexAt(int index) {
            return inputs[index];
        }

        public Gate getInputAt(int index) {
            return circuit.getGateAt(getInputIndexAt(index));
        }


        public void set(boolean value) {
            this.value = value;
        }

        public boolean isSet() {
            return value;
        }

        public Circuit.Gate evaluate() {
            switch (type) {
                case AND:
                    this.value = getInputAt(0).isSet() && getInputAt(1).isSet();
                    break;

                case OR:
                    this.value = getInputAt(0).isSet() || getInputAt(1).isSet();
                    break;

                default:
                    throw new IllegalStateException("Invalid type");
            }

            return this;
        }

        @Override
        public String toString() {
            return "Gate{" +
                    "type=" + type +
                    ", index=" + index +
                    ", depth=" + depth +
                    ", inputs=" + Arrays.toString(inputs) +
                    ", value=" + value +
                    '}';
        }


        protected void setCircuit(Circuit circuit) {
            this.circuit = circuit;
        }

    }

}
