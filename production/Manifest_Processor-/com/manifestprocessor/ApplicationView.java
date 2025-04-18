package com.techelevator.custom;

import com.techelevator.util.BasicConsole;


/**
 * ApplicationView is a class that the ApplicationController uses for gathering information
 * from the user and presenting information to the user. It requires an object that implements
 * the BasicConsole interface to handle the mechanics of reading from and writing to the console.
 */

public class ApplicationView {
    // **************************************************************
    // region Printing to console in color
    // **************************************************************
    /*
    https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println has an example of
    printing in color. Here we use (char)27, which is the same character as '\u001B' (hex 1B == dec 27).
    Escape codes for color
    https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences
    +~~~~~~+~~~~~~+~~~~~~~~~~~+
    |  fg  |  bg  |  color    |
    +~~~~~~+~~~~~~+~~~~~~~~~~~+
    |  30  |  40  |  black    |
    |  31  |  41  |  red      |
    |  32  |  42  |  green    |
    |  33  |  43  |  yellow   |
    |  34  |  44  |  blue     |
    |  35  |  45  |  magenta  |
    |  36  |  46  |  cyan     |
    |  37  |  47  |  white    |
    |  39  |  49  |  default  |
    +~~~~~~+~~~~~~+~~~~~~~~~~~+
    */
    private final String FOREGROUND_DEFAULT = (char) 27 + "[39m";
    private final String FOREGROUND_RED = (char) 27 + "[31m";
    private final String FOREGROUND_GREEN = (char) 27 + "[32m";
    private final String FOREGROUND_BLUE = (char) 27 + "[34m";
    // **************************************************************
    // endregion Printing to console in color
    // **************************************************************

    private final BasicConsole console;

    // Constructor expect a console object to print to.
    public ApplicationView(BasicConsole console) {
        this.console = console;
    }

    // printMessage passes call through to console
    public void printMessage(String message) {
        console.printMessage(message);
    }

    // printErrorMessage makes the text color RED
    public void printErrorMessage(String message) {
        console.printErrorMessage(FOREGROUND_RED + message + FOREGROUND_DEFAULT);
    }

    // printBanner makes the text color GREEN
    public void printBanner(String message) {
        console.printBanner(FOREGROUND_GREEN + message + FOREGROUND_DEFAULT);
    }

    // promptForYesNo passes call through to console
    public boolean promptForYesNo(String prompt) {
        return console.promptForYesNo(prompt);
    }

    // getMenuSelection prints a BLUE banner, then passes through to console.
    public String getMenuSelection(String menuTitle, String[] options) {
        printBanner(FOREGROUND_BLUE + menuTitle + FOREGROUND_DEFAULT);
        return console.getMenuSelection(options);
    }

    public Integer promptForInt(String prompt){
        return console.promptForInteger(prompt);
    }

    public String promptForString(String enterTrailerNumber) {
        return console.promptForString(enterTrailerNumber);
    }
}
