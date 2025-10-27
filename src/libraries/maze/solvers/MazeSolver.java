package libraries.maze.solvers;

import libraries.cli.CLI;
import libraries.graph.Graph;
import libraries.maze.EdgeData;
import libraries.maze.Maze;
import libraries.maze.NodeData;
import libraries.maze.solvers.MazeSolverInterface;

/**
 * Abstract class for maze solvers.
 * <p>
 * Any class extending this abstract class should also provide a static method
 * {@code public static MazeSolver startUserInteraction()}
 * that prompts the user for necessary parameters and returns a new instance of the solver.
 */
public abstract class MazeSolver<N extends NodeData, E extends EdgeData> implements MazeSolverInterface<N, E> {

    /**
     * number of changes per second to display
     */
    public static final int CPS = 10;

    // maze dimensions
    protected int width;
    protected int height;

    // data structures
    protected Maze<N, E> maze;
    protected Graph<N, E> graph;

    // PUBLIC FUNCTIONS -----------------------------------------------------------------

    public MazeSolver(Maze<NodeData, EdgeData> maze) {
        this.width = maze.getWidth();
        this.height = maze.getHeight();
    }

    /**
     * Start maze generation
     *
     * @param visualize true if we want to show to the cli each generation step, false otherwise
     * @return the generated maze
     */
    public abstract Maze<N, E> start(boolean visualize);

    /**
     * Start maze generation
     * <p>This is just devs sugar to call {@link #start(boolean)} with visualize set to {@code false}</p>
     *
     * @return the generated maze
     */
    public Maze<N, E> start() {
        return this.start(false);
    }

    // PROTECTED FUNCTIONS --------------------------------------------------------------

    /**
     * Show the maze current state on the CLI
     *
     * @param style if we need to show all styles or only the default ones
     */
    protected void show(boolean style) {
        CLI.clear();
        if (CLI.LOGO != null) CLI.out(CLI.LOGO);

        this.maze.show(style);
        try {
            Thread.sleep(1000 / MazeSolver.CPS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show the maze current state on the CLI
     *
     * <p>This method is a dev sugar for {@link #show(boolean)} passing {@code true} to style param</p>
     */
    protected void show() {
        this.show(true);
    }

    // GETTERS / SETTERS ----------------------------------------------------------------

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Maze<N, E> getMaze() {
        return this.maze;
    }

    public void setMaze(Maze<N, E> maze) {
        this.maze = maze;
        this.graph = maze.getGraph();
    }
}
