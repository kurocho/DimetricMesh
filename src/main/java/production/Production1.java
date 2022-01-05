package production;

import graph.Edge;
import graph.Graph;
import graph.Node;

public class Production1 implements Production {

    public Graph apply(Graph graph) {
        for (Node node : graph.getNodes()) {
            if ("E".equals(node.getLabel())) {
                float oldX = node.getX();
                float oldY = node.getY();

                int newLevel = node.getLevel() + 1;

                node.setLabel("e");

                Node I = new Node("I", oldX, oldY, newLevel);
                Node E1 = new Node("E", oldX - 1, oldY + 1, newLevel);
                Node E2 = new Node("E", oldX + 1, oldY + 1, newLevel);
                Node E3 = new Node("E", oldX + 1, oldY - 1, newLevel);
                Node E4 = new Node("E", oldX - 1, oldY - 1, newLevel);
                graph.addNode(I);
                graph.addNode(E1);
                graph.addNode(E2);
                graph.addNode(E3);
                graph.addNode(E4);
                graph.addEdge(new Edge(node, I));
                graph.addEdge(new Edge(I, E1));
                graph.addEdge(new Edge(I, E2));
                graph.addEdge(new Edge(I, E3));
                graph.addEdge(new Edge(I, E4));
                graph.addEdge(new Edge(E1, E2));
                graph.addEdge(new Edge(E2, E3));
                graph.addEdge(new Edge(E3, E4));
                graph.addEdge(new Edge(E4, E1));
                break;
            }
        }
        return graph;
    }

}
