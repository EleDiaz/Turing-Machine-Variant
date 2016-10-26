/**
 * TuringMachine - Movement.java 25/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package turing_machine.model;

/**
 * Indicates movement of header in a tape
 *
 */
public enum Movement {
    LeftM,
    RightM,
    StopM;

    /**
     * Little parser from char to a Movement
     * @param character char to convert
     * @return correspondent movement
     * @throws Exception fail to parse
     */
    static Movement parse(Character character) throws Exception {
        switch (character) {
            case 'L':
                return LeftM;
            case 'R':
                return RightM;
            case 'S':
                return StopM;
            default:
                throw new Exception("Error on parse a movement");
        }
    }
}
