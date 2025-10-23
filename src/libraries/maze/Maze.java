package libraries.maze;

import libraries.cli.CLI;
import libraries.cli.CLIStyle;
import libraries.graph.Edge;
import libraries.graph.Graph;
import libraries.graph.Node;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Maze<N extends NodeData, E extends EdgeData> {

    // const used to handle junction char choosing (bitwise operation)
    private static final int NORTH = 1; // 0001
    private static final int SOUTH = 2; // 0010
    private static final int EAST = 4;  // 0100
    private static final int WEST = 8;  // 1000
    private static final char[] JUNCTION_CHARS = {
            ' ', '│', '│', '│', '─', '└', '┌', '├',
            '─', '┘', '┐', '┤', '─', '┴', '┬', '┼'
    };

    // maze dimensions
    private final int width;
    private final int height;

    // edge / node suppliers
    private final BiFunction<Integer, Integer, N> nodeSupplier;
    private final Supplier<E> edgeSupplier;

    // main data structures graph and support matrix for visualization purpose
    private Graph<N, E> graph;
    private List<List<Node<N>>> visualizationMatrix;


    public Maze(int width, int height, BiFunction<Integer, Integer, N> nodeSupplier, Supplier<E> edgeSupplier, boolean deferInit) {

        // pre checks
        if (width <= 0) throw new IllegalArgumentException("width must be greater than zero");
        if (height <= 0) throw new IllegalArgumentException("height must be greater than zero");

        // initialize variables
        this.width = width;
        this.height = height;

        // set node/edge classes
        this.nodeSupplier = nodeSupplier;
        this.edgeSupplier = edgeSupplier;

        // generate maze graph without data
        if(!deferInit) this.initializeEmptyMaze();
    }

    public Maze(int width, int height, BiFunction<Integer, Integer, N> nodeSupplier, Supplier<E> edgeSupplier) {
        this(width, height, nodeSupplier, edgeSupplier, false);
    }

    /**
     * Shows current maze state during algorithm
     */
    public void show(boolean style) {

        // init the maze output
        StringBuilder builder = new StringBuilder();

        CLI.out(CLIStyle.apply(String.format("Maze Generator (%dx%d)\n\n", this.height, this.width), CLIStyle.BOLD));

        // generate the maze
        this.generateMazeTopRow(builder, style);
        this.generateMazeBody(builder, style);
        this.generateMazeBottomRow(builder, style);

        // output the maze
        CLI.out(builder);
    }

    /**
     * Shows the current maze state during algorithm
     * <p>This is a convenience method to call maze visualization applying styles by default</p>
     */
    public void show() { this.show(true);}

    /**
     * Cast a specif maze to a base maze implementation
     *
     * @return the normalized maze
     */
    public Maze<NodeData, EdgeData> getNormalized() {

        // retrieve maze graph
        Graph<N, E> graph = this.graph;

        // create standard objects
        Maze<NodeData, EdgeData> stdMaze = new Maze<>(this.width, this.height, null, null, true);
        Graph<NodeData, EdgeData> stdGraph = new Graph.Builder<NodeData, EdgeData>().build();

        // create support data structure
        Stack<Node<N>> stack = new Stack<>();
        Set<Node<N>> visited = new HashSet<>();

        // create first node and traverse the given graph with DFS
        Node<N> first = graph.getNodes().stream().toList().getFirst();
        stack.push(first);

        while (!stack.isEmpty()) {

            // get the new node to check and its connections
            Node<N> node = stack.pop();

            if(!visited.contains(node)) {

                // add to visited set the node
                visited.add(node);

                // getNormalized the node
                Node<NodeData> stdNode = new Node<>(NodeData.getNormalized(node.getValue()));

                // cycle each edge of the node
                for(Edge<E,N> e: graph.getEdges(node)){
                    Node<N> other = e.getNode1() == node ? e.getNode2() : e.getNode1();

                    // getNormalized the other node and add it to graph
                    Node<NodeData> stdOther = new Node<>(NodeData.getNormalized(other.getValue()));

                    // getNormalized connection between nodes (nodes will be auto added to graph)
                    stdGraph.addEdge(new Edge<>(
                            e.getNode1() == node ? stdNode : stdOther,
                            e.getNode2() == node ? stdNode : stdOther,
                            EdgeData.getNormalized(e.getValue())
                    ));

                    // push the new node
                    stack.push(other);
                }
            }
        }

        // replace graph
        stdMaze.graph = stdGraph;

        // return base maze
        return stdMaze;
    }

    // PRIVATE METHODS --------------------------------------------------------

    /**
     * Generate maze main body based on current graph state
     *
     * @param builder the builder object containing the maze
     * @param style if true show also cell style, otherwise always output default style
     */
    private void generateMazeBody(StringBuilder builder, boolean style) {

        for(int y = 0; y < this.height; y++) {

            StringBuilder divider = new StringBuilder();
            Edge<E, N> startWall = y + 1 < this.height ?
                    this.graph.getLinkEdge(this.visualizationMatrix.get(y).getFirst(), this.visualizationMatrix.get(y + 1).getFirst()).orElse(null)
                    : null;
            Edge<E, N> endWall = y + 1 < this.height ?
                    this.graph.getLinkEdge(this.visualizationMatrix.get(y).getLast(), this.visualizationMatrix.get(y + 1).getLast()).orElse(null)
                    : null;

            // append start of the maze
            builder.append("│");

            divider.append(startWall != null && startWall.getValue().isWall() ? "├" : "│");

            for(int x = 0; x < this.width; x++) {

                // retrieve current node
                Node<N> n = this.visualizationMatrix.get(y).get(x);

                // taking as center the junction on the right of current cell find all walls
                Edge<E, N> northWall = x + 1 < this.width ?
                        this.graph.getLinkEdge(n, this.visualizationMatrix.get(y).get(x + 1)).orElse(null)
                        : null;
                Edge<E, N> westWall = y + 1 < this.height ?
                        this.graph.getLinkEdge(n, this.visualizationMatrix.get(y + 1).get(x)).orElse(null)
                        : null;
                Edge<E, N> southWall = y + 1 < this.height && x + 1 < this.width ?
                        this.graph.getLinkEdge(this.visualizationMatrix.get(y + 1).get(x), this.visualizationMatrix.get(y + 1).get(x + 1)).orElse(null)
                        : null;
                Edge<E, N> eastWall = y + 1 < this.height && x + 1 < this.width ?
                        this.graph.getLinkEdge(this.visualizationMatrix.get(y).get(x + 1), this.visualizationMatrix.get(y + 1).get(x + 1)).orElse(null)
                        : null;

                // append current node and right wall if exists on the next node
                builder.append(n.getValue().toString(style));

                // append walls between cells if they exists
                builder.append(northWall == null || northWall.getValue().isWall() ? "│" : " ");
                divider.append(westWall == null || westWall.getValue().isWall() ? "─" : " ");

                // add join char
                if (y < this.height - 1 && x < this.width - 1) {
                    char j = Maze.getJunctionChar(
                    northWall != null && northWall.getValue().isWall(),
                    southWall != null && southWall.getValue().isWall(),
                    eastWall != null && eastWall.getValue().isWall(),
                    westWall != null && westWall.getValue().isWall()
                    );
                    divider.append(j);
                } else if (x < this.width -1) {
                    divider.append("─");
                }
            }

            // next line both lines;
            builder.append("\n");
            divider.append(endWall != null && endWall.getValue().isWall() ? "┤" : "│");
            divider.append("\n");

            // append to main object the divider row created by cycling previous nodes if we are not on last row since last row is handled after
            if(y < this.height - 1) builder.append(divider);
        }
    }

    /**
     * Generate last row of the maze
     *
     * @param builder the builder object containing the maze
     * @param style if true show also cell style, otherwise always output default style
     */
    private void generateMazeBottomRow(StringBuilder builder, boolean style) {
        // create last line
        builder.append("╰");

        for(int i = 0; i < this.width; i++) {
            // retrieve current node and right wall
            Node<N> n = this.visualizationMatrix.getLast().get(i);
            Edge<E, N> rightEdge = i + 1 < this.width ?
                    this.graph.getLinkEdge(n, this.visualizationMatrix.getLast().get(i + 1)).orElse(null)
                    : null;

            // print maze edge or entrance
            builder.append(!n.getValue().isEnd() ? "─" : " ");

            // print correct join char
            if(i < this.width - 1) builder.append(rightEdge != null && rightEdge.getValue().isWall() ? '┴' : '─');
        }

        builder.append("╯\n");
    }

    /**
     * Generate first row of the maze
     *
     * @param builder the builder object containing maze
     * @param style if true show also cell style, otherwise always output default style
     */
    private void generateMazeTopRow(StringBuilder builder, boolean style) {
        builder.append("╭");

        for(int i = 0; i < this.width; i++) {
            // retrieve current node and right wall
            Node<N> n = this.visualizationMatrix.getFirst().get(i);
            Edge<E, N> rightEdge = i + 1 < this.width ?
                    this.graph.getLinkEdge(n, this.visualizationMatrix.getFirst().get(i + 1)).orElse(null)
                    : null;

            // print maze edge or entrance
            builder.append(!n.getValue().isStart() ? "─" : " ");

            // print correct join char
            if(i < this.width - 1) builder.append(rightEdge != null && rightEdge.getValue().isWall() ? "┬" : "─");
        }

        builder.append("╮\n");
    }

    /**
     * decide the index where to place the start of the maze
     *
     * @return the index where to place the start of the maze
     */
    private int getStartIndex() { return 1; }

    /**
     * decide the index where to place the end of the maze
     *
     * @return the index where to place the end of the maze
     */
    private int getEndIndex() { return this.width - 2; }

    /**
     * Initialize an empty graph containing
     */
    private void initializeEmptyMaze(){

        // initialize some support data
        Map<Integer, Node<N>> prev_y = new HashMap<>();
        Node<N> prev_x = null;

        // init the main maze graph data and support matrix
        this.graph = new Graph.Builder<N, E>().withEdgeSupplier(this.edgeSupplier).build();
        this.visualizationMatrix = new ArrayList<>();

        // cycle the given height / width
        for (int y = 0; y < this.height; y++)
         {
            // init a support row
             List<Node<N>> row = new ArrayList<>();

            for(int x = 0; x < this.width; x++){

                Node<N> n = this.graph.addNode(this.nodeSupplier.apply(x, y));

                // add connection between upper node and the current one
                if(y > 0) this.graph.linkNodes(n, prev_y.get(x));

                // add connections between the left node and the current one
                if(x > 0) this.graph.linkNodes(n, prev_x);

                // save the node ref also into a support matrix to easy visualization
                row.add(n);

                // store previous values
                prev_x = n;
                prev_y.put(x, n);
            }

            // add entire row
            this.visualizationMatrix.add(row);
        }

        // create main entry points
        this.visualizationMatrix.getFirst().get(this.getStartIndex()).getValue().setStart(true);
        this.visualizationMatrix.getLast().get(this.getEndIndex()).getValue().setEnd(true);

    }

    // STATIC METHODS ---------------------------------------------------------

    /**
     * Decide Maze junction character during maze visualization using bitwise operation
     *
     * @param hasNorth true if north wall is up, false otherwise
     * @param hasSouth true if south wall is up, false otherwise
     * @param hasEast true if east wall is up, false otherwise
     * @param hasWest true if west wall is up, false otherwise
     * @return the junction character
     */
    private static char getJunctionChar(boolean hasNorth, boolean hasSouth, boolean hasEast, boolean hasWest) {
        int index = 0;

        if (hasNorth) index |= Maze.NORTH;
        if (hasSouth) index |= Maze.SOUTH;
        if (hasEast)  index |= Maze.EAST;
        if (hasWest)  index |= Maze.WEST;

        return Maze.JUNCTION_CHARS[index];
    }

    // GETTERS / SETTERS ------------------------------------------------------

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Graph<N, E> getGraph() {
        return graph;
    }
}
