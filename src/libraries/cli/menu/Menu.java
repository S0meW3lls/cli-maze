package libraries.cli.menu;

import libraries.cli.CLIStyle;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {


    private final MenuType type;
    private final String prompt;
    private final List<String> options;
    private final Scanner scanner;
    private final String error_message;

    public Menu(MenuType type, String prompt, List<String> options, String error_message){
        this.type = type;
        this.prompt = prompt;
        this.options = options;
        this.scanner = new Scanner(System.in);
        this.error_message = error_message;
    }

    public Menu(MenuType type, String prompt, List<String> options){
        this(type, prompt, options, "Invalid option given");
    }

    /**
     * Shows the configured menu to the user command line
     *
     * @return the choice made by the user
     */
    public int show(){

        // setup some variables
        int choice = 0;
        boolean error;
        int max = this.getOptionNumberMaxLength(options.size()); // used to nicely print options

        // show question and options
        System.out.println(prompt);
        for(int i =0; i < options.size(); i++){
            System.out.println(String.format("%-"+max+"s", this.getOptionNumber(i + 1)) + options.get(i));
        }

        // keep asking the user for an input
        do{
            error = false;
            System.out.print("Choice: ");
            try{
                choice = this.scanner.nextInt();

                // if an invalid option was given
                if(choice <= 0 || choice > options.size()) throw new IndexOutOfBoundsException();

            }catch(InputMismatchException | IndexOutOfBoundsException e){
                error = true;
                this.scanner.nextLine();
                System.out.println(CLIStyle.RED+this.error_message+ CLIStyle.RESET);
            }
        }while(error);

        return choice - 1;
    }

    /**
     * retrieve the option string based on the menu type given
     *
     * @param number the number to convert
     * @return the option number string, based on prefs
     */
    private String getOptionNumber(int number){
        String output;

        if(this.type == MenuType.NUMBERED) output = number + ")";
        else if(this.type == MenuType.DOTTED) output = "•";
        else if(this.type == MenuType.ROMAN) output = this.getRomanOptionNumber(number) + ")";
        else output = "";

        return output;
    }

    /**
     * Finds up to the given number, based on options the max length taking option
     *
     * @param number the number to check up to
     * @return the max length taking option
     */
    private int getOptionNumberMaxLength(int number) {
        int max;

        if(this.type == MenuType.NUMBERED) max = Math.floorDiv(number , 10) + 2;
        else if(this.type == MenuType.DOTTED) max = 1;
        else if(this.type == MenuType.ROMAN) max = this.getRomanOptionNumberMaxLength(number) + 1;
        else max = 1;

        return max + 1; // plus 1 is to add an extra space
    }

    /**
     * return a roman number string for the given number
     *
     * @param number the number to convert
     * @return the roman representation of the number
     */
    private String getRomanOptionNumber(int number) {

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        // iterate through each value-symbol pair
        for (int i = 0; i < values.length; i++) {
            // add the symbol as many times as the value fits into num
            while (number >= values[i]) {
                result.append(symbols[i]);
                number -= values[i];
            }
        }

        return result.toString();
    }

    /**
     * Finds up to the given number, based on options the max length taking option in roman format
     *
     * @param number the number to search
     * @return the max length taken to roman numbers from 0 up to number
     */
    private int getRomanOptionNumberMaxLength(int number) {
        int max = 0;

        // loop all numbers and check which one takes more space
        for(int i = 1; i <= number; i++){
            int l = this.getRomanOptionNumber(i).length();
            if(l > max) max = l;
        }

        return max;
    }
}
