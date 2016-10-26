/**
 * CC Asignements - Transition.java 19/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package turing_machine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * A wrapper class for Hashmap type, it is used to fast access to transition.
 * Its acts like a function
 * :: a in TapeAlphabet, n in Natural. (State, Vec a n) -> (State, Vec (a, Movement) n)
 *  // n indicates size of Vec
 */
public class Transition { // A heritage fix better here (final problem)
    private HashMap<Input, Output> transitions;

    /**
     * Auxiliar Type
     */
    public class Input {
        public String fromState;
        public ArrayList<Character> readChars;

        public Input(String fromState, ArrayList<Character> readChars) {
            this.fromState = fromState; this.readChars = readChars;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Input input = (Input) o;

            if (fromState != null ? !fromState.equals(input.fromState) : input.fromState != null) return false;
            if (readChars != null && readChars.size() == input.readChars.size()) {
                for (int i = 0; i < readChars.size(); i++) {
                    if (readChars.get(i) != input.readChars.get(i)) {
                        return false;
                    }
                }
                return true;
            }
            return readChars != null ? readChars.equals(input.readChars) : input.readChars == null;
        }

        @Override
        public int hashCode() {
            int result = fromState != null ? fromState.hashCode() : 0;
            result = 31 * result + (readChars != null ? readChars.hashCode() : 0);
            return result;
        }
    }

    /**
     * Auxiliar Type
     */
    public class Output {
        public String toState;
        public ArrayList<Character> writeChars;
        public ArrayList<Movement> movements;

        public Output(String toState, ArrayList<Character> writeChars, ArrayList<Movement> movements) {
            this.toState = toState; this.writeChars = writeChars; this.movements = movements;
        }
    }

    /**
     * A empty transition table
     */
    public Transition() {
        transitions = new HashMap<>();
    }

    /**
     * Add a transition to table
     * @param fromState
     * @param toState
     * @param readChars
     * @param writeChars
     * @param movements
     */
    void addTransition(String fromState, String toState
                      , ArrayList<Character> readChars
                      , ArrayList<Character> writeChars
                      , ArrayList<Movement> movements) {
        getTransitions().put(new Input(fromState, readChars), new Output(toState, writeChars, movements));
    }

    /**
     * Get transition in function of arrive state and read characters
     * @param fromState
     * @param readCharacters
     * @return transition to new state, and necessary operations
     */
    Optional<Output> getTrasition(String fromState, ArrayList<Character> readCharacters) {
        return Optional.ofNullable(getTransitions().get(new Input(fromState, readCharacters)));
    }

    /**
     * Display transition table in raw format
     */
    public void show() {
        getTransitions()
            .entrySet()
            .forEach(entry -> {
                Input input = entry.getKey();
                Output output = entry.getValue();
                System.out.println(input.fromState + " " + input.readChars + " " + output.toState + " " + output.writeChars + " " + output.movements);
            });
    }

    /// Getters and setters

    /**
     *
     */
    private HashMap<Input, Output> getTransitions() {
        return transitions;
    }

    private Transition setTransitions(HashMap<Input, Output> transitions) {
        this.transitions = transitions;
        return this;
    }
}