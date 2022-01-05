package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    static int lastNodeId = 201160819;

    private String label;

    private final float x;

    private final float y;

    private final int level;

    private final int nodeId;

    private final List<Edge> edges = new ArrayList<>();

    public Node(String label, float x, float y, int level) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.level = level;
        this.nodeId = lastNodeId++;
    }

    public Node(Node oldNode, int level) {
        this.label = oldNode.label;
        this.x = oldNode.x;
        this.y = oldNode.y;
        this.level = level;
        this.nodeId = lastNodeId++;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getLevel() {
        return level;
    }

    public String getId() {
        return label + "|" + x + "|" + y + "|" + level + "|" + nodeId;
    }


    @Override
    public String toString() {
        return label + "|" + x + "|" + y + "|" + level + "|" + nodeId;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNeighbors() {
        List<Node> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            neighbors.add(edge.getDestination(this));
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return getId().equals(node.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, x, y, label, nodeId);
    }
}
