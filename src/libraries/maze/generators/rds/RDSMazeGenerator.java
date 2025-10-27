package libraries.maze.generators.rds;

import libraries.cli.CLI;
import libraries.graph.Edge;
import libraries.graph.Node;
import libraries.maze.Maze;
import libraries.maze.generators.MazeGenerator;

import java.util.List;
import java.util.Random;
import java.util.Stack;

public class RDSMazeGenerator extends MazeGenerator<NodeData, EdgeData> {

    public RDSMazeGenerator(int width, int height) {

        // call parent constructor
        super(width, height);

        // generate the maze
        this.setMaze(new Maze<>(width, height, NodeData::new, EdgeData::new));
    }

    /**
     * Ask for generator essential data and create a new instance of the generator
     *
     * @return a new instance of RDSMazeGenerator
     */
    public static RDSMazeGenerator startUserInteraction() {
        CLI.clear();
        int width = CLI.inputNum(String.format("Maze width (max: %s) : ", Math.floorDiv(CLI.getWidth(), 2) - 10));
        CLI.clear();
        int height = CLI.inputNum(String.format("Maze height (max: %s) : ", Math.floorDiv(CLI.getHeight(), 2) - 10));

        return new RDSMazeGenerator(width, height);
    }

    /**
     * Start maze generation
     *
     * @param visualize true if we want to show to the cli each generation step, false otherwise
     * @return the generated maze
     */
    public Maze<NodeData, EdgeData> start(boolean visualize) {

        // create walls for all roads
        this.graph.getEdges().forEach(e -> e.getValue().setWall(true));

        // init support stack
        Stack<Node<NodeData>> stack = new Stack<>();

        // choose a random node to start
        Node<NodeData> start = this.graph.getNodes().stream().toList().get((new Random()).nextInt(this.graph.getNodes().size() - 1));

        // add first node to the stack
        stack.push(start);

        // keep cycling until stack is empty
        while (!stack.isEmpty()) {

            // get the new node to check and its unvisited neighbors
            Node<NodeData> node = stack.pop();

            // clear previous head marking
            this.graph.getNeighbors(node).forEach(n -> n.getValue().setHead(false));

            // mark current node as head
            node.getValue().setHead(true);

            // show current state if necessary
            if (visualize) this.show();

            node.getValue().setHead(false);
            node.getValue().setTrail(true);
            List<Node<NodeData>> neighbors = this.graph.getNeighbors(node).stream().filter(n -> !n.getValue().isVisited()).toList();

            // if it has at least 1 unvisited neighbor
            if (!neighbors.isEmpty()) {
                // push back current item to the stack to be able to backtrack to it
                stack.push(node);

                // decide a random neighbor from the list
                Node<NodeData> selected = neighbors.get((new Random()).nextInt(neighbors.size()));

                // remove wall between the 2 nodes
                Edge<EdgeData, NodeData> link = this.graph.getLinkEdge(node, selected).orElseThrow(RuntimeException::new);
                link.getValue().setWall(false);

                // mark new cell as visited and push it to the stack
                selected.getValue().setVisited(true);
                node.getValue().setTrail(false);
                stack.push(selected);
            }
        }

        return this.maze;
    }
}
