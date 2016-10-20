/**
 * CC Asignements - Transition.java 19/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package main.java.turing_machine.model;

import java.util.ArrayList;
import java.util.Set;

/**
 * TODO: Commenta algo
 *
 */
public class Transition {
    /**
     * Each header(case of turing multi header)
     */
    private ArrayList<Tape> tapes;
    private Set<String>     states;
    private Set<Character>  alphabet;
    private char            blankSymbol;
    private Transition      transitionTable;
    private String          startState;
    private Set<String>     endStates;


    /**
     * Build a Turing machine
     */
    public Transition() {
        // TODO: Check constraints to be a correct Turing machine
        // endStates ⊂ states
        // startState ∈ states
        // blankSymbol ∉ alphabet
        // Check transition table correctness
    }
}
