package libraries.maze.solvers.astar;

import libraries.graph.Edge;
import libraries.graph.Node;
import libraries.maze.Maze;
import libraries.maze.solvers.MazeSolver;

import java.util.*;

public class AStarSolver extends MazeSolver<NodeData, EdgeData> {

    // CONST ----------------------------------------------------------------------------

    /**
     * Comparator used to define if a node has better evaluation than another
     */
    private static final Comparator<Node<NodeData>> COMPARATOR = Comparator.comparingInt(n -> n.getValue().getF());

    // MEMBERS --------------------------------------------------------------------------
    // PUBLIC FUNCTIONS -----------------------------------------------------------------

    public AStarSolver(Maze<libraries.maze.NodeData, libraries.maze.EdgeData> maze) {

        // call parent constructor
        super(maze);

        // cast base maze implementation into a* maze implementation
        this.maze = maze.castTo(AStarSolver::castNodeData, AStarSolver::castEdgeData);
        this.graph = this.maze.getGraph();

    }

    /**
     * Start maze generation
     *
     * @param visualize true if we want to show to the cli each generation step, false otherwise
     * @return the generated maze
     */
    public Maze<NodeData, EdgeData> start(boolean visualize) {

        // retrieve the start and end node
        Node<NodeData> start = this.maze.getStartNode();
        Node<NodeData> end = this.maze.getEndNode();

        // create priority queue to store nodes scores
        PriorityQueue<Node<NodeData>> openset = new PriorityQueue<>(AStarSolver.COMPARATOR);

        // create a map to store best path for each node
        Map<Node<NodeData>, Node<NodeData>> from = new HashMap<>();

        // calculate g,h and f (is set automatically) cost of the start node
        start.getValue().setG(0);
        start.getValue().setH(this.heuristic(start, end));

        // add start to openset
        openset.add(start);

        while(!openset.isEmpty()) {

            // show current progress
            if(visualize) this.show();

            Node<NodeData> node = openset.poll();
            if(node == null) break;

            // check if current node is the goal
            if(node == end) return this.reconstructPath(from, node);

            // find all reachable nodes
            List<Node<NodeData>> neighbors = this.graph.getEdges(node).stream()
                .filter((Edge<EdgeData, NodeData> e) -> !e.getValue().isWall())
                .map((Edge<EdgeData, NodeData> e) -> e.getNode1() != node ? e.getNode1() : e.getNode2())
                .toList();

            for(Node<NodeData> neighbor : neighbors) {

                // calculate the g score of the neighbor
                int candidate_g = node.getValue().getG() + this.distance(node, neighbor);

                // if the founded score is better than previous one
                if(candidate_g < neighbor.getValue().getG()) {

                    // track the path and update the g/h/f factor
                    from.put(neighbor, node);
                    neighbor.getValue().setG(candidate_g);
                    neighbor.getValue().setH(this.heuristic(neighbor, end));

                    // if not present add the neighbor to the openset
                    if(!openset.contains(neighbor)) {
                        neighbor.getValue().setCandidate(true);
                        openset.add(neighbor);
                    }

                }

            }
        }

        // error
        return null;
    }

    // PRIVATE FUNCTIONS ----------------------------------------------------------------

    /**
     * Reconstruct path of the maze
     *
     * @param from the list containing all possible paths
     * @param node the end node
     * @return the maze with highlighted path
     */
    private Maze<NodeData, EdgeData> reconstructPath(Map<Node<NodeData>,Node<NodeData>> from, Node<NodeData> node) {

        while (from.containsKey(node)) {
            node.getValue().setPath(true);
            node = from.get(node);
            this.show();
        }

        // return complete maze
        return this.maze;
    }

    /**
     * Return the weight of passing from current note to the given neighbor
     *
     * @param current the first node
     * @param neighbor the second node
     * @return the calculated weight
     */
    private int distance(Node<NodeData> current, Node<NodeData> neighbor ) {
        // since this is always an unweighted graph return always standard weight to pass from a node to another
        return 1;
    }

    /**
     * heuristic function used to estimate cost from node to the end, using taxicab geometry
     *
     * @param node the start node
     * @param end the end node
     * @return the estimate cost to reach end node from start node
     */
    private int heuristic(Node<NodeData> node, Node<NodeData> end) {
        return Math.abs(node.getValue().getX() - end.getValue().getX()) + Math.abs(node.getValue().getY() - end.getValue().getY());
    }

    // STATIC FUNCTIONS ----------------------------------------------------------------

    /**
     * Cast a standard NodeData into an A* NodeData
     *
     * @param n the NodeData to cast
     * @return the cast NodeData
     */
    public static NodeData castNodeData(libraries.maze.NodeData n) {

        // clone only main data
        NodeData nn = new NodeData(n.getX(), n.getY());
        nn.setStart(n.isStart());
        nn.setEnd(n.isEnd());

        return nn;
    }

    /**
     * Cast a standard EdgeData into an A* EdgeData
     *
     * @param e the EdgeData to cast
     * @return the cast EdgeData
     */
    public static EdgeData castEdgeData(libraries.maze.EdgeData e) {

        // clone only main data
        EdgeData ee = new EdgeData();
        ee.setWall(e.isWall());

        return ee;
    }

}
