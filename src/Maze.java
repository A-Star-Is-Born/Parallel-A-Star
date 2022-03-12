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
    private final int n;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;
    private final Node[][] nodeGrid;
    private static final double RADIUS = 0.375;

    private boolean done = false;

    private final Node start;
    private final Node target;

    public Maze(int n) {
        this.n = n;
        StdDraw.setXscale(0, n+2);
        StdDraw.setYscale(0, n+2);
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
        draw();
    }


    private void init() {
        // initialize border cells as already visited
        visited = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            visited[x][0] = true;
            visited[x][n+1] = true;
        }
        for (int y = 0; y < n+2; y++) {
            visited[0][y] = true;
            visited[n+1][y] = true;
        }


        // initialize all walls as present
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


    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                double r = StdRandom.uniform(4);
                if (r == 0 && !visited[x][y+1]) {
                    north[x][y] = false;
                    south[x][y+1] = false;
                    generate(x, y + 1);
                    break;
                }
                else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !visited[x][y-1]) {
                    south[x][y] = false;
                    north[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);
/*
        // delete some random walls
        for (int i = 0; i < n; i++) {
            int x = 1 + StdRandom.uniform(n-1);
            int y = 1 + StdRandom.uniform(n-1);
            north[x][y] = south[x][y+1] = false;
        }

        // add some random walls
        for (int i = 0; i < 10; i++) {
            int x = n/2 + StdRandom.uniform(n/2);
            int y = n/2 + StdRandom.uniform(n/2);
            east[x][y] = west[x+1][y] = true;
        }
*/
    }

    // solve the maze using depth-first search
    // TODO: Delete this after we have a better understanding of how to use all this
    private void solve(int x, int y) {

        if (x == 0 || y == 0 || x == n+1 || y == n+1) return;
        if (done || visited[x][y]) return;
        visited[x][y] = true;

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);

        // reached middle
        // TODO: note that this does not go to our new target location
        if (x == n/2 && y == n/2) done = true;

        if (!north[x][y]) solve(x, y + 1);
        if (!east[x][y])  solve(x + 1, y);
        if (!south[x][y]) solve(x, y - 1);
        if (!west[x][y])  solve(x - 1, y);

        if (done) return;

        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);
    }

    // solve the maze starting from the start state
    public void solve() {
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                visited[x][y] = false;
        done = false;
        solve(1, 1);
    }


    // draw the maze
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

    // public boolean notWall(boolean[][] arr, int x, int y) {return !(arr[x][y]);}


    public Node getTarget() {
        return target;
    }

    public Node getStart() {
        return start;
    }

    public ArrayList<Node> getNeighbors(Node current) {
        int x = current.getX();
        int y = current.getY();
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

    public int getHeuristic(int x, int y, Point finish) {
        // TODO: switch to double instead of int, surely?
        int theY = abs(y - finish.y);
        int theX = abs(x - finish.x);
        return theY + theX;
    }


    // a test client
    public static void main(String[] args) {
        // int n = 8; // Integer.parseInt(args[0]);
        Maze maze = new Maze(4);
        StdDraw.enableDoubleBuffering();
        maze.draw();
        // maze.report(2, 2);
        maze.solve();
    }

}