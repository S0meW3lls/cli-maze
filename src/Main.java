import libraries.cli.CLI;
import libraries.maze.Maze;
import libraries.maze.EdgeData;
import libraries.maze.NodeData;
import libraries.maze.generators.rds.RDSMazeGenerator;


public class Main {
    public static void main(String[] args) {

        // ask user for basic inputs
        CLI.clear();
        int width = CLI.inputNum(String.format("Maze width (max: %s) : ", Math.floorDiv(CLI.getWidth(),2) - 10));
        CLI.clear();
        int height = CLI.inputNum(String.format("Maze height (max: %s) : ", Math.floorDiv(CLI.getHeight(),2) - 10));

        // create a new maze generator
        RDSMazeGenerator generator = new RDSMazeGenerator(width, height);

        // start generation
        generator.start(true);

        if(!CLI.inputBool("\nShow final result?", true)) return;

        Maze<NodeData, EdgeData> maze = generator.getMaze().getNormalized();

        // show final result without extra styles
        CLI.clear();
        maze.show();

        if(!CLI.inputBool("\nDo you want to solve the maze?", true))  return;

        // TODO: solve the maze

    }
}