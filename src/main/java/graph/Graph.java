package graph;

import production.Production;

import java.util.*;

public class Graph {

    static int lastNodeId = 201160819;

    private final Set<Node> nodes;
    private final Set<Edge> edges;

    public Graph() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
    }

    public List<Node> getNeighbors(Node node, String type) {
        List<Node> result = new ArrayList<>();
        for (Node otherNode : getNodes()) {
            if (type.equals(otherNode.getLabel()) && areNeighbors(node, otherNode)) {
                result.add(otherNode);
            }
        }
        return result;
    }

    public boolean areNeighbors(Node first, Node second) {
        return edges.contains(new Edge(first, second)) || edges.contains(new Edge(second, first));
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
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

    public void removeEdge(Node startNode, Node endNode) {
        edges.remove(new Edge(startNode, endNode));
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void applyProduction(Production production) {
        production.apply(this);
    }
}
