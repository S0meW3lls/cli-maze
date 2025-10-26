package libraries.cli;

import libraries.cli.menu.Menu;
import libraries.cli.menu.MenuType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void out(String message, boolean newline){
        System.out.printf("%s%s", message, newline ? "\n" : "");
    }

    public static void out(String message){
        CLI.out(message, false);
    }

    public static void out(StringBuilder builder) {
        CLI.out(builder.toString());
    }

    /**
     * Prompts the user for a line of text.
     *
     * @param prompt The message to display to the user.
     * @param new_line true if we need to escape row after prompt, false otherwise
     * @return The string input by the user.
     */
    public static String input(String prompt, boolean new_line){
        System.out.print(prompt + (new_line ? "\n" : ""));
        return CLI.scanner.nextLine();
    }

    /**
     * Prompts the user for a line of text.
     * <p>
     * This is a convenience method that calls {@link #input(String, boolean)} with a default of {@code false}.
     */
    public static String input(String prompt){
        return CLI.input(prompt, false);
    }

    /**
     * Prompts the user for an integer.
     * Note: This method does not handle InputMismatchException if the user enters non-integer input.
     *
     * @param prompt The message to display to the user.
     * @return The integer input by the user.
     */
    public static int inputNum(String prompt) {
        System.out.print(prompt);
        int n = CLI.scanner.nextInt();
        CLI.scanner.nextLine();
        return n;
    }

    /**
     * Prompts the user for a double value.
     * Note: This method does not handle InputMismatchException if the user enters non-double input.
     *
     * @param prompt The message to display to the user.
     * @return The double value input by the user.
     */
    public static double inputFlt(String prompt) {
        System.out.print(prompt);
        double n = CLI.scanner.nextDouble();
        CLI.scanner.nextLine();
        return n;
    }

    /**
     * Prompts the user for a boolean (y/n) answer.
     *
     * @param prompt The message to display to the user.
     * @param def The default value, returned if the user just presses Enter.
     * @return true if the user's input starts with 'y', false otherwise.
     */
    public static boolean inputBool(String prompt, boolean def) {

        // print prompt
        String y = def ? "Y" : "y";
        String n = !def ? "N" : "n";
        System.out.printf("%s [%s/%s] ", prompt, y, n);
        char r = CLI.scanner.next().toLowerCase().charAt(0);

        return r == 'y';
    }


    /**
     * Prompts the user for a date answer
     *
     * @param prompt the message to display to the user
     * @param nullable true if prompt can be skipped returning null date, false otherwise
     * @return a LocalDate containing a parsed date for the user input
     */
    public static LocalDate inputDate(String prompt, boolean nullable){
        // setup vars
        LocalDate date;
        boolean is_valid;

        // keep asking the user until date is valid
        do {
            date = null;
            is_valid = true;
            try {
                // convert given date string into a valid date object
                String date_str = CLI.input(prompt);

                // attempt to convert the date given
                if (!date_str.trim().isEmpty()) date = LocalDate.parse(date_str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                else if(!nullable) throw new DateTimeParseException("", date_str, 0);

            } catch (DateTimeParseException e) {
                System.out.println(CLIStyle.RED + "Invalid date given (use dd/mm/yyyy format)" + CLIStyle.RESET);
                is_valid = false;
            }
        }while (!is_valid);

        return date;
    }

    /**
     * Prompts the user for a date answer
     * <p>This is a convenience method that calls {@link #inputDate(String, boolean)}
     * with {@code nullable} set to {@code false}
     * </p>
     *
     * @param prompt The message to display to the user
     * @return a LocalDate containing the parsed user input
     */
    public static LocalDate inputDate(String prompt){
        return CLI.inputDate(prompt, false);
    }

    /**
     * Shows a menu on CLI screen
     * @param type the type of menu
     * @param prompt the initial prompt
     * @param options the available options
     * @return the selected option
     */
    public static int showMenu(MenuType type, String prompt, List<String> options) {
        return (new Menu(type, prompt, options)).show();
    }

    /**
     * Get CLI width and height
     *
     * @return the console screen dimensions as int[] {width, height}
     */
    public static int[] getDimensions() {
        int rows, cols;

        try {
            // define the command to be executed
            String[] cmd = {"/bin/sh", "-c", "stty size </dev/tty"};

            // start a new process to execute the command
            Process process = new ProcessBuilder(Arrays.asList(cmd)).start();

            // read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();

            // wait for the process to complete
            int exitCode = process.waitFor();

            // if errors were encountered on the command
            if (exitCode != 0 || output == null)  return new int[]{0,0};

            // The output is expected to be in the format "rows cols" so we convert them in int
            String[] dimensions = output.split(" ");
            rows = Integer.parseInt(dimensions[0]);
            cols = Integer.parseInt(dimensions[1]);

        } catch (IOException | InterruptedException e) {
            return new int[]{0,0};
        }

        return new int[] {cols, rows};
    }

    /**
     * Get the screen width
     *
     * @return the CLI screen width
     */
    public static int getWidth() {
        int[] d = CLI.getDimensions();
        return d[0];
    }

    /**
     * Get the CLI screen height
     *
     * @return the CLI screen height
     */
    public static int getHeight() {
        int[] d = CLI.getDimensions();
        return d[1];
    }

    /**
     * clears the console screen
     */
    public static void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Removes ANSI escape codes from a string.
     *
     * @param str The string to clean.
     * @return The string without any ANSI color or formatting codes.
     */
    public static String stripAnsiCodes(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        // This regex removes the ANSI color codes
        return str.replaceAll("\\u001B\\[[0-9;]*m", "");
    }

    /**
     * Calculates the visible length of a string by first stripping ANSI escape codes.
     * This is useful for aligning text in tables.
     *
     * @param str The string to measure.
     * @return The visible character count of the string.
     */
    public static int getVisibleLength(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        return CLI.stripAnsiCodes(str).length();
    }

}