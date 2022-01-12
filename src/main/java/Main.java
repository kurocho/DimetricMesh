import graph.*;
import production.*;
import util.Production13Util;
import util.Production4Util;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");
        System.setProperty("org.graphstream.debug", "true");

//        Graph graph = new Graph();
        Graph graph = Production4Util.getGraph();
        graph.applyProduction(new Production4());
//        graph.applyProduction(new Production4());

//        Node e = new Node("E", 1, 1, 0);
//        graph.addNode(e);
//
//        graph.applyProduction(new Production1());
//
//        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
//
//        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
//        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
//
//        graph.applyProduction(new Production12());
//        graph.applyProduction(new Production12());

//        GraphUtil.displayGraph(graph, 0);
//        GraphUtil.displayGraph(graph,1);

        GraphUtil.displayGraph(graph);

//        Graph production13Graph = Production13Util.getGraph();
//        production13Graph.applyProduction(new Production13());
//        GraphUtil.displayGraph(production13Graph);
    }

}
