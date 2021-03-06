package production;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Production12 implements Production {

    @Override
    public Graph apply(Graph graph) {
        Node nodeI = null;
        List<Node> nodesE = new LinkedList<>();
        for (Node node : graph.getNodes()) { //lets find any I
            if ("I".equals(node.getLabel())) {
                for (Edge edge : graph.getEdges()) { //lets find all edges with this I and E node
                    if ("E".equals(edge.getEnd().getLabel()) &&
                            edge.getStart().getId().equals(node.getId())) {
                        nodesE.add(edge.getEnd());
                    }
                }
                if (isFormingRectangle(graph, nodesE)) { // check if found edges are forming a rectangle
                    nodeI = node;
                    break;
                } else { //if not, let's go again
                    nodesE.clear();
                }
            }
        }

        if (nodeI == null) { // this means no node was found
            System.out.println("No graph matching left side of production found");
            return graph; // unedited
        }

        Node newNodeI = cloneNodeOnNextLevel(nodeI);
        List<Node> newNodesE = nodesE.stream()
                        .map(this::cloneNodeOnNextLevel)
                        .collect(Collectors.toList());

        graph.addNode(newNodeI);
        for(int i=0;i<newNodesE.size();++i) {
            final Node firstNode = newNodesE.get(i);
            graph.addNode(firstNode);
            graph.addEdge(firstNode, newNodeI);
            for(int j=i+1;j<newNodesE.size();++j) {
                final Node secondNode = newNodesE.get(j);
                final boolean sameNode = firstNode.equals(secondNode);
                final boolean xLined = firstNode.getX() == secondNode.getX();
                final boolean yLined = firstNode.getY() == secondNode.getY();
                if(!sameNode && (xLined || yLined)) {
                    graph.addEdge(firstNode, secondNode);
                }
            }
        }

        graph.addEdge(nodeI, newNodeI);

        nodeI.setLabel("i");

        return graph;
    }

    private Node cloneNodeOnNextLevel(Node nodeToClone) {
        return new Node(nodeToClone.getLabel(), nodeToClone.getX(), nodeToClone.getY(), nodeToClone.getLevel() + 1);
    }

    private boolean isFormingRectangle(Graph graph, List<Node> nodes) {
        if (nodes.size() != 4) return false;
        for (Node n1 : nodes) {
            for (Node n2 : nodes) {
                if (n1.getId().equals(n2.getId()) ||
                        (n1.getX() != n2.getX() && n1.getY() != n2.getY()))
                    continue;
                if (!graph.getEdges().contains(new Edge(n1, n2)) &&
                        !graph.getEdges().contains(new Edge(n2, n1)))
                    return false;
            }
        }
        return true;
    }
}
