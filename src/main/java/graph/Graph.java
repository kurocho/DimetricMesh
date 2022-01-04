package graph;

import production.Production;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final List<Node> nodes;

    private final List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Edge> getEdges() {
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
