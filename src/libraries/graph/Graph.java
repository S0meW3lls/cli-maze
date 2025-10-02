package libraries.graph;

import java.util.*;
import java.util.function.Supplier;

public class Graph<N, E> {
    private final Map<Node<N>, List<Edge<E, N>>> graph;
    private final Supplier<N> nodeSupplier;
    private final Supplier<E> edgeSupplier;

    public Graph(Supplier<N> nodeSupplier, Supplier<E> edgeSupplier) {
        this.graph = new HashMap<>();
        this.nodeSupplier = nodeSupplier;
        this.edgeSupplier = edgeSupplier;
    }

    public Graph(Builder<N,E> builder) {
        this(builder.nodeSupplier, builder.edgeSupplier);
    }

    /**
     * Adds a new node to the graph if it doesn't exist
     *
     * @param node the node to add
     * @return the created node
     */
    public Node<N> addNode(Node<N> node) {
        if(!this.graph.containsKey(node)) this.graph.put(node, new ArrayList<>());

        return node;
    }

    /**
     * Adds a new node to the graph if it doesn't exist
     *
     * @param node the node data to add to the new node
     * @return the real node instance on the graph
     */
    public Node<N> addNode(N node) {
        Node<N> n = new Node<>(node);
        return this.addNode(n);
    }

    /**
     * Adds a new node to the graph with default data given by the supplier
     *
     * @return the newly created node
     */
    public Node<N> addNode() {

        // check if a node supplier was given
        if(this.nodeSupplier == null) throw new IllegalStateException("to use addNode() you need to provide a valid node supplier");

        return this.addNode(this.nodeSupplier.get());
    }

    /**
     * Removes a node from the graph
     *
     * @param node the node to remove from the graph
     */
    public void removeNode(Node<N> node) {
        this.graph.remove(node);
    }

    /**
     * retrieve the list of neighbors of the given node
     *
     * @param node the node of which get the neighbors
     * @return the list of neighbor nodes
     */
    public List<Node<N>> getNeighbors(Node<N> node) {

        // check if the node is on the graph
        if(!this.graph.containsKey(node)) throw new IllegalArgumentException("Given node is not part of the graph");

        List<Node<N>> neighbors = new ArrayList<>();

        // cycle all edges of the node and append the 'other' node
        for(Edge<E, N> e : this.graph.get(node)){
            if(e.getNode1() != node) neighbors.add(e.getNode1());
            if(e.getNode2() != node) neighbors.add(e.getNode2());
        }

        return neighbors;
    }


    /**
     * retrieve the list of edges connected to the node
     *
     * @param node the node to get the edges
     * @return the list of edges connected to the node
     */
    public List<Edge<E, N>> getEdges(Node<N> node){

        // check if the node is on the graph
        if(!this.graph.containsKey(node)) throw new IllegalArgumentException("Given node is not part of the graph");

        // return edges of the node
        return this.graph.get(node);
    }

    /**
     * check if the 2 given nodes are adjacent
     *
     * @param node1 the first node
     * @param node2 the second node
     * @return true if the node are connected, false otherwise
     */
    public boolean areAdjacent(Node<N> node1, Node<N> node2) {
        // check if both nodes are on the graph
        if(!this.graph.containsKey(node1) || !this.graph.containsKey(node2)) return false;

        // cycle all nodes of the node1 searching for the second node
        return this.getNeighbors(node1).stream().anyMatch(n -> n == node2);
    }

    public Set<Node<N>> getNodes() {
        return this.graph.keySet();
    }

    /**
     * Adds a new edge to connect the node to another one
     *
     * @param edge the edge to add to the node
     */
    public Edge<E, N> addEdge(Edge<E, N> edge) {
        // add the connection both ways since this is undirectional graph
        this.addNode(edge.getNode1());
        this.addNode(edge.getNode2());

        // add a new edge to the graph
        this.graph.get(edge.getNode1()).add(edge);
        this.graph.get(edge.getNode2()).add(edge);

        return edge;
    }

    /**
     * Adds an edge between 2 nodes
     *
     * @param node1 the first node to connect
     * @param node2 the second node to connect
     * @param edge the edge data to attach to the edge
     * @return the created edge on the graph
     */
    public Edge<E, N> addEdge(N node1, N node2, E edge) {
        Node<N> n1 = new Node<N>(node1);
        Node<N> n2 = new Node<N>(node2);
        Edge<E, N> e = new Edge<E, N>(n1, n2, edge);

        return this.addEdge(e);
    }

    /**
     * link to nodes with an edge with default data
     * <p>Careful, both nodes must exist on the graph, otherwise null is returned
     * The edge data class is created by the supplier given on graph creation</p>
     *
     * @param node1 the first node
     * @param node2 the second node to link
     *
     * @return the newly created edge
     */
    public Edge<E, N> linkNodes(Node<N> node1, Node<N> node2){

        // check if an edge supplier was given
        if(this.edgeSupplier == null) throw new IllegalStateException("to use linkNodes() you need to provide a valid edge supplier");

        // check if both nodes are on the graph
        if(!this.graph.containsKey(node1) || !this.graph.containsKey(node2))
            throw new IllegalArgumentException("Both nodes must be already part of the graph to be able to link them");

        // create a new edge with default data
        Edge<E, N> e = new Edge<>(node1, node2, this.edgeSupplier.get());
        return this.addEdge(e);
    }

    /**
     * Removes an edge from a node
     *
     * @param edge the edge to remove
     */
    public void removeEdge(Edge<E, N> edge) {

        // remove both edge references from the node list if present
        if(this.graph.containsKey(edge.getNode1())) this.graph.get(edge.getNode1()).remove(edge);
        if(this.graph.containsKey(edge.getNode2())) this.graph.get(edge.getNode2()).remove(edge);

    }

    // BUILDER --------------------------------------------------------------------------

    public static class Builder<N,E> {
        private Supplier<N> nodeSupplier = null;
        private Supplier<E> edgeSupplier = null;

        /**
         * sets graph to have a node supplier
         *
         * @param nodeSupplier the supplier for nodes
         * @return fluently returns itself
         */
        public Builder<N,E> withNodeSupplier(Supplier<N> nodeSupplier){
            this.nodeSupplier = nodeSupplier;
            return this;
        }

        /**
         * sets graph to have an edge supplier
         *
         * @param edgeSupplier the supplier for edges
         * @return fluently returns itself
         */
        public Builder<N,E> withEdgeSupplier(Supplier<E> edgeSupplier){
            this.edgeSupplier = edgeSupplier;
            return this;
        }

        /**
         * create a new Graph object with selected properties
         *
         * @return the constructed Graph with selected options
         */
        public Graph<N, E> build() {
            return new Graph<N,E>(this);
        }
    }
}