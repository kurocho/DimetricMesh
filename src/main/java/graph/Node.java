package graph;

public class Node {

    private String label;

    private final float x;

    private final float y;

    private final int level;

    private final int nodeId;

    public Node(String label, float x, float y, int level) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.level = level;
        this.nodeId = Graph.lastNodeId++;
    }

    public Node(Node oldNode, int level) {
        this.label = oldNode.label;
        this.x = oldNode.x;
        this.y = oldNode.y;
        this.level = level;
        this.nodeId = Graph.lastNodeId++;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

//        return hashCode() == node.hashCode();
        return getId().equals(node.getId());
    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + level;
        result = 31 * result + nodeId;
        return result;
    }
}
