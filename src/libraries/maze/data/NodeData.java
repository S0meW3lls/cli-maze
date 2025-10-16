package libraries.maze.data;

import libraries.cli.CLIStyle;

public class NodeData {

    // CONSTANTS --------------------------------------------------------------

    public static final String STYLE_DEF = CLIStyle.apply(" ", CLIStyle.RESET);
    public static final String STYLE_START = CLIStyle.apply("●", CLIStyle.BRIGHT_GREEN, CLIStyle.BOLD);
    public static final String STYLE_END = CLIStyle.apply("●", CLIStyle.BRIGHT_BLUE, CLIStyle.BOLD);
    public static final String STYLE_VIS = CLIStyle.apply("·", CLIStyle.CYAN, CLIStyle.BOLD);
    public static final String STYLE_TRL = CLIStyle.apply("·", CLIStyle.YELLOW, CLIStyle.BOLD);

    // MEMBERS ----------------------------------------------------------------

    private boolean visited;
    private boolean start;
    private boolean end;
    private boolean trail;

    private int x;
    private int y;

    public NodeData(int x, int y) {

        // set node position
        this.x = x;
        this.y = y;

        // set default data
        this.visited = false;
        this.start = false;
        this.end = false;
        this.trail = false;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    @Override
    public String toString() {
        return this.toString(true);
    }

    public String toString(boolean style) {

        if(this.isEnd()) return NodeData.STYLE_END;
        else if(this.isStart()) return  NodeData.STYLE_START;
        else if (style && this.isTrail()) return NodeData.STYLE_TRL;
        else if(style && this.isVisited()) return  NodeData.STYLE_VIS;

        return NodeData.STYLE_DEF;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isStart() {
        return this.start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isTrail() { return this.trail; }

    public void setTrail(boolean trail) { this.trail = trail; }

    public boolean isVisited() { return visited; }

    public void setVisited(boolean visited) { this.visited = visited; }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }
}
