package graph;

import production.Production;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    static int lastNodeId = 201160819;

    private final Set<Node> nodes;
    private final Set<Edge> edges;

    public Graph() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addEdge(Node startNode, Node endNode) {
        edges.add(new Edge(startNode, endNode));
    }

    public void applyProduction(Production production) {
        production.apply(this);
    }
}
