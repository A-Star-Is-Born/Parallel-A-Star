/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class Node.java
 * 
 * Basic Node (cell) of a maze, tracking coordinates, weight, a parent Node, and if seen.
 */

import java.awt.*;

public class Node implements Comparable<Node> {

    public int y; // y-coordinate in maze
    public int x; // x-coordinate in maze
    public double weight = 1.0; // default distance of this Node; contributes to "g" (traveled distance to a Node)
    public Node parent; // the Node that preceeded this one
    public boolean[] seen; // whether a thread has seen this Node as a neighbor of a current Node

    /**
     * Constructs Node.
     *
     * @param x maze to traverse
     */
    public Node(int x, int y, Node parent, double h) {
        this.y = y;
        this.x = x;
        this.h = h;
        this.parent = parent;
        seen = new boolean[2];
    }

    /**
     * @return a Point with this Node's coordinates
     */
    public Point getCoordinates() {
        return new Point(x, y);
    }

    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    public double h;

    /**
     * @return copy of this Node
     */
    public Node clone() {
        return new Node(x, y, parent, h);
    }

    /**
     * Comparator for Node.
     * 
     * @param o other Node
     * @return comparison
     */
    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.f);
    }

}