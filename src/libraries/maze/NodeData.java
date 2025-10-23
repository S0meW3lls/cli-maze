package libraries.maze;

import libraries.cli.CLIStyle;

public class NodeData {

    // CONSTANTS --------------------------------------------------------------
    public static final String STYLE_DEF = CLIStyle.apply(" ", CLIStyle.RESET);
    public static final String STYLE_START = CLIStyle.apply("●", CLIStyle.BRIGHT_GREEN, CLIStyle.BOLD);
    public static final String STYLE_END = CLIStyle.apply("●", CLIStyle.BRIGHT_BLUE, CLIStyle.BOLD);

    // MEMBERS ----------------------------------------------------------------
    private boolean start;
    private boolean end;
    private int x;
    private int y;

    public NodeData(int x, int y) {

        // set node position
        this.x = x;
        this.y = y;

        // set extra data
        this.start = false;
        this.end = false;

    }

    // STATIC METHODS ---------------------------------------------------------

    /**
     * Cast a specific node to a generic node implementation
     *
     * @param node the node to cast
     * @return the base node implementation
     */
    public static NodeData getNormalized(NodeData node) {

        // create a base object
        NodeData n = new NodeData(node.getX(), node.getY());
        n.start = node.isStart();
        n.end = node.isEnd();

        // return it
        return n;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    @Override
    public String toString() {
        return this.toString(true);
    }

    public String toString(boolean style) {

        if(this.isEnd()) return NodeData.STYLE_END;
        else if(this.isStart()) return  NodeData.STYLE_START;

        return NodeData.STYLE_DEF;
    }

    public void setStart(boolean start) { this.start = start; }
    public boolean isStart() { return this.start; }

    public boolean isEnd() { return end; }
    public void setEnd(boolean end) { this.end = end; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

}
