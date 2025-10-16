import libraries.cli.CLI;
import libraries.cli.CLIStyle;
import libraries.maze.Maze;


public class Main {
    public static void main(String[] args) {

        // ask user for basic inputs
        CLI.clear();
        int width = CLI.inputNum(String.format("Maze width (max: %s) : ", Math.floorDiv(CLI.getWidth(),2) - 10));
        CLI.clear();
        int height = CLI.inputNum(String.format("Maze height (max: %s) : ", Math.floorDiv(CLI.getHeight(),2) - 10));

        // generate the maze
        Maze maze = new Maze(width, height);

        // decide start / end of the maze
        maze.createEntryPoints();

        // start RDS visualization
        CLI.clear();
        maze.generateWithRDS();

        if(CLI.inputBool("\nShow final result?", true)) {
            CLI.clear();
            maze.show(false);
        }

    }
}