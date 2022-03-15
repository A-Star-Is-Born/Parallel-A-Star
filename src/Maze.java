/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class Maze.java
 * 
 * Creates the graphical maze that A Star searches through.
 * Mazes are square and have a start and end (SW and NE corners).
 * There is a 1 cell border around the edges.
 * 
 * Most of code for maze creation was borrowed. See paper for reference.
 */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

/******************************************************************************
 *  Compilation:  javac Maze.java
 *  Execution:    java Maze.java n
 *  Dependencies: StdDraw.java
 *
 *  Generates a perfect n-by-n maze using depth-first search with a stack.
 *
 *  % java Maze 62
 *
 *  % java Maze 61
 *
 *  Note: this program generalizes nicely to finding a random tree
 *        in a graph.
 *
 ******************************************************************************/

public class Maze {
    private final int n; // dimension of maze
    private boolean[][] north; // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited; // if the path has been to this cell before
    private final Node[][] nodeGrid; // grid itself, no border included
    private static final double RADIUS = 0.375; // start/end circle radius
    private Node start; // start Node
    private Node target; // end Node

    /**
     * Constructor for Maze.
     *
     * @param n dimension
     */
    public Maze(int n) {
        this.n = n;
        // StdDraw.setXscale(0, n+2);
        // StdDraw.setYscale(0, n+2);
        nodeGrid = new Node[n+2][n+2];
        Point targetPoint = new Point(n, n);

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                nodeGrid[i][j] = new Node(i, j, null, getHeuristic(i, j, targetPoint));
            }
        }

        start = nodeGrid[1][1];
        target = nodeGrid[n][n];

        init();
        generate();
        // draw();
    }

    /**
     * Initialize walls, and border cells as already visited.
     */
    private void init() {
        visited = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            visited[x][0] = true;
            visited[x][n+1] = true;
        }
        for (int y = 0; y < n+2; y++) {
            visited[0][y] = true;
            visited[n+1][y] = true;
        }

        north = new boolean[n+2][n+2];
        east  = new boolean[n+2][n+2];
        south = new boolean[n+2][n+2];
        west  = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < n+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }
    }

    /**
     * Generates random corrirods, reworked from the original recursion based method.
     * @param origin the point we calculate from.
     */
    // generate maze using a stack instead of recursion
    private void generate(Point origin) {
        Stack<Point> sharp = new Stack<>();
        sharp.push(origin);
        int x;
        int y;
        Point current;
        while (!sharp.isEmpty()) {
            current = sharp.peek();
            x = current.x;
            y = current.y;
            visited[x][y] = true;
            if (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {
                while (true) {
                    double r = StdRandom.uniform(4);
                    if (r == 0 && !visited[x][y+1]) {
                        north[x][y] = false;
                        south[x][y+1] = false;
                        sharp.push(new Point(x, y+ 1));
                        break;
                    }
                    else if (r == 1 && !visited[x+1][y]) {
                        east[x][y] = false;
                        west[x+1][y] = false;
                        sharp.push(new Point(x + 1, y));
                        break;
                    }
                    else if (r == 2 && !visited[x][y-1]) {
                        south[x][y] = false;
                        north[x][y-1] = false;
                        sharp.push(new Point(x, y - 1));
                        break;
                    }
                    else if (r == 3 && !visited[x-1][y]) {
                        west[x][y] = false;
                        east[x-1][y] = false;
                        sharp.push(new Point(x - 1, y));
                        break;
                    }
                }
            } else {
                sharp.pop();
            }
        }
    }

    /**
     * Generate maze from SW corner.
     * This may be causing less/no forks at that corner while the end has forks;
     * this could be causing thread 0 in bi-directional to have easy pathfinding.
     */
    private void generate() {
        generate(new Point(1, 1));
    }

    /**
     * Draws the start and end circles.
     * Draws maze walls.
     */
    public void draw() {
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledCircle(1.5, 1.5, RADIUS);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(target.x + 0.5, target.y + 0.5, RADIUS);

        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) StdDraw.line(x, y, x+1, y);
                if (north[x][y]) StdDraw.line(x, y+1, x+1, y+1);
                if (west[x][y])  StdDraw.line(x, y, x, y+1);
                if (east[x][y])  StdDraw.line(x+1, y, x+1, y+1);
            }
        }
        StdDraw.show();
        StdDraw.pause(50);
    }

    /**
     * @return end Node
     */
    public Node getTarget() {
        return target;
    }

    /**
     * @return start Node
     */
    public Node getStart() {
        return start;
    }

    /**
     * Reports all the accessible neighboring Nodes of a given Node.
     * 
     * @param current Node to get neighbors of
     * @return ArrayList of neighboring Nodes
     */
    public ArrayList<Node> getNeighbors(Node current) {
        int x = current.x;
        int y = current.y;
        ArrayList<Node> retList = new ArrayList<>(4);
        if (!north[x][y])
            retList.add(nodeGrid[x][y + 1]);
        if (!east[x][y])
            retList.add(nodeGrid[x + 1][y]);
        if (!south[x][y])
            retList.add(nodeGrid[x][y - 1]);
        if (!west[x][y])
            retList.add(nodeGrid[x - 1][y]);
        return retList;
    }

    /**
     * Calculate remaining distance hueristically to end.
     * 
     * @param x current x
     * @param y current y
     * @param finish end Point
     * @return heuristic distance to end
     */
    public int getHeuristic(int x, int y, Point finish) {
        int theY = abs(y - finish.y);
        int theX = abs(x - finish.x);
        return theY + theX;
    }
}