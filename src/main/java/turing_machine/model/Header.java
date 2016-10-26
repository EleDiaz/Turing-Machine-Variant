/**
 * Turing Machine - Header.java 19/10/16
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
public class Header {
    /**
     * Right part of tape
     */
    private ArrayList<Character> right = new ArrayList<>();
    /**
     * Left part of tape
     */
    private ArrayList<Character> left = new ArrayList<>();
    /**
     * Position into tape
     */
    private int position = 0;
    /**
     * Char to fill tape
     */
    private char blank = '$';
    /**
     * Initial contents
     */
    private String contents = "";

    /**
     * Empty Constructor
     */
    public Header() {}

    /**
     * Build a infinite tape
     * @param text actual content of tape start in 0 position and end over text.lenght
     * @param blank char specified when no there any char into tape
     */
    public Header(String text, char blank) {
        right = text.chars().mapToObj(iChar -> (char) iChar).collect(Collectors.toCollection(ArrayList::new));
        left = new ArrayList<>();
        this.blank = blank;
        contents = text;
    }

    /**
     * Reset Header to initial input
     */
    public void reset() {
        setRight(getContents()
            .chars()
            .mapToObj(iChar -> (char) iChar)
            .collect(Collectors.toCollection(ArrayList::new)));
        setLeft(new ArrayList<>());
    }

    /**
     * Move one unit to left into tape
     */
    public void moveLeft() {
        position--;
        if (position < -getLeft().size()) {
            getLeft().add(getBlank());
        }
    }

    /**
     * Move one unit to right into tape
     */
    public void moveRight() {
        position++;
        if (position >= getRight().size()) {
            getRight().add(getBlank());
        }
    }

    /**
     * Get Current char if not there are character belong to input alphabet its returns blank char
     */
    public char getCurrentChar() {
        if (position >= 0) {
            return getRight().get(position);
        }
        else {
            return getLeft().get(-position - 1);
        }
    }

    /**
     * Set char into current position, it overwrite previous char
     * @param chr char to insert
     */
    public void setCurrentChar(char chr) {
        if (position >= 0) {
            getRight().set(position, chr);
        }
        else {
            getLeft().set(-position - 1, chr);
        }
    }

    /**
     * Show tape with a mark below and above of tape, this mark show the position over tape
     */
    public void show() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = getLeft().size() - 1; i >= 0; i--) {
            stringBuilder.append(getLeft().get(i));
        }

        for (int i = 0; i < getRight().size(); i++) {
            stringBuilder.append(getRight().get(i));
        }

        StringBuilder posHead = new StringBuilder();
        int auxPos = position >= 0? position + getLeft().size() : getLeft().size() + position;

        for (int i = 0; i < getLeft().size() + getRight().size(); i++) {
            if (auxPos == i) {
                posHead.append("|");
            }
            else {
                posHead.append(" ");
            }
        }

        System.out.println(posHead.toString());
        System.out.println(stringBuilder.toString());
        System.out.println(posHead.toString());
    }

    public String getContents() {
        return contents;
    }

    /**
     *
     */
    public ArrayList<Character> getRight() {
        return right;
    }

    public Header setRight(ArrayList<Character> right) {
        this.right = right;
        return this;
    }

    /**
     *
     */
    public ArrayList<Character> getLeft() {
        return left;
    }

    public Header setLeft(ArrayList<Character> left) {
        this.left = left;
        return this;
    }

    /**
     *
     */
    public char getBlank() {
        return blank;
    }

    public Header setBlank(char blank) {
        this.blank = blank;
        return this;
    }

    public Header setContents(String contents) {
        this.contents = contents;
        return this;
    }
}
