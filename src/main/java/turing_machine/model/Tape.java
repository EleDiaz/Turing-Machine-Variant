/**
 * CC Asignements - Tape.java 19/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package turing_machine.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a infinite tape in both sides
 *
 */
public class Tape {
    /**
     * Right part of tape
     */
    private ArrayList<Character> right;
    /**
     * Left part of tape
     */
    private ArrayList<Character> left;
    private int position = 0;
    private char blank;

    /**
     * Build a infinite tape
     * @param text actual content of tape start in 0 position and end over text.lenght
     * @param blank char specified when no there any char into tape
     */
    public Tape(String text, char blank) {
        right = text.chars().mapToObj(iChar -> (char) iChar).collect(Collectors.toCollection(ArrayList::new));
        left = new ArrayList<>();
        this.blank = blank;
    }

    /**
     * Move one unit to left into tape
     */
    public void moveLeft() {
        position--;
        if (position <= -left.size()) {
            left.add(blank);
        }
    }

    /**
     * Move one unit to right into tape
     */
    public void moveRight() {
        position++;

        if (position <= -left.size()) {
            left.add(blank);
        }
    }

    /**
     * Get Current char if not there are character belong to input alphabet its returns blank char
     */
    public char getCurrentChar() {
        if (position >= 0) {
            return right.get(position);
        }
        else {
            return left.get(1 - position);
        }
    }

    /**
     * Set char into current position, it overwrite previous char
     * @param chr char to insert
     */
    public void setCurrentChar(char chr) {
        if (position >= 0) {
            right.set(position, chr);
        }
        else {
            left.set(1 - position, chr);
        }
    }
}
