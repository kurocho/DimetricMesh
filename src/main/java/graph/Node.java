package graph;

public class Node {

    private String label;

    private final float x;

    private final float y;

    private final int level;

    public Node(String label, float x, float y, int level) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.level = level;
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
        return label + x + y + level;
    }

}
