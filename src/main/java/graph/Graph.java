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

    public void removeNode(Node node) {
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Node is not in graph");
        }
        node.getEdges().forEach(edge -> edge.getDestination(node).getEdges().remove(edge));
        nodes.remove(node);
    }

    public void removeEdge(Edge edge) {
        if(!edges.contains(edge)) {
            throw new IllegalArgumentException("Edge is not in graph");
        }
        edge.getStart().getEdges().remove(edge);
        edge.getEnd().getEdges().remove(edge);
        edges.remove(edge);
    }

    public void applyProduction(Production production) {
        production.apply(this);
    }
}
