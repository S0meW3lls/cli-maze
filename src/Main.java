import libraries.cli.CLI;
import libraries.cli.CLIBuilder;
import libraries.cli.CLIStyle;
import libraries.cli.menu.MenuType;
import libraries.maze.Maze;
import libraries.maze.generators.MazeGenerator;
import libraries.maze.generators.rds.RDSMazeGenerator;
import libraries.maze.solvers.MazeSolver;
import libraries.maze.solvers.astar.AStarSolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    private static final Map<String, Class<? extends MazeGenerator>> GENERATORS = Map.of(
        "RDS", RDSMazeGenerator.class
    );

    private static final Map<String, Class<? extends MazeSolver>> SOLVERS = Map.of(
        "A*", AStarSolver.class
    );

    public static void main(String[] args) {
        // show logo
        CLI.clear();
        CLI.out(CLI.LOGO);

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
        CLI.out(CLI.LOGO);

        List<String> options = GENERATORS.keySet().stream().toList();
        int choice = CLI.showMenu(MenuType.NUMBERED, "Select a generator:", options);
        Class<?> selected = GENERATORS.get(options.get(choice));

        try {
            Method startUserInteraction = selected.getMethod("startUserInteraction");
            Object instance = startUserInteraction.invoke(null);

            Method start = selected.getMethod("start", boolean.class);
            start.invoke(instance, true);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Shows the solvers menu
     */
    public static void solversMenu() {
        CLI.clear();
        CLI.out(CLI.LOGO);

        List<String> options = SOLVERS.keySet().stream().toList();
        int choice = CLI.showMenu(MenuType.NUMBERED, "Select a solver:", options);
        Class<?> selected = SOLVERS.get(options.get(choice));

        try {
            Method startUserInteraction = selected.getMethod("startUserInteraction");
            Object instance = startUserInteraction.invoke(null);

            Method start = selected.getMethod("start", boolean.class);
            start.invoke(instance, true);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}