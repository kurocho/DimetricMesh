import graph.Edge;
import graph.Graph;
import graph.Node;
import org.junit.jupiter.api.Test;
import production.Production13;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Production13Tests {

    @Test
    void shouldWorkForSimpleCase() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node leftIChildOne = new Node("I", -2, -3, 0);
        Node leftIChildTwo = new Node("I", -2, -5, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -4, 0);
        Node leftIEThree = new Node("E", 0, -6, 0);
        Node rightIChild = new Node("I", 2, -4, 0);
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, leftIChildOne, leftIChildTwo, leftIEOne, leftIETwo, leftIEThree, rightIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);
        resultGraph.addEdge(leftI, leftIChildOne);
        resultGraph.addEdge(leftI, leftIChildTwo);
        resultGraph.addEdge(leftIChildOne, leftIEOne);
        resultGraph.addEdge(leftIChildOne, leftIETwo);
        resultGraph.addEdge(leftIChildTwo, leftIETwo);
        resultGraph.addEdge(leftIChildTwo, leftIEThree);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(leftIETwo, leftIEThree);
        resultGraph.addEdge(rightI, rightIChild);
        resultGraph.addEdge(rightIChild, rightIEOne);
        resultGraph.addEdge(rightIChild, rightIETwo);
        resultGraph.addEdge(rightIEOne, rightIETwo);

        resultGraph.applyProduction(new Production13());

        assertThatAmountIsCorrect(resultGraph, "E", 4);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, 0f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -2f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -4f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -6f);
        assertThatNodeIsAtLocation(resultGraph, "I", -2f, -3f);
        assertThatNodeIsAtLocation(resultGraph, "I", -2f, -5f);
        assertThatNodeIsAtLocation(resultGraph, "I", 2f, -4f);
        assertThatNodeIsAtLocation(resultGraph, "i", -1f, -1f);
        assertThatNodeIsAtLocation(resultGraph, "i", 1f, -1f);
    }

    @Test
    void shouldWorkForRotatedGraph() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("I", 2, -5, 0);
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);
        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        resultGraph.applyProduction(new Production13());

        assertThatAmountIsCorrect(resultGraph, "E", 4);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, 0f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -2f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -4f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -6f);
        assertThatNodeIsAtLocation(resultGraph, "I", 2f, -3f);
        assertThatNodeIsAtLocation(resultGraph, "I", 2f, -5f);
        assertThatNodeIsAtLocation(resultGraph, "I", -2f, -4f);
        assertThatNodeIsAtLocation(resultGraph, "i", -1f, -1f);
        assertThatNodeIsAtLocation(resultGraph, "i", 1f, -1f);
    }

    @Test
    void shouldNotWorkWhenEdgeIsRemoved() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("I", 2, -5, 0);
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);
        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
