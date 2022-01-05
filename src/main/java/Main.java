import graph.*;
import production.Production1;
import production.Production12;
import production.Production13;
import production.Production2;

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

//        GraphUtil.displayGraph(graph, 0);
//
//        GraphUtil.displayGraph(graph,1);

//        GraphUtil.displayGraph(graph);

        Graph tstGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        tstGraph.addNode(mainNode);

        Node leftI = new Node("I", -1, -1, 0);
        Node rightI = new Node("I", 1, -1, 0);
        tstGraph.addNode(leftI);
        tstGraph.addNode(rightI);
        tstGraph.addEdge(mainNode, leftI);
        tstGraph.addEdge(mainNode, rightI);

        Node leftIChildOne = new Node("I", -2, -3, 0);
        Node leftIChildTwo = new Node("I", -2, -5, 0);
        tstGraph.addNode(leftIChildOne);
        tstGraph.addNode(leftIChildTwo);
        tstGraph.addEdge(leftI, leftIChildOne);
        tstGraph.addEdge(leftI, leftIChildTwo);

        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -4, 0);
        Node leftIEThree = new Node("E", 0, -6, 0);
        tstGraph.addNode(leftIEOne);
        tstGraph.addNode(leftIETwo);
        tstGraph.addNode(leftIEThree);
        tstGraph.addEdge(leftIChildOne, leftIEOne);
        tstGraph.addEdge(leftIChildOne, leftIETwo);
        tstGraph.addEdge(leftIChildTwo, leftIETwo);
        tstGraph.addEdge(leftIChildTwo, leftIEThree);
        tstGraph.addEdge(leftIEOne, leftIETwo);
        tstGraph.addEdge(leftIETwo, leftIEThree);

        Node rightIChild = new Node("I", 2, -4, 0);
        tstGraph.addNode(rightIChild);
        tstGraph.addEdge(rightI, rightIChild);

        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -6, 0);
        tstGraph.addNode(rightIEOne);
        tstGraph.addNode(rightIETwo);
        tstGraph.addEdge(rightIChild, rightIEOne);
        tstGraph.addEdge(rightIChild, rightIETwo);
        tstGraph.addEdge(rightIEOne, rightIETwo);

        tstGraph.applyProduction(new Production13());

        GraphUtil.displayGraph(tstGraph);
    }

}
