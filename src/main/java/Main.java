/**
 * Turing Machine - Main.java 16/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

import turing_machine.controller.Controller;
import turing_machine.model.TuringMachine;

/**
 * Main of turing machine simulator, it could take a file by parameter
 *
 */
public class Main {
    public static String USAGE = "./turingmachine [file_name]    // with definition of automaton";
    public static void main(String[] args) throws Exception {
        ParseCommands commands = new ParseCommands(args, USAGE);

        Controller controller = new Controller();
        try {
            TuringMachine turingMachine = new TuringMachine(commands.getString());
            controller.setTuringMachine(turingMachine);
        }
        catch (Exception e) {
            controller.showErrorDialog(e.getMessage());
        }
        controller.options();
    }
}

