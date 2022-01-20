package production;

import graph.Edge;
import graph.Graph;
import graph.Node;
import javafx.util.Pair;

import java.util.*;

public class Production14Hacks implements Production {
    @Override
    public Graph apply(Graph graph) {
        HashMap<Pair<Float, Float>, List<Node>> x = new HashMap<>();

        int maxLevel = graph.getNodes().stream().max(Comparator.comparingInt(Node::getLevel)).get().getLevel();

        graph.getNodes().forEach(node -> {
            if (node.getLevel() == maxLevel) {
                Pair<Float, Float> key = new Pair<>(node.getX(), node.getY());
                List<Node> got = x.get(key);
                if (got == null) {
                    got = new ArrayList<>();
                }
                got.add(node);
                x.put(key, got);
            }
        });

        Optional<List<Node>> onSamePlace = x.values().stream().filter(e -> e.size() > 1 && e.get(0) != e.get(1)).findFirst();

        if (onSamePlace.isEmpty()) {
            System.out.println("Didn't found any nodes to run on");
            return graph;
        }

        Node fst = onSamePlace.get().get(0);
        Node snd = onSamePlace.get().get(1);

        List<Node> common1 = findCommon(fst, snd);
        List<Node> common2 = findCommon(snd, fst);

        if (common1.size() == 1 && common1.get(0) != fst
                && common1.get(0) != snd
                && x.get(new Pair<>((fst.getX() + common1.get(0).getX()) / 2, (fst.getY() + common1.get(0).getY()) / 2)) != null) {
            return run(fst, snd, common1.get(0), graph);
        }

        if (common2.size() == 1 && common2.get(0) != fst && common2.get(0) != snd
                && x.get(new Pair<>((fst.getX() + common2.get(0).getX()) / 2, (fst.getY() + common2.get(0).getY()) / 2)) != null) {
            return run(snd, fst, common2.get(0), graph);
        }

        System.out.println("Didn't found any nodes to run on");
        return graph;
    }

    private Graph run(Node fst, Node snd, Node common, Graph graph) {
        try {
            graph.removeEdge(new Edge(common, fst));
            graph.removeEdge(new Edge(fst, common));
        } catch (Exception ignored) {

        }

        List<Node> connections = fst.getNeighbors();
        graph.removeNode(fst);

        connections.forEach(connection -> {
            if (!connection.equals(snd) && !connection.equals(common)) {
                graph.addEdge(snd, connection);
            }
        });

        return graph;
    }

    private List<Node> findCommon(Node fst, Node snd) {
        List<Node> common = new ArrayList<>();
        fst.getNeighbors().forEach(fsts -> {
            snd.getNeighbors().forEach(snds -> {
                if (snds.getLabel().equals("E")) {
                    snds.getNeighbors().forEach(sndss -> {
                        if (fsts == sndss && fsts.getLabel().equals("E")) {
                            common.add(sndss);
                        }
                    });
                }
            });
        });

        return common;
    }
}
