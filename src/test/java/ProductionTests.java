import graph.Graph;
import graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import production.Production1;
import production.Production12;
import production.Production13;
import production.Production2;
import util.Production13Util;

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
        assertThatAmountIsCorrect("E",1);
    }

    @Test
    void testProduction1(){
        //when
        graph.applyProduction(new Production1());
        //then
        assertThatAmountIsCorrect("e",1);
        assertThatAmountIsCorrect("E",4);
        assertThatAmountIsCorrect("I",1);
    }

    @Test
    void testProduction2(){
        //when
        graph.applyProduction(new Production1());
        graph.applyProduction(new Production2());
        //then
        assertThatAmountIsCorrect("e",1);
        assertThatAmountIsCorrect("E",10);
        assertThatAmountIsCorrect("i",1);
        assertThatAmountIsCorrect("I",2);
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
        assertThatAmountIsCorrect("e",1);
        assertThatAmountIsCorrect("E",30);
        assertThatAmountIsCorrect("i",5);
        assertThatAmountIsCorrect("I",4);
    }

    @Test
    void testProduction13(){
        //when
        graph = Production13Util.getGraph();
        graph.applyProduction(new Production13());
        //then
        assertThatAmountIsCorrect("e",0);
        assertThatAmountIsCorrect("E",4);
        assertThatAmountIsCorrect("i",0);
        assertThatAmountIsCorrect("I",5);
    }

    private void assertThatAmountIsCorrect(String label, long amount){
        long count = graph.getNodes().stream().filter(n->n.getLabel().equals(label)).count();
        assertEquals(amount,count);
    }
}