//        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        resultGraph.applyProduction(new Production13());

        assertThatAmountIsCorrect(resultGraph, "E", 6);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, 0f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -2f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -4f);
        assertThatNodeIsAtLocation(resultGraph, "E", 0f, -6f);
        assertThatNodeIsAtLocation(resultGraph, "I", 2f, -3f);
        assertThatNodeIsAtLocation(resultGraph, "I", 2f, -5f);
        assertThatNodeIsAtLocation(resultGraph, "I", -2f, -4f);
        assertThatNodeIsAtLocation(resultGraph, "i", -1f, -1f);
        assertThatNodeIsAtLocation(resultGraph, "i", 1f, -1f);
    }

    @Test
    void shouldNotWorkWhenNodeIsDifferent() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("Not_I", 2, -5, 0);      // wrong label
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);
        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        List<Edge> prevEdges = new ArrayList<>(resultGraph.getEdges());
        List<Node> prevNodes = new ArrayList<>(resultGraph.getNodes());

        resultGraph.applyProduction(new Production13());

        assertEquals(prevEdges, resultGraph.getEdges());
        assertEquals(prevNodes, resultGraph.getNodes());
    }

    @Test
    void shouldNotWorkWhenNodeIsNotPresent() {
        Graph resultGraph = new Graph();
//        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("I", 2, -5, 0);
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -6, 0);
        Stream.of(leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        List<Edge> prevEdges = new ArrayList<>(resultGraph.getEdges());
        List<Node> prevNodes = new ArrayList<>(resultGraph.getNodes());

        resultGraph.applyProduction(new Production13());

        assertEquals(prevEdges, resultGraph.getEdges());
        assertEquals(prevNodes, resultGraph.getNodes());
    }

    @Test
    void shouldNotWorkWhenNodeIsOnWrongPosition() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("I", 2, -5, 0);
        Node rightIEOne = new Node("E", 0, -1, 0);      // should be (0, -2)
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);
        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        List<Edge> prevEdges = new ArrayList<>(resultGraph.getEdges());
        List<Node> prevNodes = new ArrayList<>(resultGraph.getNodes());

        resultGraph.applyProduction(new Production13());

        assertEquals(prevEdges, resultGraph.getEdges());
        assertEquals(prevNodes, resultGraph.getNodes());
    }

    @Test
    void shouldNotWorkWhenNodeIsConnectedWithBothSides() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("I", 2, -5, 0);
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -4, 0);
        Node leftIEThree = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, leftIEThree, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);
        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIChild, leftIEThree);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(leftIETwo, leftIEThree);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        List<Edge> prevEdges = new ArrayList<>(resultGraph.getEdges());
        List<Node> prevNodes = new ArrayList<>(resultGraph.getNodes());

        resultGraph.applyProduction(new Production13());

        assertEquals(prevEdges, resultGraph.getEdges());
        assertEquals(prevNodes, resultGraph.getNodes());
    }

    @Test
    void shouldNotWorkWithAdditionalINode() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        Node additionalI = new Node("i", 0, -1, 0);
        Node rightIChildOne = new Node("I", 2, -3, 0);
        Node rightIChildTwo = new Node("I", 2, -5, 0);
        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -4, 0);
        Node rightIEThree = new Node("E", 0, -6, 0);
        Node leftIChild = new Node("I", -2, -4, 0);
        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -6, 0);
        Stream.of(mainNode, leftI, rightI, rightIChildOne, rightIChildTwo, leftIEOne, leftIETwo, rightIEThree, leftIChild, rightIEOne, rightIETwo)
                .forEach(resultGraph::addNode);

        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, additionalI);
        resultGraph.addEdge(additionalI, rightI);
        resultGraph.addEdge(leftI, leftIChild);
        resultGraph.addEdge(leftIChild, leftIEOne);
        resultGraph.addEdge(leftIChild, leftIETwo);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(rightI, rightIChildOne);
        resultGraph.addEdge(rightI, rightIChildTwo);
        resultGraph.addEdge(rightIChildOne, rightIEOne);
        resultGraph.addEdge(rightIChildOne, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIETwo);
        resultGraph.addEdge(rightIChildTwo, rightIEThree);
        resultGraph.addEdge(rightIEOne, rightIETwo);
        resultGraph.addEdge(rightIETwo, rightIEThree);

        List<Edge> prevEdges = new ArrayList<>(resultGraph.getEdges());
        List<Node> prevNodes = new ArrayList<>(resultGraph.getNodes());

        resultGraph.applyProduction(new Production13());

        assertEquals(prevEdges, resultGraph.getEdges());
        assertEquals(prevNodes, resultGraph.getNodes());
    }

    private void assertThatNodeIsAtLocation(Graph graph, String label, float x, float y) {
        long count = graph.getNodes().stream().filter(n -> n.getLabel().equals(label) && n.getX() == x && n.getY() == y).count();
        assert count > 0;
    }

    private void assertThatAmountIsCorrect(Graph graph, String label, long amount){
        long count = graph.getNodes().stream().filter(n -> n.getLabel().equals(label)).count();
        assertEquals(amount,count);
    }
}
