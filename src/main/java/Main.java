import graph.*;
import production.Production1;
import production.Production12;
import production.Production13;
import production.Production2;
import util.Production13Util;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");
        System.setProperty("org.graphstream.debug", "true");

        Graph graph = new Graph();

        Node e = new Node("E", 1, 1, 0);
        graph.addNode(e);

        graph.applyProduction(new Production1());

        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));

        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));

        graph.applyProduction(new Production12());
        graph.applyProduction(new Production12());

//        GraphUtil.displayGraph(graph, 0);
//        GraphUtil.displayGraph(graph,1);

        GraphUtil.displayGraph(graph);

        Graph production13Graph = Production13Util.getGraph();
        production13Graph.applyProduction(new Production13());
        GraphUtil.displayGraph(production13Graph);
    }

}
