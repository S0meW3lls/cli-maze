package libraries.maze;

import libraries.cli.CLI;
import libraries.cli.CLIStyle;
import libraries.graph.Edge;
import libraries.graph.Graph;
import libraries.graph.Node;
import libraries.maze.data.EdgeData;
import libraries.maze.data.NodeData;

import java.util.*;

public class Maze {

    /**
     * number of changes per second
     */
    private static final int CPS = 10;

    // const used to handle junction char choosing (bitwise operation)
    private static final int NORTH = 1; // 0001
    private static final int SOUTH = 2; // 0010
    private static final int EAST = 4;  // 0100
    private static final int WEST = 8;  // 1000
    private static final char[] JUNCTION_CHARS = {
            ' ', '│', '│', '│', '─', '└', '┌', '├',
            '─', '┘', '┐', '┤', '─', '┴', '┬', '┼'
    };

    private final int width;
    private final int height;
    private String state;

    private Graph<NodeData, EdgeData> graph;
    private List<List<Node<NodeData>>> visualizationMatrix;

    public Maze(int width, int height) {

        // initialize variables
        this.width = width;
        this.height = height;
        this.state = "";

        // generate maze graph without data
        this.initializeEmptyMaze();
    }

    /**
     * Generate the maze using randomized depth-first search
     */
    public void generateWithRDS(){

        // create walls for all roads
        this.graph.getEdges().forEach(e -> e.getValue().setWall(true));

        // init support stack
        Stack<Node<NodeData>> stack = new Stack<>();

        // choose a middle top node to the start of the maze
        Node<NodeData> start = this.graph.getNodes().stream()
                .filter(n -> n.getValue().getX() == Math.ceilDiv(this.width, 2) && n.getValue().getY() == 0)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        // add first node to the stack
        stack.push(start);

        // keep cycling until stack is empty
        while(!stack.isEmpty()) {

            // get the new node to check and its unvisited neighbors
            Node<NodeData> node = stack.pop();
            
            // clear previous head marking
            this.graph.getNeighbors(node).forEach(n -> n.getValue().setRDSHead(false));
            
            // mark current node as head
            node.getValue().setRDSHead(true);
            
            // show current state
            CLI.clear();
            this.show();
            try { Thread.sleep(1000 / Maze.CPS); }
            catch (InterruptedException e) { throw new RuntimeException(e); }
            
            node.getValue().setRDSHead(false);
            node.getValue().setRDSTrail(true);
            List<Node<NodeData>> neighbors = this.graph.getNeighbors(node).stream().filter(n -> !n.getValue().isRDSVisited()).toList();

            // if it has at least 1 unvisited neighbor
            if(!neighbors.isEmpty()) {
                // push back current item to the stack to be able to backtrack to it
                stack.push(node);

                // decide a random neighbor from the list
                Node<NodeData> selected = neighbors.get((new Random()).nextInt(neighbors.size()));

                // remove wall between the 2 nodes
                Edge<EdgeData, NodeData> link = this.graph.getLinkEdge(node, selected).orElseThrow(RuntimeException::new);
                link.getValue().setWall(false);

                // mark new cell as visited and push it to the stack
                selected.getValue().setRDSVisited(true);
                node.getValue().setRDSTrail(false);
                stack.push(selected);
            }
        }
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
    public void show() { this.show(true);}

    /**
     * Generate centered entry points for the maze
     */
    public void createEntryPoints() {
        this.visualizationMatrix.getFirst().get(this.getStartIndex()).getValue().setStart(true);
        this.visualizationMatrix.getLast().get(this.getEndIndex()).getValue().setEnd(true);
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

            // append start of the maze
            builder.append("│");
            divider.append("│");

            for(int x = 0; x < this.width; x++) {

                // retrieve current node
                Node<NodeData> n = this.visualizationMatrix.get(y).get(x);

                // taking as center the junction on the right of current cell find all walls
                Edge<EdgeData, NodeData> northWall = x + 1 < this.width ?
                        this.graph.getLinkEdge(n, this.visualizationMatrix.get(y).get(x + 1)).orElse(null)
                        : null;
                Edge<EdgeData, NodeData> westWall = y + 1 < this.height ?
                        this.graph.getLinkEdge(n, this.visualizationMatrix.get(y + 1).get(x)).orElse(null)
                        : null;
                Edge<EdgeData, NodeData> southWall = y + 1 < this.height && x + 1 < this.width  ?
                        this.graph.getLinkEdge(this.visualizationMatrix.get(y + 1).get(x), this.visualizationMatrix.get(y + 1).get(x + 1)).orElse(null)
                        : null;
                Edge<EdgeData, NodeData> eastWall =  y + 1 < this.height && x + 1 < this.width ?
                        this.graph.getLinkEdge(this.visualizationMatrix.get(y).get(x + 1), this.visualizationMatrix.get(y + 1).get(x + 1)).orElse(null)
                        : null;

                // append current node and right wall if exists on the next node
                builder.append(n.getValue().toString(style));

                // append walls between cells if they exists
                builder.append(northWall == null || northWall.getValue().isWall() ? "│" : " ");
                divider.append(westWall == null || westWall.getValue().isWall() ? "─" : " ");

                // add join char
                if (y < this.height - 1 && x < this.width - 1) {

                    boolean pathUp = northWall != null && northWall.getValue().isWall();
                    boolean pathLeft = westWall != null && westWall.getValue().isWall();
                    boolean pathDown = southWall != null && southWall.getValue().isWall();
                    boolean pathRight = eastWall != null && eastWall.getValue().isWall();

                    char j = Maze.getJunctionChar(pathUp, pathDown, pathRight, pathLeft);
                    divider.append(j);
                } else if (x < this.width -1) {
                    divider.append("─");
                }
            }

            // next line both lines;
            builder.append("\n");
            divider.append("│\n");

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
            Node<NodeData> n = this.visualizationMatrix.getLast().get(i);
            Edge<EdgeData, NodeData> rightEdge = i + 1 < this.width ?
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
            Node<NodeData> n = this.visualizationMatrix.getFirst().get(i);
            Edge<EdgeData, NodeData> rightEdge = i + 1 < this.width ?
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
    private int getStartIndex() {
        return 1;
    }

    /**
     * decide the index where to place the end of the maze
     *
     * @return the index where to place the end of the maze
     */
    private int getEndIndex() {
        return this.width - 2;
    }

    /**
     * Initialize an empty graph containing
     */
    private void initializeEmptyMaze(){

        // initialize some support data
        Map<Integer, Node<NodeData>> prev_y = new HashMap<>();
        Node<NodeData> prev_x = null;

        // init the main maze graph data and support matrix
        this.graph = new Graph.Builder<NodeData, EdgeData>().withEdgeSupplier(EdgeData::new).build();
        this.visualizationMatrix = new ArrayList<>();

        // cycle the given height / width
        for (int y = 0; y < this.height; y++)
         {
            // init a support row
            List<Node<NodeData>> row = new ArrayList<>();

            for(int x = 0; x < this.width; x++){

                Node<NodeData> n = this.graph.addNode(new NodeData(x,y));

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
