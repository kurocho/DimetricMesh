package util;

import graph.Graph;
import graph.Node;

import java.util.stream.Stream;

public class Production4Util {
    public static Graph getGraph() {
        Graph resultGraph = new Graph();
        var topLeft = new Node("E", -1, 1, 0);
        var topRight = new Node("E", 1, 1, 0);
        var bottomLeft = new Node("E", -1, -1, 0);
        var bottomRight = new Node("E", 1, -1, 0);
        var additional = new Node("E", 0, 1, 0);
        var iNode = new Node("I", 0, 0, 0);
        Stream.of(topLeft, topRight, bottomLeft, bottomRight, additional, iNode).forEach(resultGraph::addNode);

        resultGraph.addEdge(topLeft, additional);
        resultGraph.addEdge(topLeft, iNode);
        resultGraph.addEdge(topLeft, bottomLeft);
        resultGraph.addEdge(additional, topRight);
        resultGraph.addEdge(topRight, bottomRight);
        resultGraph.addEdge(topRight, iNode);
        resultGraph.addEdge(bottomLeft, iNode);
        resultGraph.addEdge(bottomLeft, bottomRight);
        resultGraph.addEdge(bottomRight, iNode);

        return resultGraph;
    }
}
