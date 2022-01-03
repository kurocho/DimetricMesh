package production;

import graph.Graph;
import graph.Node;
import javafx.util.Pair;

import java.util.*;

public class Production13 implements Production {

    @Override
    public Graph apply(Graph graph) {
        List<Node> leftNodes = null;
        List<Node> rightNodes = null;
        for (Node node : graph.getNodes()) {
            if ("E".equals(node.getLabel())) {
                leftNodes = getLeftSide(graph, node);
                if(leftNodes.isEmpty()) continue;
                rightNodes = getRightSide(graph, node, leftNodes.get(0), leftNodes.get(2));
                if(!rightNodes.isEmpty()) break;
            }
        }

        if (leftNodes == null || rightNodes == null) {
            System.out.println("No graph matching left side of production found");
            return graph; // unedited
        }

        graph.removeEdge(rightNodes.get(0), rightNodes.get(2));
        graph.removeEdge(rightNodes.get(0), rightNodes.get(1));
        graph.removeEdge(rightNodes.get(2), rightNodes.get(1));
        graph.removeNode(rightNodes.get(0));
        graph.removeNode(rightNodes.get(2));

        graph.addEdge(rightNodes.get(1), leftNodes.get(0));
        graph.addEdge(rightNodes.get(1), leftNodes.get(2));

        return graph;
    }

    private List<Node> getLeftSide(Graph graph, Node rootNode) {
        List<Node> iRootNodeNeighbors = graph.getNeighbors(rootNode, "I");
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
                if(iValue != null && iValue.size() == 2 && eValue != null && eValue.size() == 2) {
                    neighborhood.put(key, new Pair<>(iValue, eValue));
                }
            });
            if(neighborhood.keySet().isEmpty()) continue;
            for (Node key : neighborhood.keySet()) {
                Pair<List<Node>, List<Node>> pair = neighborhood.get(key);
                Node firstI = pair.getKey().get(0);
                Node secondI = pair.getKey().get(1);
                Node firstE = pair.getValue().get(0);
                Node secondE = pair.getValue().get(1);
                if((graph.areNeighbors(firstI, firstE) && graph.areNeighbors(secondI, secondE)) ||
                        (graph.areNeighbors(firstI, secondE) && graph.areNeighbors(secondI, firstE))) {
                    List<Node> result = new ArrayList<>();
                    result.add(firstE);
                    result.add(key);
                    result.add(secondE);
                    return result;
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Node> getRightSide(Graph graph, Node rootNode, Node samePosition1, Node samePosition2) {
        List<Node> iRootNodeNeighbors = graph.getNeighbors(rootNode, "I");
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
                if(iValue != null && iValue.size() == 1 && eValue != null && eValue.size() == 1) {
                    neighborhood.put(key, new Pair<>(iValue, eValue));
                }
            });
            if(neighborhood.keySet().isEmpty()) continue;
            for (Node key : neighborhood.keySet()) {
                Node eValue = eNeighborhood.get(key).get(0);
                Node iValue = iNeighborhood.get(key).get(0);
                if(key.getX() == samePosition1.getX() && key.getY() == samePosition1.getY()) {
                    if(eValue.getX() == samePosition2.getX() && eValue.getY() == samePosition2.getY()) {
                        List<Node> result = new ArrayList<>();
                        result.add(key);
                        result.add(iValue);
                        result.add(eValue);
                        return result;
                    }
                }
                if(key.getX() == samePosition2.getX() && key.getY() == samePosition2.getY()) {
                    if(eValue.getX() == samePosition1.getX() && eValue.getY() == samePosition1.getY()) {
                        List<Node> result = new ArrayList<>();
                        result.add(eValue);
                        result.add(iValue);
                        result.add(key);
                        return result;
                    }
                }
            }
        }
        return new ArrayList<>();
    }
}
