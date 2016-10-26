/**
 * CC Asignements - TuringMachine.java 19/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package turing_machine.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A turing machine with multitape support. Each tape could grown in both directions. Besides
 * admit three possible movements: left, right and stop.
 *
 */
public class TuringMachine {
    /**
     * Parser Values
     */
    static final private String COMMENT    = " *(#.+)?\\n";
    static final private String ITEMS      = "\\S+( +\\S+)*";
    static final private String INI_STATE  = "\\S+";
    static final private String BLANK_CHAR = "\\S";
    static final private String HEAD_NUM   = "\\d+";
    // q1 fromA1 q2 toA2 L [fromA' toA' L']*
    static final private String TRANSITION = "\\S+ +\\S +\\S+ +\\S +[LRS]( +\\S +\\S +[LRS])*";
    /**
     * Parser definition
     */
    static final private Pattern headerComment = Pattern.compile("(" + COMMENT + ")*");
    static final private Pattern items         = Pattern.compile("(?<items>" + ITEMS + ")" + COMMENT);
    static final private Pattern initialState  = Pattern.compile("(?<iniSt>" + INI_STATE + ")" + COMMENT);
    static final private Pattern blankChar     = Pattern.compile("(?<blank>" + BLANK_CHAR + ")" + COMMENT);
    static final private Pattern headersNumber = Pattern.compile("(?<headN>" + HEAD_NUM + ")" + COMMENT);
    static final private Pattern transition    = Pattern.compile("(?<transition>" + TRANSITION + ")" + COMMENT);

    /**
     * Each header(case of turing multi header)
     */
    private ArrayList<Header> headers = new ArrayList<>();
    /**
     * TODO: REMOVE
     */
    private HashSet<String> states = new HashSet<>();
    /**
     * TODO: REMOVE
     */
    private HashSet<Character> alphabet = new HashSet<>();
    /**
     * The blank symbol used
     */
    private char blankSymbol;
    /**
     * Transition table (Sigma function in theoric speech)
     */
    private Transition transitionTable = new Transition();
    /**
     * Represent the first state to transit
     */
    private String startState;
    /**
     * End states, to accept a input
     */
    private HashSet<String> endStates = new HashSet<>();
    /**
     * Save the current state when its used in step by step "mode"
     */
    private String currentState;
    /**
     * Limit of iterations in a Turing machine
     */
    private int limitIterations = 100_000;


    /**
     * Build a Turing machine
     */
    public TuringMachine(String path) throws Exception {
        String contents = new String(Files.readAllBytes(Paths.get(path)));

        // Comments
        Matcher matcher = headerComment.matcher(contents);
        matcher.find();

        // States
        matcher.usePattern(items);
        if (!matcher.find()) { throw new Exception("Error on states specification"); }
        Arrays.stream(matcher.group("items").split(" ")).forEach(state -> getStates().add(state));

        // Tape alphabet
        if (!matcher.find()) { throw new Exception("Error on alphabet specification"); }
        Arrays.stream(matcher.group("items").split(" ")).forEach(chr -> getAlphabet().add(chr.charAt(0)));

        // Writing turing head
        if (!matcher.find()) { throw new Exception("Error on alphabet specification"); }
        //String tapeAlphabet = matcher.group("items");

        // Initial State
        matcher.usePattern(initialState);
        if (!matcher.find()) { throw new Exception("Error on initial state specification"); }
        setStartState(matcher.group("iniSt"));

        // Blank Char
        matcher.usePattern(blankChar);
        if (!matcher.find()) { throw new Exception("Error on blank char specification"); }
        setBlankSymbol(matcher.group("blank").charAt(0));

        // End States
        matcher.usePattern(items);
        if (!matcher.find()) { throw new Exception("Error on states specification"); }
        Arrays.stream(matcher.group("items").split(" ")).forEach(state -> getEndStates().add(state));

        // Number of headers
        matcher.usePattern(headersNumber);
        if (!matcher.find()) { throw new Exception("Error on number of headers specification"); }
        int headerQuantity = Integer.parseInt(matcher.group("headN"));

        for (int i = 0; i < headerQuantity; i++) {
            getHeaders().add(new Header());
        }

        // Transitions
        matcher.usePattern(transition);
        while (matcher.find()) {
            // q1 fromA1 q2 toA2 L [fromA' toA' L']*
            String[] transition = matcher.group("transition").split(" ");
            System.out.println(matcher.group("transition"));
            String fromState = transition[0];
            String toState   = transition[2];

            ArrayList<Character> readChars = new ArrayList<>();
            readChars.add(transition[1].charAt(0));

            ArrayList<Character> writeChars = new ArrayList<>();
            writeChars.add(transition[3].charAt(0));

            ArrayList<Movement> movements = new ArrayList<>();
            movements.add(Movement.parse(transition[4].charAt(0)));

            if (transition.length < ((getHeaders().size() - 1) * 3) + 5) {
                throw new Exception("Number of headers is different");
            }

            for (int i = 5; i < transition.length; i+=3) {
                readChars.add(transition[i].charAt(0));
                writeChars.add(transition[i+1].charAt(0));
                movements.add(Movement.parse(transition[i+2].charAt(0)));
            }
            getTransitionTable().addTransition(fromState, toState, readChars, writeChars, movements);
        }

        setCurrentState(getStartState());
        getTransitionTable().show();

        // TODO:
        //states.containsAll(endStates);
        //states.contains(startState);
    }

