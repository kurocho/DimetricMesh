package graph;

import java.util.Objects;

public class Edge {

    private final Node start;

    private final Node end;

    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;

        start.getEdges().add(this);
        end.getEdges().add(this);
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public String getId() {
        return start.getId() + "-" + end.getId();
    }

    public Node getDestination(Node from) {
        if(start != from && end != from) {
            throw new IllegalArgumentException("Edge not connected with given node");
        } else if(start == from) {
            return end;
        } else {
            return start;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;
        return (getStart().equals(edge.getStart()) && getEnd().equals(edge.getEnd()))
                || (getStart().equals(edge.getEnd()) && getEnd().equals(edge.getStart()));
    }
}
