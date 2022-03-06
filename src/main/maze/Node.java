package main.maze;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

    private static int idCounter = 0;
    public int id;
    public Node parent = null;
    public List<Edge> neighbors;

    public static class Edge {

        public int weight;
        public Node node;

        Edge(int weight, Node node) {
            this.weight = weight;
            this.node = node;
        }
    }

    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    public double h;

    public Node(double h) {
        this.h = h;
        this.id = idCounter++;
        this.neighbors = new ArrayList<>();
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.f);
    }

    public void addBranch(int weight, Node node) {
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }

    public double calculateHeuristic(Node target) {
        return this.h;
    }
}