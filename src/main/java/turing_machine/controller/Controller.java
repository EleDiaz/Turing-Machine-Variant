/**
 * Turing Machine - Controller.java 16/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */

package turing_machine.controller;


import turing_machine.model.Header;
import turing_machine.model.TuringMachine;

import java.util.Scanner;

/**
 * A controller to TUI interaction with user.
 *
 */
public class Controller {
    public static Scanner SCANNER = new Scanner(System.in);

    private TuringMachine turingMachine;

    /**
     * Main menu
     */
    public void options() throws Exception{
        int option = 1;
        while (option != 0) {
            System.out.println("1) Set inputs to each header");
            System.out.println("2) Check a string into a giving turing machine");
            System.out.println("3) Check input step by step");
            System.out.println("4) Open turing machine file");
            System.out.println("0) Exit of application");
            option = SCANNER.nextInt();
            switch (option) {
                case 1:
                    setInputs();
                    break;
                case 2:
                    checkString();
                    break;
                case 3:
                    checkByStep();
                    break;
                case 4:
                    onOpenFile();
                    break;
            }
        }
    }

    /**
     * TODO:
     */
    public void setInputs() {
        System.out.println("Set input(s)");

        for (int i = 0; i < getTuringMachine().getHeaders().size(); i++) {
            System.out.println("Introduce input:");
            getTuringMachine().getHeaders().set(i, new Header(SCANNER.next(), getTuringMachine().getBlankSymbol()));
        }
        showTuring();
    }

    /**
     * TODO:
     */
    public void checkString() throws Exception{
        try {
            if (getTuringMachine().run()) {
                System.out.println("Is accept input");
            }
            else {
                System.out.println("No accept input");
            }
            showTuring();
        } catch (Exception err) {
            showErrorDialog(err.getMessage());
        }
    }

    /**
     * TODO:
     */
    public void checkByStep() {
        getTuringMachine().reset();
        System.out.println("------ Initial configuration ------");
        showTuring();
        while (true) {
            if (!getTuringMachine().step()) {
                if (getTuringMachine().getEndStates().contains(getTuringMachine().getCurrentState())) {
                    System.out.println("Is accept input");
                }
                else {
                    System.out.println("No accept input");
                }
                break;
            }
            showTuring();
        }
    }

    /**
     * TODO:
     */
    public void showTuring() {
        System.out.println("------ LOADED TURING MACHINE --- ---");
        System.out.println("Current State: " + getTuringMachine().getCurrentState());
        getTuringMachine().getHeaders().forEach(Header::show);
    }

    /**
     * Dispatch a file dialog to open a .tm files
     */
    public void onOpenFile() {
        System.out.println("Introduce a turing machine file specification (.tm)");
        String file = new Scanner(System.in).nextLine();

        try {
            turingMachine = new TuringMachine(file);
            System.out.println("Loaded a turing machine file file");
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    /**
     * Show a error dialog with specified text
     */
    public void showErrorDialog(String error) {
        System.out.println("An error happened");
        System.out.println(">   " +  error);
        System.out.println("\nPress Enter to continue");
        SCANNER.next();
    }

    /**
     *
     */
    public TuringMachine getTuringMachine() {
        return turingMachine;
    }

    public Controller setTuringMachine(TuringMachine turingMachine) {
        this.turingMachine = turingMachine;
        return this;
    }
}
