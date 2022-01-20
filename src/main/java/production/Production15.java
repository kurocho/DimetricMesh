package production;

import graph.Edge;
import graph.Graph;
import graph.Node;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Production15 implements Production {

    @Override
    public Graph apply(Graph graph) {
        List<Node> nodes = null;

        for (Node node : graph.getNodes()) {
            if ("E".equals(node.getLabel())) {
                nodes = getNodes(graph, node);
                if (nodes.isEmpty()) continue;
                else break;
            }
        }

        if (nodes.isEmpty()) {
            System.out.println("No matching left side found");
            return graph;
        }


        System.out.println(nodes);

        graph.removeEdge(new Edge(nodes.get(0), nodes.get(1)));
        graph.removeEdge(new Edge(nodes.get(2), nodes.get(3)));


        List<Node> firstRightNeighbors = graph.getNeighbors(nodes.get(2));
        List<Node> secondRightNeighbors = graph.getNeighbors(nodes.get(3));
        final Node first = nodes.get(0);
        final Node second = nodes.get(1);

        firstRightNeighbors.forEach(neighbor -> graph.addEdge(first, neighbor));
        secondRightNeighbors.forEach(neighbor -> graph.addEdge(second, neighbor));

        graph.removeNode(nodes.get(2));
        graph.removeNode(nodes.get(3));

        graph.addEdge(first, second);

        return graph;

    }

    private List<Node> getNodes(Graph graph, Node rootNode) {
        List<Node> iRootNodeNeighbors = graph.getNeighbors(rootNode, "i");
        List<Node> result = new ArrayList<>();
        for (Node node : iRootNodeNeighbors) {
            for (Node node2 : iRootNodeNeighbors) {
                if (node.equals(node2)) continue;
                result = maybeGetNodesFromI(graph, node, node2);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return result;
    }

    private List<Node> maybeGetNodesFromI(Graph graph, Node node, Node node2) {
        List<Node> iFirstNodeNeighbors = graph.getNeighbors(node, "I");
        List<Node> iSecondNodeNeighbors = graph.getNeighbors(node2, "I");
        List<Node> result = new ArrayList<>();

        for (Node iNode : iFirstNodeNeighbors) {
            for (Node iNode2 : iSecondNodeNeighbors) {
                if (iNode.equals(iNode2)) continue;
                result = maybeGetNodesFromSecondI(graph, iNode, iNode2);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return result;
    }

    private List<Node> maybeGetNodesFromSecondI(Graph graph, Node node, Node node2) {
        List<Node> eFirstNodeNeighbours = graph.getNeighbors(node, "E");
        List<Node> eSecondNodeNeighbours = graph.getNeighbors(node2, "E");
        List<Node> result = new ArrayList<>();

        for (Node eNode1 : eFirstNodeNeighbours) {
            List<Node> eNode1Neighbours = eNode1.getNeighbors().stream().filter(node1 -> "E".equals(node1.getLabel())).collect(Collectors.toList());
            for (Node eNode1Neighbour : eNode1Neighbours) {
                Optional<Node> correspondingNode1 = eSecondNodeNeighbours.stream().filter(node1 -> "E".equals(node1.getLabel()))
                        .filter(node1 -> node1.getX() == eNode1.getX() &&
                                node1.getY() == eNode1.getY() &&
                                eNode1.getLevel() == node1.getLevel() &&
                                !node1.equals(eNode1)).findAny();


                Optional<Node> correspondingNode2 = eSecondNodeNeighbours.stream().filter(node1 -> "E".equals(node1.getLabel()))
                        .filter(node1 -> node1.getX() == eNode1Neighbour.getX() &&
                                node1.getY() == eNode1Neighbour.getY() &&
                                node1.getLevel() == eNode1Neighbour.getLevel() &&
                                !node1.equals(eNode1Neighbour)).findAny();

                if (!correspondingNode1.isPresent() || !correspondingNode2.isPresent()) continue;
                else {
                    result.add(eNode1);
                    result.add(eNode1Neighbour);
                    result.add(correspondingNode1.get());
                    result.add(correspondingNode2.get());
                    return result;
                }
            }

        }
        return result;
    }
}
