import graph.Edge;
import graph.Graph;
import graph.GraphUtil;
import graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import production.Production1;
import production.Production3;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Production3Tests {

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
    void applyForIsomorphicGraphTest(){
        graph.applyProduction(new Production1());
        //when
        graph.applyProduction(new Production3());
        //then
        //"I" node changed to "i"
        assertThatNodeIsAtLocationAndLevel("i", 1, 1, 1);
        //"E" Nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("E", 2, 9);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 2);
        //"I" nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("I", 2, 4);
        assertThatNodeIsAtLocationAndLevel("I", 2, 0.5f, 0.5f);
        assertThatNodeIsAtLocationAndLevel("I", 2, 1.5f, 0.5f);
        assertThatNodeIsAtLocationAndLevel("I", 2, 0.5f, 1.5f);
        assertThatNodeIsAtLocationAndLevel("I", 2, 1.5f, 1.5f);
    }

    @Test
    void notApplyForGraphWithRemovedEdge(){
        createGraphWithRemovedEdge();
        //when
        graph.applyProduction(new Production3());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void notApplyForGraphWithRemovedNode() {
        createGraphWithRemovedNode();
        //when
        graph.applyProduction(new Production3());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void notApplyForGraphWith5NodesSquare() {
        createGraphWith5NodesSquare();
        //when
        graph.applyProduction(new Production3());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void applyForGraphWithAddictionalEdge() {
        createGraphWithAddictionalEdge();
        //when
        graph.applyProduction(new Production3());
        //then
        //"I" node changed to "i"
        assertThatNodeIsAtLocationAndLevel("i", 1, 1, 1);
        //"E" Nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("E", 2, 9);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 0);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 1);
        assertThatNodeIsAtLocationAndLevel("E", 2, 0, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 1, 2);
        assertThatNodeIsAtLocationAndLevel("E", 2, 2, 2);
        //"I" nodes in place at level 2
        assertThatAmountAtLevelIsCorrect("I", 2, 4);
        assertThatNodeIsAtLocationAndLevel("I", 2, 0.5f, 0.5f);
        assertThatNodeIsAtLocationAndLevel("I", 2, 1.5f, 0.5f);
        assertThatNodeIsAtLocationAndLevel("I", 2, 0.5f, 1.5f);
        assertThatNodeIsAtLocationAndLevel("I", 2, 1.5f, 1.5f);
        //Addictional Node still in place
        assertThatNodeIsAtLocationAndLevel("E", 1, 3, 2);
        assertThatEdgeExists("E", 1, 3, 2, "E", 1, 2, 2);

    }

    @Test
    void notApplyForGraphWithChangedNodeLabel() {
        createGraphWithChangedLabel();
        //when
        graph.applyProduction(new Production3());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    @Test
    void notApplyForGraphWithChangedNodeCoord() {
        createGraphWithChangedCoord();
        //when
        graph.applyProduction(new Production3());
        //then
        assertThatAmountAtLevelIsCorrect("E", 2, 0);
    }

    private void createGraphWithRemovedEdge() {
        graph.applyProduction(new Production1());
        graph.removeEdge(graph.getEdges().get(2));
    }

    private void createGraphWithRemovedNode() {
        graph.applyProduction(new Production1());
        graph.removeNode(graph.getNodes().get(2));
    }

    private void createGraphWith5NodesSquare() {
        graph.applyProduction(new Production1());
        graph.removeEdge(graph.getEdges().get(5));
        Node newNode = new Node("E", 1, 2, 1);
        graph.addNode(newNode);
        graph.addEdge(new Edge(graph.getNodes().get(2), newNode));
        graph.addEdge(new Edge(graph.getNodes().get(3), newNode));
    }

    private void createGraphWithAddictionalEdge() {
        graph.applyProduction(new Production1());
        Node newNode = new Node("E", 3, 2, 1);
        graph.addNode(newNode);
        graph.addEdge(new Edge(graph.getNodes().get(3), newNode));
    }

    private void createGraphWithChangedLabel() {
        graph.applyProduction(new Production1());
        Node node1 = graph.getNodes().get(2);
        List<Node> neighbours = node1.getNeighbors();
        graph.removeNode(node1);
        Node newNode = new Node("I", node1.getX(), node1.getY(), node1.getLevel());
        graph.addNode(newNode);
        for(Node n:neighbours) {
            graph.addEdge(n, newNode);
        }
    }


    private void createGraphWithChangedCoord() {
        graph.applyProduction(new Production1());
        Node node1 = graph.getNodes().get(2);
        List<Node> neighbours = node1.getNeighbors();
        graph.removeNode(node1);
        Node newNode = new Node(node1.getLabel(), -1, node1.getY(), node1.getLevel());
        graph.addNode(newNode);
        for(Node n:neighbours) {
            graph.addEdge(n, newNode);
        }
    }

    private void visualizeGraph() {
        System.setProperty("org.graphstream.ui", "javafx");
        System.setProperty("org.graphstream.debug", "true");
        GraphUtil.displayGraph(graph);
        try {
            System.in.read();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private void assertThatNodeIsAtLocationAndLevel(String label, int level, float x, float y){
        long count = graph.getNodes().stream().filter(n->n.getLabel().equals(label) && n.getX()==x && n.getY()==y && n.getLevel() == level).count();
        assert count > 0;
    }

    private void assertThatAmountIsCorrect(String label, long amount){
        long count = graph.getNodes().stream().filter(n->n.getLabel().equals(label)).count();
        assertEquals(amount,count);
    }

    private void assertThatAmountAtLevelIsCorrect(String label, long level, long amount){
        long count = graph.getNodes().stream().filter(n->n.getLabel().equals(label)).filter(n->n.getLevel() == level).count();
        assertEquals(amount,count);
    }

    private void assertThatEdgeExists(String label1, int level1, float x1, float y1, String label2, int level2, float x2, float y2){
        Optional<Node> node1 = graph.getNodes().stream().filter(n->n.getLabel().equals(label1) && n.getX()==x1 && n.getY()==y1 && n.getLevel() == level1).findFirst();
        Optional<Node> node2 = graph.getNodes().stream().filter(n->n.getLabel().equals(label2) && n.getX()==x2 && n.getY()==y2 && n.getLevel() == level2).findFirst();

        assertTrue(node1.isPresent());
        assertTrue(node2.isPresent());
        assertTrue(node1.get().getNeighbors().contains(node2.get()));
    }

    private long getNodesCountAtLevel(int level){
        long count = graph.getNodes().stream().filter(n->n.getLevel() == level).count();
        return count;
    }
}
