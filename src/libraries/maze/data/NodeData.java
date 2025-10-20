package libraries.maze.data;

import libraries.cli.CLIStyle;

public class NodeData {

    // CONSTANTS --------------------------------------------------------------

    public static final String STYLE_DEF = CLIStyle.apply(" ", CLIStyle.RESET);
    public static final String STYLE_START = CLIStyle.apply("●", CLIStyle.BRIGHT_GREEN, CLIStyle.BOLD);
    public static final String STYLE_END = CLIStyle.apply("●", CLIStyle.BRIGHT_BLUE, CLIStyle.BOLD);
    public static final String STYLE_RDS_VIS = CLIStyle.apply("·", CLIStyle.CYAN, CLIStyle.BOLD);
    public static final String STYLE_RDS_TRL = CLIStyle.apply("·", CLIStyle.YELLOW, CLIStyle.BOLD);
    public static final String STYLE_RDS_HEAD = CLIStyle.apply("*", CLIStyle.BRIGHT_RED, CLIStyle.BOLD);

    // MEMBERS ----------------------------------------------------------------

    // core properties
    private boolean start;
    private boolean end;
    private int x;
    private int y;

    // RDS generation properties
    private boolean rdsVisited;
    private boolean rdsTrail;
    private boolean rdsHead;

    public NodeData(int x, int y) {

        // set node position
        this.x = x;
        this.y = y;

        // set default data
        this.start = false;
        this.end = false;

        // RDS default data
        this.rdsVisited = false;
        this.rdsTrail = false;
        this.rdsHead = false;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    @Override
    public String toString() {
        return this.toString(true);
    }

    public String toString(boolean style) {

        if(this.isEnd()) return NodeData.STYLE_END;
        else if(this.isStart()) return  NodeData.STYLE_START;
        else if (style && this.isRDSHead()) return NodeData.STYLE_RDS_HEAD;
        else if (style && this.isRDSTrail()) return NodeData.STYLE_RDS_TRL;
        else if(style && this.isRDSVisited()) return  NodeData.STYLE_RDS_VIS;

        return NodeData.STYLE_DEF;
    }

    // Core properties
    public void setStart(boolean start) { this.start = start; }
    public boolean isStart() { return this.start; }

    public boolean isEnd() { return end; }
    public void setEnd(boolean end) { this.end = end; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }


    // RDS generation properties
    public boolean isRDSTrail() { return this.rdsTrail; }
    public void setRDSTrail(boolean trail) { this.rdsTrail = trail; }

    public boolean isRDSVisited() { return rdsVisited; }
    public void setRDSVisited(boolean rdsVisited) { this.rdsVisited = rdsVisited; }

    public boolean isRDSHead() { return this.rdsHead; }
    public void setRDSHead(boolean head) { this.rdsHead = head; }
}
