package it.unisa.dia.gas.crypto.dfa;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface DFA {

    static interface Alphabet {

        int getSize();

        int getIndex(Character character);

    }

    static interface Transition {

        int getFrom();

        Character getReading();

        int getTo();

    }


    int getInitialState();

    Transition getTransition(int from, Character reading);

    boolean isFinalState(int state);

    int getNumTransitions();

    Transition getTransitionAt(int index);

    int getNumStates();

    int getNumFinalStates();

    int getFinalStateAt(int index);


}
