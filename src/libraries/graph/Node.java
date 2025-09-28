package libraries.graph;

public class Node<T> {
    private T value;

    public Node(T value) {
        this.value = value;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
