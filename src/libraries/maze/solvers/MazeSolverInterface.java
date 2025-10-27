package libraries.maze.solvers;

import libraries.maze.EdgeData;
import libraries.maze.Maze;
import libraries.maze.NodeData;

/**
 * Interface for maze solvers.
 * <p>
 * Any class implementing this interface should also provide a static method
 * {@code public static MazeSolverInterface startUserInteraction()}
 * that prompts the user for necessary parameters and returns a new instance of the solver.
 */
public interface MazeSolverInterface<N extends NodeData, E extends EdgeData> {

    /**
     * Start maze resolution
     * <p>This is just devs sugar to call {@link #start(boolean)} with visualize set to {@code false}</p>
     *
     * @return the generated maze
     */
    Maze<N, E> start();

    /**
     * Start maze resolution
     *
     * @param visualize true if we want to show to the cli each generation step, false otherwise
     */
    Maze<N, E> start(boolean visualize);
}
