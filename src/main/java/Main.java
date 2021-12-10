import graph.*;
import production.Production1;

public class Main {


    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");

        Graph graph = new Graph();

        Node e = new Node("E", 1, 1, 0);
        graph.addNode(e);

        graph.applyProduction(new Production1());

        GraphUtil.displayGraph(graph, 0);

        GraphUtil.displayGraph(graph,1);

        GraphUtil.displayGraph(graph);
    }

}
