package production;

import graph.Graph;
import graph.Node;
import util.Pair;
import util.Triple;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Production4 implements Production {
    private final String preApplicationInsideLabel = "I";
    private final String postApplicationInsideLabel = "i";
    private final NodeComparator nodeComparator = new NodeComparator();

    @Override
    public Graph apply(Graph graph) {
        Optional<Node> applicable = graph.getNodes().stream()
                .filter(node -> preApplicationInsideLabel.equals(node.getLabel()))
                .filter(this::matchesPredicate).findAny();

        if (applicable.isEmpty()) {
            System.out.println("Production 4 didn't find anything to run on");
            return graph;
        }

        return transform(applicable.get(), graph);
    }

    private Graph transform(Node applicable, Graph graph) {
        int newLevel = applicable.getLevel() + 1;

        applicable.setLabel(postApplicationInsideLabel);
        var nodesBetweenTwo = getNodeBetweenTwo(applicable).stream().findFirst().get();

        var newNodesBetweenTwo = Stream.of(nodesBetweenTwo.get1(), nodesBetweenTwo.get3()).map(node -> new Node(node, newLevel)).collect(Collectors.toList());
        newNodesBetweenTwo.forEach(graph::addNode);

        var newOuterTopNode = new Node(nodesBetweenTwo.get2(), newLevel);
        graph.addNode(newOuterTopNode);

        var otherNodes = applicable.getNeighbors().stream()
                .filter(e -> !e.equals(nodesBetweenTwo.get1()) && !e.equals(nodesBetweenTwo.get3())).collect(Collectors.toList());

        var newOtherNodes = otherNodes.stream()
                .map(node -> new Node(node, newLevel))
                .sorted(nodeComparator)
                .collect(Collectors.toList());

        newOtherNodes.forEach(graph::addNode);

        var coordinates = otherNodes.stream()
                .map(e -> new Pair<>(e.getX(), e.getY()))
                .reduce((a, b) -> new Pair<>((a.getKey() + b.getKey()) / 2, (a.getValue() + b.getValue()) / 2))
                .orElseGet(() -> new Pair<>(0f, 0f));
        var newOuterBottomNode = new Node("E", coordinates.getKey(), coordinates.getValue(), newLevel);
        graph.addNode(newOuterBottomNode);

        var leftIx = (nodesBetweenTwo.get1().getX() + newOuterTopNode.getX()) / 2;
        var leftIy = (nodesBetweenTwo.get1().getY() + newOtherNodes.get(0).getY()) / 2;
        var leftI = new Node("I", leftIx, leftIy, newLevel);
        graph.addNode(leftI);

        var rightIx = (nodesBetweenTwo.get3().getX() + newOuterTopNode.getX()) / 2;
        var rightIy = (newOuterTopNode.getY() + newOuterBottomNode.getY()) / 2;
        var rightI = new Node("I", rightIx, rightIy, newLevel);
        graph.addNode(rightI);

        graph.addEdge(newNodesBetweenTwo.get(0), newOuterTopNode);
        graph.addEdge(newOuterTopNode, newNodesBetweenTwo.get(1));
        graph.addEdge(newNodesBetweenTwo.get(1), newOtherNodes.get(1));
        graph.addEdge(newNodesBetweenTwo.get(0), newOtherNodes.get(0));
        graph.addEdge(newOuterTopNode, newOuterBottomNode);

        graph.addEdge(leftI, newNodesBetweenTwo.get(0));
        graph.addEdge(leftI, newOuterTopNode);
        graph.addEdge(leftI, newOtherNodes.get(0));
        graph.addEdge(leftI, newOuterBottomNode);

        graph.addEdge(rightI, newOuterTopNode);
        graph.addEdge(rightI, newOuterBottomNode);
        graph.addEdge(rightI, newOtherNodes.get(1));

        graph.addEdge(newOtherNodes.get(0), newOuterBottomNode);
        graph.addEdge(newOtherNodes.get(1), newOuterBottomNode);

        graph.addEdge(applicable, leftI);
        graph.addEdge(applicable, rightI);

        return graph;
    }

    private Boolean matchesPredicate(Node inner) {
        boolean neighborsAreE = inner.getNeighbors().stream().allMatch(node -> node.getLabel().equals("E"));
        return neighborsAreE &&
                are4NodesConnected(inner) &&
                isExactlyOneOtherNodeNotConnected(inner);
    }

    private Set<Triple<Node, Node, Node>> getNodeBetweenTwo(Node inner) {
        Set<Triple<Node, Node, Node>> innerENodes = new HashSet<>();
        for (Node n1 : inner.getNeighbors()) {
            for (Node n2 : inner.getNeighbors()) {
                if (n1 != n2) {
                    var n2Neighbors = new HashSet<>(n2.getNeighbors());
                    Set<Node> intersection = n1.getNeighbors().stream()
                            .filter(n2Neighbors::contains)
                            .collect(Collectors.toSet());
                    intersection.remove(inner);

                    if (intersection.size() == 1
                            && isExactlyInTheMiddle(n1, intersection.stream().findFirst().get(), n2)
                            && intersection.stream().findFirst().get().getNeighbors().stream().noneMatch(e -> e.equals(inner))
                    ) {
                        var left = nodeComparator.compare(n1, n2) < 0 ? n1 : n2;
                        var right = nodeComparator.compare(n1, n2) < 0 ? n2 : n1;
                        innerENodes.add(new Triple<>(left, intersection.stream().findFirst().get(), right));
                    }
                }
            }
        }
        return innerENodes;

//        Set<Triple<Node, Node, Node>> innerENodes = new HashSet<>();
//        for (Node prev : inner.getNeighbors()) {
//            for (Node current : inner.getNeighbors()) {
//                for (Node next : inner.getNeighbors()) {
//                    if (isExactlyInTheMiddle(prev, current, next)) {
//                        var left = nodeComparator.compare(prev, next) < 0 ? prev : next;
//                        var right = nodeComparator.compare(prev, next) < 0 ? next : prev;
//                        innerENodes.add(new Triple<>(left, current, right));
//                    }
//                }
//            }
//        }
//        return innerENodes;
    }


    private Boolean isExactlyInTheMiddle(Node prev, Node current, Node next) {
        return prev.getX() + next.getX() == 2 * current.getX() && prev.getY() + next.getY() == 2 * current.getY();
    }

    private Boolean isExactlyOneOtherNodeNotConnected(Node inner) {
        return getNodeBetweenTwo(inner).size() == 1;
    }

    private Boolean are4NodesConnected(Node inner) {
        return inner.getNeighbors().size() == 4;
    }

    protected static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node lhs, Node rhs) {
            int byY = Float.compare(lhs.getY(), rhs.getY());
            int byX = Float.compare(lhs.getX(), rhs.getX());
            if (byY == 0) {
                return byX;
            }
            return byY;
        }
    }
}