    /**
     * Execute the Turing machine
     * @return if input introduced into belong to language represented by Turing machine
     */
    public boolean run() throws Exception {
        reset();
        int count = 0;
        while (step() && count++ < getLimitIterations());

        if (count >= getLimitIterations()) {
            throw new Exception("Exceeded limit of iterations");
        }
        System.out.println(getEndStates());
        System.out.println(getCurrentState());
        return getEndStates().contains(getCurrentState());
    }

    /**
     * Reset Turing machine to initial input and start state
     */
    public void reset() {
        setCurrentState(getStartState());
        for (int i = 0; i < getHeaders().size(); i++) {
            getHeaders().get(i).reset();

        }
    }

    /**
     * Advance a step
     * @return return if the machine don't have any transition from current state and tape reads
     */
    public boolean step() {
        ArrayList<Character> readChars = getHeaders().stream()
            .map(Header::getCurrentChar)
            .collect(Collectors.toCollection(ArrayList::new));

        Optional<Transition.Output> optOutput = getTransitionTable().getTrasition(getCurrentState(), readChars);

        if (!optOutput.isPresent()) {
            return false;
        }

        // Java lacks of mapIndex :: (a -> Int -> b) -> f a -> f b
        for (int i = 0; i < getHeaders().size(); i++) {
            getHeaders().get(i).setCurrentChar(optOutput.get().writeChars.get(i));

            switch (optOutput.get().movements.get(i)) {
                case LeftM:
                    getHeaders().get(i).moveLeft();
                    break;
                case RightM:
                    getHeaders().get(i).moveRight();
                default:
            }
        }
        setCurrentState(optOutput.get().toState);
        return true;
    }

    /// Getters and Setters


    /**
     *
     */
    public ArrayList<Header> getHeaders() {
        return headers;
    }

    public TuringMachine setHeaders(ArrayList<Header> headers) {
        this.headers = headers;
        return this;
    }

    /**
     *
     */
    public HashSet<String> getStates() {
        return states;
    }

    public TuringMachine setStates(HashSet<String> states) {
        this.states = states;
        return this;
    }

    /**
     *
     */
    public HashSet<Character> getAlphabet() {
        return alphabet;
    }

    public TuringMachine setAlphabet(HashSet<Character> alphabet) {
        this.alphabet = alphabet;
        return this;
    }

    /**
     *
     */
    public char getBlankSymbol() {
        return blankSymbol;
    }

    public TuringMachine setBlankSymbol(char blankSymbol) {
        this.blankSymbol = blankSymbol;
        return this;
    }

    /**
     *
     */
    public Transition getTransitionTable() {
        return transitionTable;
    }

    public TuringMachine setTransitionTable(Transition transitionTable) {
        this.transitionTable = transitionTable;
        return this;
    }

    /**
     *
     */
    public String getStartState() {
        return startState;
    }

    public TuringMachine setStartState(String startState) {
        this.startState = startState;
        return this;
    }

    /**
     *
     */
    public HashSet<String> getEndStates() {
        return endStates;
    }

    public TuringMachine setEndStates(HashSet<String> endStates) {
        this.endStates = endStates;
        return this;
    }

    /**
     *
     */
    public String getCurrentState() {
        return currentState;
    }

    public TuringMachine setCurrentState(String currentState) {
        this.currentState = currentState;
        return this;
    }

    /**
     *
     */
    public int getLimitIterations() {
        return limitIterations;
    }

    public TuringMachine setLimitIterations(int limitIterations) {
        this.limitIterations = limitIterations;
        return this;
    }
}
