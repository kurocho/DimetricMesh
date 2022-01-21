import graph.Edge;
import graph.Graph;
import graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import production.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Production12Tests {
    Graph graph;

    @BeforeEach
    void beforeEach(){
        //given
        graph = new Graph();
        Node e = new Node("E", 1, 1, 0);
        graph.addNode(e);
    }

    @Test
    void sanityTest(){
        assertThatAmountIsCorrect("E",1);
    }

    @Test
    void shouldWorkForSimpleCase() {
        graph.applyProduction(new Production1());
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        //when
        graph.applyProduction(new Production12());
        //then
        //"I" node changed to "i"
        assertThatNodeIsAtLocationAndLevel("i", 2, 0.5f, 1);
        //"E" Nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("E", 2, 6);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 0);
        //"I" nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("I", 2, 1);
        assertThatNodeIsAtLocationAndLevel("I", 2, 1.5f, 1);
        //Nodes at level 3
        assertThatAmountAtLevelIsCorrect("E", 3, 4);
        assertThatNodeIsAtLocationAndLevel("E", 3, 0, 2);
        assertThatNodeIsAtLocationAndLevel("E", 3, 1, 2);
        assertThatNodeIsAtLocationAndLevel("E", 3, 0, 0);
        assertThatNodeIsAtLocationAndLevel("E", 3, 1, 0);
        assertThatNodeIsAtLocationAndLevel("I", 3, 0.5f, 1);
    }

    @Test
    void shouldNorWorkWhenEdgeIsRemoved() {
        graph.applyProduction(new Production1());
        graph.removeEdge(graph.getEdges().get(2));
        //when
        graph.applyProduction(new Production12());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void shouldNotWorkWhenNodeIsRemoved() {
        graph.applyProduction(new Production1());
        graph.removeNode(graph.getNodes().get(2));
        //when
        graph.applyProduction(new Production12());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void shouldNotWorkWithChangedNodeLabel() {
        graph.applyProduction(new Production1());
        Node node1 = graph.getNodes().get(2);
        List<Node> neighbours = node1.getNeighbors();
        graph.removeNode(node1);
        Node newNode = new Node("D", node1.getX(), node1.getY(), node1.getLevel());
        graph.addNode(newNode);
        for(Node n:neighbours) {
            graph.addEdge(n, newNode);
        }
        //when
        graph.applyProduction(new Production12());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void shouldNotWorkWith5NodesSquare() {
        graph.applyProduction(new Production1());
        graph.removeEdge(graph.getEdges().get(5));
        Node newNode = new Node("E", 1, 2, 1);
        graph.addNode(newNode);
        graph.addEdge(new Edge(graph.getNodes().get(2), newNode));
        graph.addEdge(new Edge(graph.getNodes().get(3), newNode));
        //when
        graph.applyProduction(new Production12());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void shouldWorkWithAdditionalEdge() {
        graph.applyProduction(new Production1());
        Node newNode = new Node("E", 3, 2, 1);
        graph.addNode(newNode);
        graph.addEdge(new Edge(graph.getNodes().get(3), newNode));
        //when
        graph.applyProduction(new Production12());
        //then
        //"I" node changed to "i"
        assertThatNodeIsAtLocationAndLevel("i", 1, 1, 1);
        //Nodes in place at level 1
        assertThatAmountAtLevelIsCorrect("E", 1, 5);
        assertThatNodeIsAtLocationAndLevel("E", 1, 0, 2);
        assertThatNodeIsAtLocationAndLevel("E", 1, 2, 2);
        assertThatNodeIsAtLocationAndLevel("E", 1, 0, 0);
        assertThatNodeIsAtLocationAndLevel("E", 1, 2, 0);
        assertThatNodeIsAtLocationAndLevel("i", 1, 1, 1);
        assertThatNodeIsAtLocationAndLevel("E", 1, 3, 2);
        //Nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("E", 2, 4);
        assertThatAmountAtLevelIsCorrect("I", 2, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 0);
        assertThatNodeIsAtLocationAndLevel("I", 2, 1, 1);
    }

    @Test
    void shouldNorWorkWithChangedNodeCoord() {
        graph.applyProduction(new Production1());
        Node node1 = graph.getNodes().get(2);
        List<Node> neighbours = node1.getNeighbors();
        graph.removeNode(node1);
        Node newNode = new Node(node1.getLabel(), -1, node1.getY(), node1.getLevel());
        graph.addNode(newNode);
        for(Node n:neighbours) {
            graph.addEdge(n, newNode);
        }
        //when
        graph.applyProduction(new Production12());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    private void assertThatNodeIsAtLocationAndLevel(String label, int level, float x, float y) {
        long count = graph.getNodes().stream().filter(n -> n.getLabel().equals(label) && n.getX() == x && n.getY() == y && n.getLevel() == level).count();
        assert count > 0;
    }

    private void assertThatAmountIsCorrect(String label, long amount){
        long count = graph.getNodes().stream().filter(n -> n.getLabel().equals(label)).count();
        assertEquals(amount,count);
    }

    private void assertThatAmountAtLevelIsCorrect(String label, long level, long amount){
        long count = graph.getNodes().stream().filter(n->n.getLabel().equals(label)).filter(n->n.getLevel() == level).count();
        assertEquals(amount,count);
    }
}
