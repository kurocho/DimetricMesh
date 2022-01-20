import graph.*;
import production.*;
import util.Production13Util;

public class Main {
    static void taskB(){
        Graph graph = new Graph();

        Node e = new Node("E", 1, 1, 0);
        graph.addNode(e);

        graph.applyProduction(new Production1());
        graph.applyProduction(new Production3());


        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));
        graph.applyProduction(new Production12());
        graph.applyProduction(new Production2(Production2.BreakMode.VERTICAL));
        graph.applyProduction(new Production2(Production2.BreakMode.HORIZONTAL));


        graph.applyProduction(new Production13());
        graph.applyProduction(new Production13());

        graph.applyProduction(new Production15());
        graph.applyProduction(new Production14Hacks());
        GraphUtil.displayGraph(graph, 3);
    }

    static void taskA(){
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

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");
        System.setProperty("org.graphstream.debug", "true");

        taskB();
    }

}
