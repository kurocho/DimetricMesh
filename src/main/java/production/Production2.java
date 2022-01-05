package production;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.LinkedList;
import java.util.List;

public class Production2 implements Production {

    public enum BreakMode {
        ANY,
        HORIZONTAL,
        VERTICAL
    }

    private final BreakMode breakMode;

    public Production2() {
        this.breakMode = BreakMode.ANY;
    }

    public Production2(BreakMode breakMode) {
        this.breakMode = breakMode;
    }

    @Override
    public Graph apply(Graph graph) {
        Node nodeI = null;
        List<Node> nodesE = new LinkedList<>();
        for (Node node : graph.getNodes()) { //lets find any I
            if ("I".equals(node.getLabel())) {
                for (Node neighbor : node.getNeighbors()) { //lets find all edges with this I and E node
                    if ("E".equals(neighbor.getLabel())) {
                        nodesE.add(neighbor);
                    }
                }
                if (areFormingRectangle(nodesE)) { // check if found edges are forming a rectangle
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

        float oldX = nodeI.getX();
        float oldY = nodeI.getY();

        int newLevel = nodeI.getLevel() + 1;

        nodeI.setLabel("i");

        Edge edgeToBreak1 = getEdgeToBreak(nodesE);
        Edge edgeToBreak2 = getParallelEdgeToBreak(nodesE, edgeToBreak1);
        float newXI1, newYI1, newXI2, newYI2;
        if (edgeToBreak1.getEnd().getY() == edgeToBreak1.getStart().getY()) {
            //if edge to break is horizontal then change X
            newXI1 = (edgeToBreak1.getStart().getX() + oldX) / 2;
            newYI1 = oldY;
            newXI2 = (edgeToBreak1.getEnd().getX() + oldX) / 2;
            newYI2 = oldY;
        } else {
            newYI1 = (edgeToBreak1.getStart().getY() + oldY) / 2;
            newXI1 = oldX;
            newYI2 = (edgeToBreak1.getEnd().getY() + oldY) / 2;
            newXI2 = oldX;
        }

        Node I1 = new Node("I", newXI1, newYI1, newLevel);
        Node I2 = new Node("I", newXI2, newYI2, newLevel);
        graph.addNode(I1);
        graph.addNode(I2);

        //connection to old graph
        graph.addEdge(nodeI, I1);
        graph.addEdge(nodeI, I2);

        //first node
        Node newNodeE = new Node(edgeToBreak1.getStart(), newLevel);
        graph.addNode(newNodeE);
        graph.addEdge(I1, newNodeE);

        //second node, first breaking
        Node newNodeE2 = new Node("E",
                (edgeToBreak1.getStart().getX() + edgeToBreak1.getEnd().getX()) / 2,
                (edgeToBreak1.getStart().getY() + edgeToBreak1.getEnd().getY()) / 2,
                newLevel);
        graph.addNode(newNodeE2);
        graph.addEdge(I1, newNodeE2);
        graph.addEdge(I2, newNodeE2);
        graph.addEdge(newNodeE, newNodeE2);

        //third node
        Node newNodeE3 = new Node(edgeToBreak1.getEnd(), newLevel);
        graph.addNode(newNodeE3);
        graph.addEdge(I2, newNodeE3);
        graph.addEdge(newNodeE2, newNodeE3);

        //should we start from start or end of other edgeToBreak?
        if (graph.getEdges().contains(new Edge(edgeToBreak1.getStart(), edgeToBreak2.getEnd())) ||
                graph.getEdges().contains(new Edge(edgeToBreak2.getEnd(), edgeToBreak1.getStart()))) {
            Node newNodeE4 = copyNextNode(graph, newLevel, I2, newNodeE3, edgeToBreak2.getStart());
            Node newNodeE5 = copySecondBreakingNode(graph, newLevel, edgeToBreak2, I1, I2, newNodeE4, newNodeE2);
            CopyLastNodeE(graph, newLevel, I1, newNodeE5, newNodeE, edgeToBreak2.getEnd());
        } else { // we start from the end
            Node newNodeE4 = copyNextNode(graph, newLevel, I2, newNodeE3, edgeToBreak2.getEnd());
            //breaking node on second edge
            Node newNodeE5 = copySecondBreakingNode(graph, newLevel, edgeToBreak2, I1, I2, newNodeE4, newNodeE2);
            CopyLastNodeE(graph, newLevel, I1, newNodeE5, newNodeE, edgeToBreak2.getStart());
        }
        return graph;
    }

    private Node copyNextNode(Graph graph, int newLevel, Node i, Node prevNode, Node nodeToCopy) {
        Node newNodeE = new Node(nodeToCopy, newLevel);
        graph.addNode(newNodeE);
        graph.addEdge(prevNode, newNodeE); // here we can potentially reverse previous edge
        graph.addEdge(i, newNodeE);
        return newNodeE;
    }

    private void CopyLastNodeE(Graph graph, int newLevel, Node i, Node newNodeE, Node firstNewNodeE, Node start) {
        Node newNodeE2;
        newNodeE2 = new Node(start, newLevel);
        graph.addNode(newNodeE2);
        graph.addEdge(newNodeE, newNodeE2);
        graph.addEdge(i, newNodeE2);
        graph.addEdge(newNodeE2, firstNewNodeE); // here we can potentially reverse previous edge
        System.out.println(newNodeE2.getId());
    }

    private Node copySecondBreakingNode(Graph graph, int newLevel, Edge edgeToBreak, Node i1, Node i2,
                                        Node newNodeE2, Node breakingNodeOn1Edge) {
        Node newNodeE = newNodeE2;
        newNodeE2 = new Node("E",
                (edgeToBreak.getStart().getX() + edgeToBreak.getEnd().getX()) / 2,
                (edgeToBreak.getStart().getY() + edgeToBreak.getEnd().getY()) / 2,
                newLevel);
        graph.addNode(newNodeE2);
        graph.addEdge(newNodeE, newNodeE2);
        graph.addEdge(i2, newNodeE2);
        graph.addEdge(i1, newNodeE2);
        graph.addEdge(breakingNodeOn1Edge, newNodeE2);

        return newNodeE2;
    }

    private Edge getEdgeToBreak(List<Node> nodes) {
        // replace with better logic to support choosing if break should be vertical or horizontal
        Node firstNode = nodes.get(0);
        for (Edge edge : firstNode.getEdges()) {
            Node destination = edge.getDestination(firstNode);
            if (nodes.contains(edge.getDestination(firstNode))) {
                if (breakMode == BreakMode.ANY
                        || (breakMode == BreakMode.VERTICAL && firstNode.getY() == destination.getY())
                        || (breakMode == BreakMode.HORIZONTAL && firstNode.getX() == destination.getX())) {
                    return edge;
                }
            }
        }
        throw new IllegalStateException("Node not connected to other E node");
    }

    private Edge getParallelEdgeToBreak(List<Node> nodes, Edge firstEdge) {
        List<Node> tempNodes = new LinkedList<>(nodes);
        tempNodes.remove(firstEdge.getStart());
        tempNodes.remove(firstEdge.getEnd());
        //there should be only 2 left
        Node n1 = tempNodes.get(0);
        Node n2 = tempNodes.get(1);

        return n1.getEdges()
                .stream()
                .filter(edge -> edge.getDestination(n1) == n2)
                .findFirst().orElse(null);
    }

    private boolean areFormingRectangle(List<Node> nodes) {
        if (nodes.size() != 4) return false;
        for (Node n1 : nodes) {
            for (Node n2 : nodes) {
                if (n1.getId().equals(n2.getId()) ||
                        (n1.getX() != n2.getX() && n1.getY() != n2.getY()))
                    continue;
                if (!n1.getNeighbors().contains(n2) || !n2.getNeighbors().contains(n1)) {
                    return false;
                }
            }
        }
        return true;
    }
}
