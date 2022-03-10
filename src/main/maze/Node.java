package main.maze;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

    public int y;
    public int x;
    public double weight = 1.0;
    public Node parent = null;
    public List<Node> neighbors;

    public Point getCoordinates() {
        return new Point(y, x);
    }

    // Delete?
    /*
    public static class Edge {

        public int weight;
        public Node node;

        Edge(int weight, Node node) {
            this.weight = weight;
            this.node = node;
        }
    }
    */
    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    public double h;

    public Node(double h) {
        this.h = h;
        this.neighbors = new ArrayList<>();
    }

    public int getX() {return x;}
    public int getY() {return y;}

    public Node(int x, int y, Node parent, double h) {
        this.y = y;
        this.x = x;
        this.h = h;
        this.parent = parent;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.f);
    }

    public boolean equals(Node other) {
        return this.getCoordinates() == other.getCoordinates();
    }

    // TODO: delete?
    /*
    public void addBranch(int weight, Node node) {
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }
    */

    public double calculateHeuristic(Node target) {
        return this.h;
    }
}