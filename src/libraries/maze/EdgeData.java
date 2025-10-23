package libraries.maze;

public class EdgeData {

    // core properties
    boolean wall = false;


    /**
     * Cast a specific edge to a generic edge implementation
     *
     * @param edge the edge to cast
     * @return the base node implementation
     */
    public static EdgeData getNormalized(EdgeData edge) {

        // create a base object
        EdgeData e = new EdgeData();
        e.wall = edge.isWall();

        // return it
        return e;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    // Core properties
    public boolean isWall() {
        return wall;
    }
    public void setWall(boolean wall) {
        this.wall = wall;
    }
}
