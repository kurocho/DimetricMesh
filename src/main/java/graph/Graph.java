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
        edge.getStart().getEdges().add(edge);
        edge.getEnd().getEdges().add(edge);
    }

    public void addEdge(Node startNode, Node endNode) {
        addEdge(new Edge(startNode, endNode));
    }

    public void removeNode(Node node) {
        if (!nodes.contains(node)) {
            throw new IllegalArgumentException("Node is not in graph");
        }

        node.getEdges().forEach(edge -> {
            final Node destination = edge.getDestination(node);
            if (node != destination) {
                destination.getEdges().remove(edge);
            }
            edges.remove(edge);
        });
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
