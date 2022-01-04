package graph;

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
import java.util.EnumSet;

public class GraphUtil {


    public static void displayGraph(Graph graph) {
        org.graphstream.graph.Graph singleGraph = new SingleGraph("Graph");


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
                n.setAttribute("xy", node.getX(), node.getY() - node.getLevel() * 4);
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
