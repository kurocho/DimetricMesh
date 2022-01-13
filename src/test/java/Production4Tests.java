import graph.Graph;
import graph.GraphUtil;
import graph.Node;
import org.junit.jupiter.api.Test;
import production.Production4;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Production4Tests {
    @Test
    void shouldWorkForSimpleCase() {
        Graph resultGraph = new Graph();
        Node topLeft = new Node("E", -1, 1, 0);
        Node topRight = new Node("E", 1, 1, 0);
        Node bottomLeft = new Node("E", -1, -1, 0);
        Node bottomRight = new Node("E", 1, -1, 0);
        Node additional = new Node("E", 0, 1, 0);
        Node iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, additional, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, additional);
        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, bottomLeft);
        resultGraph.addEdge(additional, topRight);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        resultGraph.applyProduction(new Production4());

        List<Node> newNodes = resultGraph.getNodes().stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList());

        assertEquals(8, newNodes.size());
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, -1f, -1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, -1f, 1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, 1f, 1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, 1f, -1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "I", 1, -0.5f, 0f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "I", 1, 0.5f, 0f);
    }

    @Test
    void shouldWorkForRotatedGraph() {
        Graph resultGraph = new Graph();
        Node topLeft = new Node("E", -1, 1, 0);
        Node topRight = new Node("E", 1, 1, 0);
        Node bottomLeft = new Node("E", -1, -1, 0);
        Node bottomRight = new Node("E", 1, -1, 0);
        Node additional = new Node("E", -1, 0, 0);
        Node iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, additional, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, topRight);
        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, additional);
        resultGraph.addEdge(bottomLeft, additional);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        resultGraph.applyProduction(new Production4());
        List<Node> newNodes = resultGraph.getNodes().stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList());
        assertEquals(8, newNodes.size());
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, -1f, -1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, -1f, 1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, 1f, 1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, 1f, -1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "E", 1, 1f, -1f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "I", 1, 0f, 0.5f);
        assertThatNodeIsAtLocationAndLevel(resultGraph, "I", 1, 0f, -0.5f);
    }

    @Test
    void shouldNotWorkWhenEdgeIsRemoved() {
        Graph resultGraph = new Graph();
        Node topLeft = new Node("E", -1, 1, 0);
        Node topRight = new Node("E", 1, 1, 0);
        Node bottomLeft = new Node("E", -1, -1, 0);
        Node bottomRight = new Node("E", 1, -1, 0);
        Node additional = new Node("E", 0, 1, 0);
        Node iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, additional, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, bottomLeft);
        resultGraph.addEdge(additional, topRight);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        resultGraph.applyProduction(new Production4());
        List<Node> newNodes = resultGraph.getNodes().stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList());
        assertEquals(0, newNodes.size());
    }

    @Test
    void shouldNotWorkWhenNodeIsDifferent() {
        Graph resultGraph = new Graph();
        Node topLeft = new Node("E", -1, 1, 0);
        Node topRight = new Node("E", 1, 1, 0);
        Node bottomLeft = new Node("I", -1, -1, 0);
        Node bottomRight = new Node("E", 1, -1, 0);
        Node additional = new Node("E", 0, 1, 0);
        Node iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, additional, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, additional);
        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, bottomLeft);
        resultGraph.addEdge(additional, topRight);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        resultGraph.applyProduction(new Production4());
        List<Node> newNodes = resultGraph.getNodes().stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList());
        assertEquals(0, newNodes.size());
    }

    @Test
    void shouldNotWorkWhenNodeIsNotPresent() {
        Graph resultGraph = new Graph();
        Node topLeft = new Node("E", -1, 1, 0);
        Node topRight = new Node("E", 1, 1, 0);
        Node bottomLeft = new Node("E", -1, -1, 0);
        Node bottomRight = new Node("E", 1, -1, 0);
        Node iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, topRight);
        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, bottomLeft);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        resultGraph.applyProduction(new Production4());
        List<Node> newNodes = resultGraph.getNodes().stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList());
        assertEquals(0, newNodes.size());
    }

    @Test
    void shouldNotWorkWhenNodeIsNotExactlyInTheMiddle() {
        Graph resultGraph = new Graph();
        Node topLeft = new Node("E", -1, 1, 0);
        Node topRight = new Node("E", 1, 1, 0);
        Node bottomLeft = new Node("E", -1, -1, 0);
        Node bottomRight = new Node("E", 1, -1, 0);
        Node additional = new Node("E", 10, 5, 0);
        Node iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, additional, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, additional);
        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, bottomLeft);
        resultGraph.addEdge(additional, topRight);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        resultGraph.applyProduction(new Production4());

        List<Node> newNodes = resultGraph.getNodes().stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList());

        assertEquals(0, newNodes.size());
    }

    private void assertThatNodeIsAtLocationAndLevel(Graph graph, String label, int level, float x, float y) {
        long count = graph.getNodes().stream().filter(n -> n.getLabel().equals(label) && n.getX() == x && n.getY() == y && n.getLevel() == level).count();
        assert count > 0;
    }
}
