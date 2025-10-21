package libraries.maze.generators.rds;

import libraries.cli.CLIStyle;

public class NodeData extends libraries.maze.NodeData {

    // CONSTANTS --------------------------------------------------------------
    public static final String STYLE_VIS = CLIStyle.apply("·", CLIStyle.CYAN, CLIStyle.BOLD);
    public static final String STYLE_TRL = CLIStyle.apply("·", CLIStyle.YELLOW, CLIStyle.BOLD);
    public static final String STYLE_HEAD = CLIStyle.apply("*", CLIStyle.BRIGHT_RED, CLIStyle.BOLD);

    // MEMBERS ----------------------------------------------------------------
    private boolean visited;
    private boolean trail;
    private boolean head;

    public NodeData(int x, int y) {
        super(x, y);
        this.visited = false;
        this.trail = false;
        this.head = false;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    public String toString(boolean style) {

        if (this.isEnd()) return NodeData.STYLE_END;
        else if (this.isStart()) return NodeData.STYLE_START;
        else if (style && this.isHead()) return NodeData.STYLE_HEAD;
        else if (style && this.isTrail()) return NodeData.STYLE_TRL;
        else if (style && this.isVisited()) return NodeData.STYLE_VIS;

        return NodeData.STYLE_DEF;
    }

    public boolean isTrail() {
        return this.trail;
    }

    public void setRDSTrail(boolean trail) {
        this.trail = trail;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isHead() {
        return this.head;
    }

    public void setHead(boolean head) {
        this.head = head;
    }
}
