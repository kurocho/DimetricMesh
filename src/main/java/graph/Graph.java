package graph;

import production.Production;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    private Set<Node> nodes;

    private Set<Edge> edges;

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

    public void applyProduction(Production production) {
        production.apply(this);
    }
}
