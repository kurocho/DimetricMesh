package util;

import graph.Graph;
import graph.Node;

public class Production13Util {
    public static Graph getGraph() {
        Graph resultGraph = new Graph();
        Node mainNode = new Node("E", 0, 0, 0);
        resultGraph.addNode(mainNode);

        Node leftI = new Node("i", -1, -1, 0);
        Node rightI = new Node("i", 1, -1, 0);
        resultGraph.addNode(leftI);
        resultGraph.addNode(rightI);
        resultGraph.addEdge(mainNode, leftI);
        resultGraph.addEdge(mainNode, rightI);

        Node leftIChildOne = new Node("I", -2, -3, 0);
        Node leftIChildTwo = new Node("I", -2, -5, 0);
        resultGraph.addNode(leftIChildOne);
        resultGraph.addNode(leftIChildTwo);
        resultGraph.addEdge(leftI, leftIChildOne);
        resultGraph.addEdge(leftI, leftIChildTwo);

        Node leftIEOne = new Node("E", 0, -2, 0);
        Node leftIETwo = new Node("E", 0, -4, 0);
        Node leftIEThree = new Node("E", 0, -6, 0);
        resultGraph.addNode(leftIEOne);
        resultGraph.addNode(leftIETwo);
        resultGraph.addNode(leftIEThree);
        resultGraph.addEdge(leftIChildOne, leftIEOne);
        resultGraph.addEdge(leftIChildOne, leftIETwo);
        resultGraph.addEdge(leftIChildTwo, leftIETwo);
        resultGraph.addEdge(leftIChildTwo, leftIEThree);
        resultGraph.addEdge(leftIEOne, leftIETwo);
        resultGraph.addEdge(leftIETwo, leftIEThree);

        Node rightIChild = new Node("I", 2, -4, 0);
        resultGraph.addNode(rightIChild);
        resultGraph.addEdge(rightI, rightIChild);

        Node rightIEOne = new Node("E", 0, -2, 0);
        Node rightIETwo = new Node("E", 0, -6, 0);
        resultGraph.addNode(rightIEOne);
        resultGraph.addNode(rightIETwo);
        resultGraph.addEdge(rightIChild, rightIEOne);
        resultGraph.addEdge(rightIChild, rightIETwo);
        resultGraph.addEdge(rightIEOne, rightIETwo);

        return resultGraph;
    }
}
