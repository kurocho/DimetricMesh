package graph;

import javafx.util.Pair;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class GraphUtil {


    public static void displayGraph(Graph graph) {
        org.graphstream.graph.Graph singleGraph = new SingleGraph("Graph", false, false);


        try {
            String styles = new String(Files.readAllBytes(Paths.get(GraphUtil.class.getResource("style.css").toURI())));
            singleGraph.setAttribute("ui.stylesheet", styles);
        } catch (Exception e) {
        }

        for (Node node : graph.getNodes()) {
            org.graphstream.graph.Node n = singleGraph.addNode(node.getId());
//            Pair<Double, Double> vector = new Pair<>(0.0, 0.0);
            Pair<Double, Double> vector = getShiftVector(graph, node);
            n.setAttribute("xy", node.getX() + vector.getKey(), node.getY() + vector.getValue() - node.getLevel() * 4);
            n.setAttribute("ui.label", node.getLabel()+" ("+node.getX()+", "+node.getY()+")");
        }

        for (Edge e : graph.getEdges()) {
            singleGraph.addEdge(e.getId(), e.getStart().getId(), e.getEnd().getId());
        }

        Viewer viewer = singleGraph.display(false);
        disableMouseEvents(viewer);
    }

    public static Pair<Double, Double> getShiftVector(Graph graph, Node node) {
        List<Node> connectedNodes = new ArrayList<>();
        for(Edge edge: graph.getEdges()) {
            if(edge.getEnd().getLevel() != edge.getStart().getLevel()) continue;
            if(edge.getStart().getId().equals(node.getId())) {
                connectedNodes.add(edge.getEnd());
            } else if(edge.getEnd().getId().equals(node.getId())) {
                connectedNodes.add(edge.getStart());
            }
        }

        double vectorX = 0;
        double vectorY = 0;
        for(Node edgeNode: connectedNodes) {
            vectorX += (edgeNode.getX() - node.getX());
            vectorY += (edgeNode.getY() - node.getY());
        }

        double vectorLength = Math.sqrt(vectorX * vectorX + vectorY * vectorY) * 5;
        return vectorLength == 0 ? new Pair<>(0.0, 0.0) : new Pair<>(vectorX/vectorLength, vectorY/vectorLength);
    }

    public static void displayGraph(Graph graph, int level) {
        Graph levelGraph = new Graph();

        graph.getNodes().stream()
                .filter(node -> node.getLevel() == level)
                .forEach(levelGraph::addNode);
        graph.getEdges().stream()
                .filter(edge -> edge.getStart().getLevel() == level && edge.getEnd().getLevel() == level)
                .forEach(levelGraph::addEdge);

        displayGraph(levelGraph);
    }

    private static void disableMouseEvents(Viewer viewer) {
        viewer.getDefaultView()
                .setMouseManager(new MouseManager() {
                    @Override
                    public void init(GraphicGraph graph, View view) {
                    }

                    @Override
                    public void release() {
                    }

                    @Override
                    public EnumSet<InteractiveElement> getManagedTypes() {
                        return null;
                    }
                });
    }
}
