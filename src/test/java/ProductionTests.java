import graph.Edge;
import graph.Graph;
import graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import production.Production1;
import production.Production12;
import production.Production13;
import production.Production2;
import util.Production13Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class ProductionTests {

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
        assertThatAmountOfNodesIsCorrect("E",1);
    }

    @Test
    void testProduction1(){
        //when
        graph.applyProduction(new Production1());
        //then
        assertThatAmountOfNodesIsCorrect("e",1);
        assertThatAmountOfNodesIsCorrect("E",4);
        assertThatAmountOfNodesIsCorrect("I",1);
    }

    @Test
    void testProduction2Vertical(){
        //when
        graph.applyProduction(new Production1());
        int prevNrOfEdges = graph.getEdges().size();
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        //then
        assertThatAmountOfNodesIsCorrect("e",1);
        assertThatAmountOfNodesIsCorrect("E",10);
        assertThatAmountOfNodesIsCorrect("i",1);
        assertThatAmountOfNodesIsCorrect("I",2);
        assertProduction2HasAppliedEdgesCorrectly(prevNrOfEdges);
        List<Node> INodes = graph.getNodes().stream().filter(n -> n.getLabel().equals("I")).collect(Collectors.toList());
        assertEquals(INodes.get(0).getY(), INodes.get(1).getY());
    }

    @Test
    void testProduction2Horizontal(){
        //when
        graph.applyProduction(new Production1());
        int prevNrOfEdges = graph.getEdges().size();
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        //then
        assertThatAmountOfNodesIsCorrect("e",1);
        assertThatAmountOfNodesIsCorrect("E",10);
        assertThatAmountOfNodesIsCorrect("i",1);
        assertThatAmountOfNodesIsCorrect("I",2);
        assertProduction2HasAppliedEdgesCorrectly(prevNrOfEdges);
        List<Node> INodes = graph.getNodes().stream().filter(n -> n.getLabel().equals("I")).collect(Collectors.toList());
        assertEquals(INodes.get(0).getX(), INodes.get(1).getX());
    }

    @Test
    void prod2TestLeftSideMissingNode(){
        //when
        graph.applyProduction(new Production1());
        Node missingNode = graph.getNodes().stream().filter(s -> s.getLabel().equals("E")).findFirst().get();
        graph.removeNode(missingNode);
        List<Edge> prevEdges = new ArrayList<>(graph.getEdges());
        List<Node> prevNodes = new ArrayList<>(graph.getNodes());

        //then
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        assertEquals(prevEdges, graph.getEdges());
        assertEquals(prevNodes, graph.getNodes());
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        assertEquals(prevEdges, graph.getEdges());
        assertEquals(prevNodes, graph.getNodes());
    }

    @Test
    void prod2TestLeftSideMissingEdge(){
        //when
        graph.applyProduction(new Production1());
        Node nodeOfMissingEdge = graph.getNodes()
                .stream().filter(s -> s.getLabel().equals("E")).findFirst().get();
        Edge missingEdge = graph.getEdges()
                .stream().filter(e -> e.getEnd() == nodeOfMissingEdge || e.getStart() == nodeOfMissingEdge)
                .findFirst().get();
        graph.removeEdge(missingEdge);
        List<Edge> prevEdges = new ArrayList<>(graph.getEdges());
        List<Node> prevNodes = new ArrayList<>(graph.getNodes());

        //then
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        assertEquals(prevEdges, graph.getEdges());
        assertEquals(prevNodes, graph.getNodes());
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        assertEquals(prevEdges, graph.getEdges());
        assertEquals(prevNodes, graph.getNodes());
    }

    @Test
    void prod2TestLeftSideNodeWithWrongLabel(){
        //when
        graph.applyProduction(new Production1());
        Node nodeWithWrongLabel = graph.getNodes().stream().filter(s -> s.getLabel().equals("E")).findFirst().get();
        nodeWithWrongLabel.setLabel("Not E");
        List<Edge> prevEdges = new ArrayList<>(graph.getEdges());
        List<Node> prevNodes = new ArrayList<>(graph.getNodes());

        //then
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        assertEquals(prevEdges, graph.getEdges());
        assertEquals(prevNodes, graph.getNodes());
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        assertEquals(prevEdges, graph.getEdges());
        assertEquals(prevNodes, graph.getNodes());
    }

    @Test
    void prod2TestLeftSideNodeWrongCoords(){
        //when
        setupGraphWithNodeWithWrongCoord();
        int prevNrOfEdges = graph.getEdges().size();
        graph.applyProduction(new Production2());

        //then
        assertThatAmountOfNodesIsCorrect("e",1);
        assertThatAmountOfNodesIsCorrect("E",10);
        assertThatAmountOfNodesIsCorrect("i",1);
        assertThatAmountOfNodesIsCorrect("I",2);
        assertProduction2HasAppliedEdgesCorrectly(prevNrOfEdges);
    }

    private void setupGraphWithNodeWithWrongCoord() {
        float oldX = graph.getNodes().get(0).getX();
        float oldY = graph.getNodes().get(0).getY();

        int newLevel = graph.getNodes().get(0).getLevel() + 1;

        graph.getNodes().get(0).setLabel("e");

        Node I = new Node("I", oldX, oldY, newLevel);
        Node E1 = new Node("E", oldX - 1, oldY + 1, newLevel);
        Node E2 = new Node("E", oldX + 1, oldY + 1, newLevel);
        Node E3 = new Node("E", oldX + 1, oldY - 1, newLevel);
        Node E4 = new Node("E", oldX - 100, oldY - 1, newLevel);
        graph.addNode(I);
        graph.addNode(E1);
        graph.addNode(E2);
        graph.addNode(E3);
        graph.addNode(E4);
        graph.addEdge(new Edge(graph.getNodes().get(0), I));
        graph.addEdge(new Edge(I, E1));
        graph.addEdge(new Edge(I, E2));
        graph.addEdge(new Edge(I, E3));
        graph.addEdge(new Edge(I, E4));
        graph.addEdge(new Edge(E1, E2));
        graph.addEdge(new Edge(E2, E3));
        graph.addEdge(new Edge(E3, E4));
        graph.addEdge(new Edge(E4, E1));
    }

    @Test
    void testProduction12(){
        //when
        graph.applyProduction(new Production1());
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        graph.applyProduction(new Production12());
        graph.applyProduction(new Production12());
        //then
        assertThatAmountOfNodesIsCorrect("e",1);
        assertThatAmountOfNodesIsCorrect("E",30);
        assertThatAmountOfNodesIsCorrect("i",5);
        assertThatAmountOfNodesIsCorrect("I",4);
    }

    @Test
    void testProduction13(){
        //when
        graph = Production13Util.getGraph();
        graph.applyProduction(new Production13());
        //then
        assertThatAmountOfNodesIsCorrect("e",0);
        assertThatAmountOfNodesIsCorrect("E",4);
        assertThatAmountOfNodesIsCorrect("i",0);
        assertThatAmountOfNodesIsCorrect("I",5);
    }


    private void assertThatAmountOfNodesIsCorrect(String label, long amount){
        long count = graph.getNodes().stream().filter(n->n.getLabel().equals(label)).count();
        assertEquals(amount,count);
    }

    private void assertProduction2HasAppliedEdgesCorrectly(int prevNrOfEdges) {
        assertThatAmountOfNodesIsCorrect("e",1);
        assertThatAmountOfNodesIsCorrect("E",10);
        assertThatAmountOfNodesIsCorrect("i",1);
        assertThatAmountOfNodesIsCorrect("I",2);
        assertEquals(prevNrOfEdges + 17, graph.getEdges().size());
        assertEquals(10,
                graph.getEdges().stream().filter(e -> e.getStart().getLabel().equals("I") || e.getEnd().getLabel().equals("I")).count(), 10);
        assertEquals(8,
                graph.getEdges()
                        .stream()
                        .filter(e -> (e.getStart().getLabel().equals("I") && e.getEnd().getLabel().equals("E")) ||
                                (e.getStart().getLabel().equals("E") && e.getEnd().getLabel().equals("I")))
                        .count());
        assertEquals(2,
                graph.getEdges()
                        .stream()
                        .filter(e -> (e.getStart().getLabel().equals("I") && e.getEnd().getLabel().equals("i")) ||
                                (e.getStart().getLabel().equals("i") && e.getEnd().getLabel().equals("I")))
                        .count());
    }

}
