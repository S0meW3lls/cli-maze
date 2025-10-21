package libraries.maze.generators;

import libraries.maze.EdgeData;
import libraries.maze.Maze;
import libraries.maze.NodeData;

public interface MazeGeneratorInterface<N extends NodeData, E extends EdgeData> {

    /**
     * Start maze generation
     * <p>This is just devs sugar to call {@link #start(boolean)} with visualize set to {@code false}</p>
     *
     * @return the generated maze
     */
    Maze<N, E> start();

    /**
     * Start maze generation
     *
     * @param visualize true if we want to show to the cli each generation step, false otherwise
     */
    Maze<N, E> start(boolean visualize);
}
