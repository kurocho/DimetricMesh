package graph;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;

public class GraphUtil {


    public static void displayGraph(Graph graph) {
        org.graphstream.graph.Graph singleGraph = new SingleGraph("Graph");


        try {
            String styles = new String(Files.readAllBytes(Paths.get(GraphUtil.class.getResource("style.css").toURI())));
            singleGraph.setAttribute("ui.stylesheet", styles);
        } catch (Exception e) {
        }

        for (Node node : graph.getNodes()) {
            org.graphstream.graph.Node n = singleGraph.addNode(node.getId());
            n.setAttribute("xy", node.getX(), node.getY() - node.getLevel() * 4);
            n.setAttribute("ui.label", node.getLabel());
        }

        for (Edge e : graph.getEdges()) {
            singleGraph.addEdge(e.getId(), e.getStart().getId(), e.getEnd().getId());
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
