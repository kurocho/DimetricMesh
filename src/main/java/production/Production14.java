package production;

import graph.Edge;
import graph.Graph;
import graph.Node;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Production14 implements Production {

    @Override
    public Graph apply(Graph graph) {
        List<Node> leftNodes = new ArrayList<>();
        List<Node> rightNodes = new ArrayList<>();
        for (Node node : graph.getNodes()) {
            if ("E".equals(node.getLabel())) {
                final List<List<Node>> leftNodesList = getLeftSides(graph, node);
                if (leftNodesList.isEmpty()) continue;
                for (List<Node> leftNodesPropose : leftNodesList) {
                    leftNodes = leftNodesPropose;
                    rightNodes = getRightSide(graph, node, leftNodes.get(0), leftNodes.get(2));
                    if (!rightNodes.isEmpty()) break;
                }
                if (!rightNodes.isEmpty()) break;
            }
        }

        if (leftNodes.isEmpty() || rightNodes.isEmpty()) {
            System.out.println(">> No graph matching left side of production found");
            return graph; // unedited
        }

        graph.removeEdge(new Edge(rightNodes.get(0), rightNodes.get(2)));
        graph.removeEdge(new Edge(rightNodes.get(0), rightNodes.get(1)));
        graph.removeEdge(new Edge(rightNodes.get(2), rightNodes.get(1)));

        List<Node> firstRightNeighbors = graph.getNeighbors(rightNodes.get(0));
        List<Node> secondRightNeighbors = graph.getNeighbors(rightNodes.get(2));
        final Node firstLeft = leftNodes.get(0);
        final Node secondLeft = leftNodes.get(2);

        Node sharedNode = firstLeft == rightNodes.get(0) || secondLeft == rightNodes.get(0) ? rightNodes.get(0) : rightNodes.get(2);
        Node redundantNode = sharedNode == rightNodes.get(0) ? rightNodes.get(2) : rightNodes.get(0);

        firstRightNeighbors.forEach(neighbor -> graph.addEdge(firstLeft, neighbor));
        secondRightNeighbors.forEach(neighbor -> graph.addEdge(secondLeft, neighbor));

        graph.removeNode(redundantNode);

        graph.addEdge(rightNodes.get(1), leftNodes.get(0));
        graph.addEdge(rightNodes.get(1), leftNodes.get(2));
//
        return graph;
    }

    private List<List<Node>> getLeftSides(Graph graph, Node rootNode) {
        List<Node> iRootNodeNeighbors = graph.getNeighbors(rootNode, "i");
        final List<List<Node>> results = new ArrayList<>();
        for (Node node : iRootNodeNeighbors) {
            Map<Node, List<Node>> iNeighborhood = new HashMap<>();
            Map<Node, List<Node>> eNeighborhood = new HashMap<>();
            List<Node> iNeighbors = graph.getNeighbors(node, "I");
            for (Node otherNode : iNeighbors) {
                List<Node> eNeighbors = graph.getNeighbors(otherNode, "E");
                eNeighbors.forEach(neighbor -> {
                    iNeighborhood.computeIfAbsent(neighbor, k -> new ArrayList<>());
                    iNeighborhood.get(neighbor).add(otherNode);
                });
            }
            iNeighborhood.keySet().forEach(neighbor -> eNeighborhood.put(neighbor, graph.getNeighbors(neighbor, "E")));
            Map<Node, Pair<Pair<Node, Node>, Pair<Node, Node>>> neighborhood = new HashMap<>();
            iNeighborhood.keySet().forEach(key -> {
                List<Node> iValue = iNeighborhood.get(key);
                List<Node> eValue = eNeighborhood.get(key);
                if (iValue != null && eValue != null) {
                    final Pair<Node, Node> iPairY = getTwoYLined(iValue);
                    final Pair<Node, Node> ePairY = getTwoYLined(eValue);
                    final Pair<Node, Node> iPairX = getTwoXLined(iValue);
                    final Pair<Node, Node> ePairX = getTwoXLined(eValue);
                    if (iPairY != null && ePairY != null) {
                        neighborhood.put(key, new Pair<>(iPairY, ePairY));
                    }
                    if (iPairX != null && ePairX != null) {
                        neighborhood.put(key, new Pair<>(iPairX, ePairX));
                    }
                }
            });
            if (neighborhood.keySet().isEmpty()) continue;
            for (Node key : neighborhood.keySet()) {
                Pair<Pair<Node, Node>, Pair<Node, Node>> pair = neighborhood.get(key);
                Node firstI = pair.getKey().getKey();
                Node secondI = pair.getKey().getValue();
                Node firstE = pair.getValue().getKey();
                Node secondE = pair.getValue().getValue();
                if ((graph.areNeighbors(firstI, firstE) && graph.areNeighbors(secondI, secondE)) ||
                        (graph.areNeighbors(firstI, secondE) && graph.areNeighbors(secondI, firstE))) {
                    List<Node> result = new ArrayList<>();
                    result.add(firstE);
                    result.add(key);
                    result.add(secondE);
                    results.add(result);
                }
            }
        }
        return results;
    }

    private List<Node> getRightSide(Graph graph, Node rootNode, Node samePosition1, Node samePosition2) {
        List<Node> iRootNodeNeighbors = graph.getNeighbors(rootNode, "i");
        for (Node node : iRootNodeNeighbors) {
            Map<Node, List<Node>> iNeighborhood = new HashMap<>();
            Map<Node, List<Node>> eNeighborhood = new HashMap<>();
            List<Node> iNeighbors = graph.getNeighbors(node, "I");
            for (Node otherNode : iNeighbors) {
                List<Node> eNeighbors = graph.getNeighbors(otherNode, "E");
                eNeighbors.forEach(neighbor -> {
                    iNeighborhood.computeIfAbsent(neighbor, k -> new ArrayList<>());
                    iNeighborhood.get(neighbor).add(otherNode);
                });
            }
            iNeighborhood.keySet().forEach(neighbor -> eNeighborhood.put(neighbor, graph.getNeighbors(neighbor, "E")));
            Map<Node, Pair<List<Node>, List<Node>>> neighborhood = new HashMap<>();
            iNeighborhood.keySet().forEach(key -> {
                List<Node> iValue = iNeighborhood.get(key);
                List<Node> eValue = eNeighborhood.get(key);
                if (iValue != null && eValue != null) {
                    neighborhood.put(key, new Pair<>(iValue, eValue));
                }
            });
            if (neighborhood.keySet().isEmpty()) continue;
            for (Node key : neighborhood.keySet()) {
                Node eValue = eNeighborhood.get(key).get(0);
                Node iValue = iNeighborhood.get(key).get(0);
                if ((graph.areNeighbors(eValue, iValue))) {
                    if (key.getX() == samePosition1.getX() && key.getY() == samePosition1.getY()) {
                        if (eValue.getX() == samePosition2.getX() && eValue.getY() == samePosition2.getY()) {
                            if (hasOneCommonNode(key, eValue, samePosition1, samePosition2)) {
                                List<Node> result = new ArrayList<>();
                                result.add(key);
                                result.add(iValue);
                                result.add(eValue);
                                return result;
                            }
                        }
                    }
                    if (key.getX() == samePosition2.getX() && key.getY() == samePosition2.getY()) {
                        if (eValue.getX() == samePosition1.getX() && eValue.getY() == samePosition1.getY()) {
                            if (hasOneCommonNode(key, eValue, samePosition1, samePosition2)) {
                                List<Node> result = new ArrayList<>();
                                result.add(eValue);
                                result.add(iValue);
                                result.add(key);
                                return result;
                            }
                        }
                    }

                }
            }
        }
        return new ArrayList<>();
    }

    private boolean hasOneCommonNode(Node firstLeft, Node secondLeft, Node firstRight, Node secondRight) {
        return (firstLeft == firstRight && secondLeft != secondRight) ||
                (secondLeft == secondRight && firstLeft != firstRight) ||
                (firstLeft == secondRight && secondLeft != firstRight) ||
                (secondLeft == firstRight && firstLeft != secondRight);
    }

    private Pair<Node, Node> getTwoYLined(List<Node> nodes) {
        if (nodes.size() < 2) return null;
        for (int i = 0; i < nodes.size() - 1; ++i) {
            final Node firstNode = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); ++j) {
                final Node secondNode = nodes.get(j);
                if (firstNode.getY() == secondNode.getY()) return new Pair<>(firstNode, secondNode);
            }
        }
        return null;
    }

    private Pair<Node, Node> getTwoXLined(List<Node> nodes) {
        if (nodes.size() < 2) return null;
        for (int i = 0; i < nodes.size() - 1; ++i) {
            final Node firstNode = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); ++j) {
                final Node secondNode = nodes.get(j);
                if (firstNode.getX() == secondNode.getX()) return new Pair<>(firstNode, secondNode);
            }
        }
        return null;
    }
}
