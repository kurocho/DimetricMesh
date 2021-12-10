import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {


    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx");

        Graph graph = new SingleGraph("Tutorial 1");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("AD", "A", "D");

        graph.display();
    }

}
