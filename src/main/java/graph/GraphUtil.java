package graph;

import javafx.util.Pair;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GraphUtil {


    public static void displayGraph(Graph graph) {
        org.graphstream.graph.Graph singleGraph = new SingleGraph("Graph", false, false);


        try {
            URL resource = GraphUtil.class.getResource("style.css");
            if (resource != null) {
                String styles = new String(Files.readAllBytes(Paths.get(resource.toURI())));
                singleGraph.setAttribute("ui.stylesheet", styles);
            }
        } catch (Exception ignored) {
        }

        for (Node node : graph.getNodes()) {
            try {
                org.graphstream.graph.Node n = singleGraph.addNode(node.getId());
                Pair<Double, Double> vector = getShiftVector(node);
                n.setAttribute("xy", node.getX() + vector.getKey(), node.getY() + vector.getValue() - node.getLevel() * 4);
//                n.setAttribute("ui.label", node.getLabel() + " (" + node.getX() + ", " + node.getY() + ")");
                switch (node.getLabel()) {
                    case "I":
                        n.setAttribute("ui.style", "fill-color: red;");
                        break;
                    case "i":
                        n.setAttribute("ui.style", "fill-color: green;");
                        break;
                }
            } catch (IdAlreadyInUseException ignored) { // ignore nodes in same x, y and level
            }
        }

        for (Edge e : graph.getEdges()) {
            try {
                singleGraph.addEdge(e.getId(), e.getStart().getId(), e.getEnd().getId());
            } catch (EdgeRejectedException ignored) { //// ignore edges in same place
            }
        }

        Viewer viewer = singleGraph.display(false);
        disableMouseEvents(viewer);
    }

    public static Pair<Double, Double> getShiftVector(Node node) {
        List<Node> connectedNodes = new ArrayList<>();
        for (Edge edge : node.getEdges()) {
            if (edge.getEnd().getLevel() != edge.getStart().getLevel()) continue;
            connectedNodes.add(edge.getDestination(node));
        }

        double vectorX = 0;
        double vectorY = 0;
        for (Node edgeNode : connectedNodes) {
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
