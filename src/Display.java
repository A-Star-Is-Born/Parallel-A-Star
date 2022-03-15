/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class Display.java
 * 
 * Utilities for displaying A Star path information to the console and graphically.
 */

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class Display {
    private int dim;
    private String[][] consoleGrid; // visual grid printed to console

    /**
     * Constructs Display.
     *
     * @param dim maze dimension
     */
    public Display (int dim) {
        this.dim = dim;
        consoleGrid = new String[dim][dim];
    }

    /**
     * Prints 2D maze path to console.
     * 
     * @param target final Node
     */
    public void printMazePath(Node target) {
        System.out.println("\n");
        ArrayList<Node> path = definePath(target);

        for(int l = 0; l < path.size(); l++) {
            Node curr = path.get(l), next;
            String toUse = "*";

            if (l == 0) {
                toUse = "S";
            } else if (l == path.size() - 1) {
                toUse = "E";
            } else {
                next = path.get(l + 1);
                if (next.x - curr.x == 1) {
                    toUse = ">";
                }
                if (next.x - curr.x == -1) {
                    toUse = "<";
                }
                if (next.y - curr.y == 1) {
                    toUse = "^";
                }
                if (next.y - curr.y == -1) {
                    toUse = "V";
                }
            }
               
            consoleGrid[curr.y - 1][curr.x - 1] = toUse;
        }
        Collections.reverse(Arrays.asList(consoleGrid));

        for (int y = -1; y < dim + 1; y++) {
                System.out.print("|");
            for (int x = 0; x < dim; x++) {
                if (y == -1 || y == dim) {
                    System.out.print("-");
                } else {
                    System.out.print(consoleGrid[y][x] == null ? " " : consoleGrid[y][x]);
                }
            }
            System.out.print("|\n");
        }

        System.out.println("\n");
    }

    /**
     * Prints path as list of coordinates to console.
     * 
     * @param target final Node
     */
    public void printPathAsList(Node target){
        StringBuilder str = new StringBuilder();
        ArrayList<Node> path = definePath(target);

        for(int l = 0; l < path.size(); l++) {
            if (l == 0) {
                str.append("Start: ");
            }
            str.append("[" + path.get(l).x + "][" + path.get(l).y + "]" + (l == path.size() - 1 ? " (End)" : "-->"));
        }

        System.out.println(str.toString());
    }

    /**
     * @return shortest path length
     */
    public int getShortestPathLength(Node target) {
        Node n = target;
        int count = 0;

        if(n==null)
            System.out.println("Something went wrong");

        while(n.parent != null){
            count++;
            n = n.parent;
        }

        return count;
    }

    /**
     * Graphically animates the shortest path in the maze.
     */
    public void animateShortestPath(Node target, Color c, double size) {
        ArrayList<Node> path = definePath(target);

        for (Node curr : path) {
            StdDraw.setPenColor(c);
            StdDraw.filledCircle(curr.x + 0.5, curr.y + 0.5, size);
            StdDraw.show();
            StdDraw.pause(30);
        }
    }

    /**
     * @return ArrayList of path Nodes in order from start to target
     */
    private ArrayList<Node> definePath(Node target) {
        Node n = target;
        ArrayList<Node> path = new ArrayList<>();

        if (n == null)
            System.out.println("Something went wrong");

        while (n.parent != null) {
            path.add(n);
            n = n.parent;
        }
        path.add(n);
        Collections.reverse(path);
        return path;
    }
}
