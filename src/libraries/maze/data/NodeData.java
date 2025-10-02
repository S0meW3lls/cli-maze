package libraries.maze.data;

public class NodeData {

    private boolean visited;
    private int x;
    private int y;

    public NodeData(int x, int y) {

        // set node position
        this.x = x;
        this.y = y;

        // set default data
        this.visited = false;

    }

    // GETTERS / SETTERS ------------------------------------------------------

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
