import libraries.cli.CLI;
import libraries.cli.CLIBuilder;
import libraries.cli.CLIStyle;
import libraries.cli.menu.MenuType;
import libraries.maze.Maze;
import libraries.maze.EdgeData;
import libraries.maze.NodeData;
import libraries.maze.generators.rds.RDSMazeGenerator;
import libraries.maze.solvers.astar.AStarSolver;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        mainMenu();
    }

    /**
     * Shows the main menu
     */
    public static void mainMenu() {

        // show logo
        CLI.clear();
        showLogo();

        // show main menu
        int choice = CLI.showMenu(MenuType.NUMBERED, "Hello! What would you like to do? Choose the option you prefer:", List.of("Generators", "Solvers"));
        if(choice == 0) generatorsMenu();
        else if (choice == 1) solversMenu();
    }

    /**
     * Shows the generators menu
     */
    public static void generatorsMenu() {
        CLI.clear();
        showLogo();

        int choice = CLI.showMenu(MenuType.NUMBERED, "Select a generator:", List.of("RDS"));
        if (choice == 0) runRDSGenerator();

    }

    /**
     * Shows the solvers menu
     */
    public static void solversMenu() {
        CLI.clear();
        showLogo();

        int choice = CLI.showMenu(MenuType.NUMBERED, "Select a solver:", List.of("A*"));
        if (choice == 0) runAStarSolver();

    }

    /**
     * Run the RDS generator
     */
    public static void runRDSGenerator() {
        CLI.clear();
        int width = CLI.inputNum(String.format("Maze width (max: %s) : ", Math.floorDiv(CLI.getWidth(), 2) - 10));
        CLI.clear();
        int height = CLI.inputNum(String.format("Maze height (max: %s) : ", Math.floorDiv(CLI.getHeight(), 2) - 10));

        RDSMazeGenerator generator = new RDSMazeGenerator(width, height);
        generator.start(true);

        if (CLI.inputBool("\nShow final result?", true)) {
            CLI.clear();
            generator.getMaze().getNormalized().show();
        }
    }

    /**
     * Run A* solver
     */
    public static void runAStarSolver() {
        CLI.clear();
        int width = CLI.inputNum(String.format("Maze width (max: %s) : ", Math.floorDiv(CLI.getWidth(), 2) - 10));
        CLI.clear();
        int height = CLI.inputNum(String.format("Maze height (max: %s) : ", Math.floorDiv(CLI.getHeight(), 2) - 10));

        RDSMazeGenerator generator = new RDSMazeGenerator(width, height);
        generator.start(false);
        Maze<NodeData, EdgeData> maze = generator.getMaze().getNormalized();

        AStarSolver solver = new AStarSolver(maze);
        solver.start(true);
    }

    /**
     * Shows the logo
     */
    private static void showLogo() {
        CLIBuilder builder = new CLIBuilder();
        builder.addRow(CLIStyle.apply(
            "    ___       ___       ___       ___       ___       ___       ___   \n" +
            "   /\\  \\     /\\__\\     /\\  \\     /\\__\\     /\\  \\     /\\  \\     /\\  \\  \n" +
            "  /::\\  \\   /:/  /    _\\:\\  \\   /::L_L_   /::\\  \\   _\\:\\  \\   /::\\  \\ \n" +
            " /:/\\:\\__\\ /:/__/    /\\/::\\__\\ /:/L:\\__\\ /::\\:\\__\\ /::::\\__\\ /::\\:\\__\\\n" +
            " \\:\\ \\/__/ \\:\\  \\    \\::/\\/__/ \\/_/:/  / \\/\\::/  / \\::;;/__/ \\:\\:\\/  /\n" +
            "  \\:\\__\\    \\:\\__\\    \\:\\__\\     /:/  /    /:/  /   \\:\\__\\    \\:\\/  / \n" +
            "   \\/__/     \\/__/     \\/__/     \\/__/     \\/__/     \\/__/     \\/__/  \n", CLIStyle.BRIGHT_GREEN)
        )
        .addRow(CLIStyle.apply("A tool for visualizing maze generation and solving algorithms", CLIStyle.GREEN))
        .addEmptyRow()
        .addEmptyRow()
        .addEmptyRow()
        .show();
    }
}