package libraries.graph;

public class Edge<T, N> {
    private T value;
    private Node<N> node1;
    private Node<N> node2;

    public Edge(Node<N> node1, Node<N> node2, T value) {
        this.node1 = node1;
        this.node2 = node2;
        this.value = value;
    }

    // GETTERS / SETTERS ------------------------------------------------------

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<N> getNode1() {
        return node1;
    }

    public void setNode1(Node<N> node1) {
        this.node1 = node1;
    }

    public Node<N> getNode2() {
        return node2;
    }

    public void setNode2(Node<N> node2) {
        this.node2 = node2;
    }
}
