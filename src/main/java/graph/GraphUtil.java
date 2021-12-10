package graph;

import org.graphstream.graph.implementations.SingleGraph;

public class GraphUtil {


    public static void displayGraph(Graph graph) {
        org.graphstream.graph.Graph singleGraph = new SingleGraph("Graph");

        for(Node node : graph.getNodes()) {
            singleGraph.addNode(node.getId());
        }

        for(Edge e : graph.getEdges()) {
            singleGraph.addEdge(e.getId(), e.getStart().getId(), e.getEnd().getId());
        }

        singleGraph.display();
    }

    public static void displayGraph(Graph graph, int level) {
        org.graphstream.graph.Graph singleGraph = new SingleGraph("Graph");

        for(Node node : graph.getNodes()) {
            if(node.getLevel() == level) {
                singleGraph.addNode(node.getId());
            }
        }

        for(Edge e : graph.getEdges()) {
            if(e.getStart().getLevel() == level && e.getEnd().getLevel() == level) {
                singleGraph.addEdge(e.getId(), e.getStart().getId(), e.getEnd().getId());
            }
        }

        singleGraph.display();
    }
}
