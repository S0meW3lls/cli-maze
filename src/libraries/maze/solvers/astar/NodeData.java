package libraries.maze.solvers.astar;

import libraries.cli.CLIStyle;

public class NodeData extends libraries.maze.NodeData {

    // CONSTANTS --------------------------------------------------------------
    public static final String STYLE_CAN = CLIStyle.apply("·", CLIStyle.GREEN);
    public static final String STYLE_PTH = CLIStyle.apply("·", CLIStyle.RED, CLIStyle.BOLD);

    // MEMBERS ----------------------------------------------------------------
    // style
    private boolean candidate;
    private boolean path;

    // score tracking
    private int g; // cost to reach this node
    private int h; // heuristic estimate to reach the end
    private int f; // complete evaluation of the node (g + h)

    public NodeData(int x, int y) {
        super(x, y);

        this.candidate = false;
        this.path = false;

        this.g = Integer.MAX_VALUE;
        this.h = Integer.MIN_VALUE;
        this.f = Integer.MAX_VALUE;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    public String toString(boolean style) {

        if (this.isEnd()) return libraries.maze.NodeData.STYLE_END;
        else if (this.isStart()) return libraries.maze.NodeData.STYLE_START;
        else if (style && this.isPath()) return String.format(NodeData.STYLE_PTH, this.path);
        else if (style && this.isCandidate()) return NodeData.STYLE_CAN;

        return libraries.maze.NodeData.STYLE_DEF;
    }

    public boolean isCandidate() { return this.candidate; }
    public void setCandidate(boolean candidate) { this.candidate = candidate; }

    public boolean isPath() { return this.path; }
    public void setPath(boolean path) { this.path = path; }


    public int getG() { return this.g; }

    public void setG(int g) {
        this.g = g;
        this.f = this.g + this.h;
    }

    public int getH() { return h; }

    public void setH(int h) {
        this.h = h;
        this.f = this.g + this.h;
    }

    public int getF() { return this.f; }
}
