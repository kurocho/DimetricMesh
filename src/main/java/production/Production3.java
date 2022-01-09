package production;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Production3 implements Production {
    public Graph apply(Graph graph){
        Node nodeI = null;
        List<Node> nodesE = new LinkedList<>();
        for (Node node : graph.getNodes()) {
            if ("I".equals(node.getLabel())) {
                for (Node neighbor : node.getNeighbors()) {
                    if ("E".equals(neighbor.getLabel())) {
                        nodesE.add(neighbor);
                    }
                }
                if (areFormingRectangle(nodesE)) {
                    nodeI = node;
                    break;
                } else {
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

        Edge edgeToBreak1 = getTopHorizontalEdge(nodesE);
        Edge edgeToBreak2 = getRightNeighbour(nodesE, edgeToBreak1);
        Edge edgeToBreak3 = getParallel(nodesE, edgeToBreak1);
        Edge edgeToBreak4 = getParallel(nodesE, edgeToBreak2);

        float midX, midY;
        midX = (edgeToBreak1.getStart().getX() + edgeToBreak1.getEnd().getX())/2;
        midY = (edgeToBreak2.getStart().getY() + edgeToBreak2.getEnd().getY())/2;

        float x1, x2, y1, y2;
        x1 = edgeToBreak4.getStart().getX();
        x2 = edgeToBreak2.getStart().getX();
        y1 = edgeToBreak1.getStart().getY();
        y2 = edgeToBreak3.getStart().getY();

        //new nodes
        Node I1 = new Node("I", (x1+midX)/2, (y1+midY)/2, newLevel);
        Node I2 = new Node("I", (x2+midX)/2, (y1+midY)/2, newLevel);
        Node I3 = new Node("I", (x1+midX)/2, (y2+midY)/2, newLevel);
        Node I4 = new Node("I", (x2+midX)/2, (y2+midY)/2, newLevel);

        Node E_x1_y1 = new Node("E", x1, y1, newLevel);
        Node E_x1_y2 = new Node("E", x1, y2, newLevel);
        Node E_x2_y1 = new Node("E", x2, y1, newLevel);
        Node E_x2_y2 = new Node("E", x2, y2, newLevel);

        Node E_midX_y1 = new Node("E", midX, y1, newLevel);
        Node E_midX_y2 = new Node("E", midX, y2, newLevel);
        Node E_x1_midY = new Node("E", x1, midY, newLevel);
        Node E_x2_midY = new Node("E", x2, midY, newLevel);
        Node E_midX_midY = new Node("E", midX, midY, newLevel);

        //
        graph.addNode(I1);
        graph.addNode(I2);
        graph.addNode(I3);
        graph.addNode(I4);

        graph.addNode(E_x1_y1);
        graph.addNode(E_x1_y2);
        graph.addNode(E_x2_y1);
        graph.addNode(E_x2_y2);

        graph.addNode(E_midX_y1);
        graph.addNode(E_midX_y2);
        graph.addNode(E_x1_midY);
        graph.addNode(E_x2_midY);
        graph.addNode(E_midX_midY);

        graph.addEdge(nodeI, I1);
        graph.addEdge(nodeI, I2);
        graph.addEdge(nodeI, I3);
        graph.addEdge(nodeI, I4);

        graph.addEdge(I1, E_x1_y1);
        graph.addEdge(I1, E_midX_y1);
        graph.addEdge(I1, E_x1_midY);
        graph.addEdge(I1, E_midX_midY);

        graph.addEdge(I2, E_midX_y1);
        graph.addEdge(I2, E_x2_y1);
        graph.addEdge(I2, E_midX_midY);
        graph.addEdge(I2, E_x2_midY);

        graph.addEdge(I3, E_x1_midY);
        graph.addEdge(I3, E_midX_midY);
        graph.addEdge(I3, E_x1_y2);
        graph.addEdge(I3, E_midX_y2);

        graph.addEdge(I4, E_midX_midY);
        graph.addEdge(I4, E_x2_midY);
        graph.addEdge(I4, E_midX_y2);
        graph.addEdge(I4, E_x2_y2);

        graph.addEdge(E_x1_y1, E_midX_y1);
        graph.addEdge(E_midX_y1, E_x2_y1);
        graph.addEdge(E_x2_y1, E_x2_midY);
        graph.addEdge(E_x2_midY, E_x2_y2);
        graph.addEdge(E_x2_y2, E_midX_y2);
        graph.addEdge(E_midX_y2, E_x1_y2);
        graph.addEdge(E_x1_y2, E_x1_midY);
        graph.addEdge(E_x1_midY, E_x1_y1);

        graph.addEdge(E_midX_y1, E_midX_midY);
        graph.addEdge(E_midX_y2, E_midX_midY);
        graph.addEdge(E_x1_midY, E_midX_midY);
        graph.addEdge(E_x2_midY, E_midX_midY);

        return graph;
    }


    private Edge getTopHorizontalEdge(List<Node> nodes) {
        Node topYNode = nodes.get(0);
        for (Node n: nodes) {
            if(n.getY() > topYNode.getY()) {
                topYNode = n;
            }
        }
        for(Edge e: topYNode.getEdges()) {
            if(e.getStart().getY() == e.getEnd().getY()) {
                return e;
            }
        }
        // in case no horizontal edge found
        throw new IllegalStateException("No horizontal edge found");
    }

    private Edge getRightNeighbour(List<Node> nodes, Edge firstEdge) {
        Node rightNode = firstEdge.getStart().getX() > firstEdge.getEnd().getX() ? firstEdge.getStart() : firstEdge.getEnd();
        for (Edge e:rightNode.getEdges()) {
            if(e != firstEdge &&
                    firstEdge.getStart().getLabel().equals("E") &&
                    e.getEnd().getLabel().equals("E") &&
                    e.getStart().getX() == e.getEnd().getX() &&
                    e.getStart().getY() != e.getEnd().getY()) {
                return e;
            }
        }
        throw new IllegalStateException("No right neighbour found");
    }

    private Edge getParallel(List<Node> nodes, Edge e) {
        List<Node> tempNodes = new LinkedList<>(nodes);
        tempNodes.remove(e.getStart());
        tempNodes.remove(e.getEnd());
        //there should be only 2 left
        Node n1 = tempNodes.get(0);
        Node n2 = tempNodes.get(1);

        List<Edge> allEdges = n1.getEdges();

        return n1.getEdges()
                .stream()
                .filter(edge -> edge.getDestination(n1) == n2)
                .findFirst().orElse(null);
    }

    private boolean areFormingRectangle(List<Node> nodes) {
        if (nodes.size() != 4) return false;
        List<Float> xs = new ArrayList<Float>();
        List<Float> ys = new ArrayList<Float>();
        for (Node n : nodes) {
            //every Node has 2 neighbours from list
            if (n.getNeighbors().stream().filter(node -> nodes.contains(node)).count() != 2) return false;
            xs.add(n.getX());
            ys.add(n.getY());
        }

        if(xs.stream().distinct().count() != 2) return false;
        if(ys.stream().distinct().count() != 2) return false;

        for (Node n: nodes) {
            if(xs.stream().filter(x -> x == n.getX()).count() != 2) return false;
            if(ys.stream().filter(x -> x == n.getY()).count() != 2) return false;
        }

        return true;
    }
}
