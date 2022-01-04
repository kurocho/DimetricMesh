import graph.*;
import production.Production1;
import production.Production2;

public class Main {


    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");

        Graph graph = new Graph();

        Node e = new Node("E", 1, 1, 0);
        graph.addNode(e);

        graph.applyProduction(new Production1());

        graph.applyProduction(new Production2());

        graph.applyProduction(new Production2());
        graph.applyProduction(new Production2());

        graph.applyProduction(new Production2());
        graph.applyProduction(new Production2());
        graph.applyProduction(new Production2());
        graph.applyProduction(new Production2());

        GraphUtil.displayGraph(graph,4);

        GraphUtil.displayGraph(graph);
    }

}
