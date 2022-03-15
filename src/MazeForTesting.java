import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class MazeForTesting {
    private final int n;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;
    private final Node[][] nodeGrid;
    private static final double RADIUS = 0.375;

    private boolean done = false;

    private Node start;
    private Node target;

    public MazeForTesting(int n) {
        this.n = n;
        nodeGrid = new Node[n + 2][n + 2];

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
    }


    private void init() {
        // initialize border cells as already visited
        visited = new boolean[n + 2][n + 2];
        for (int x = 0; x < n + 2; x++) {
            visited[x][0] = true;
            visited[x][n + 1] = true;
        }
        for (int y = 0; y < n + 2; y++) {
            visited[0][y] = true;
            visited[n + 1][y] = true;
        }


        // initialize all walls as present
        north = new boolean[n + 2][n + 2];
        east = new boolean[n + 2][n + 2];
        south = new boolean[n + 2][n + 2];
        west = new boolean[n + 2][n + 2];
        for (int x = 0; x < n + 2; x++) {
            for (int y = 0; y < n + 2; y++) {
                north[x][y] = true;
                east[x][y] = true;
                south[x][y] = true;
                west[x][y] = true;
            }
        }
    }


    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y + 1] || !visited[x + 1][y] || !visited[x][y - 1] || !visited[x - 1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                double r = StdRandom.uniform(4);
                if (r == 0 && !visited[x][y + 1]) {
                    north[x][y] = false;
                    south[x][y + 1] = false;
                    generate(x, y + 1);
                    break;
                } else if (r == 1 && !visited[x + 1][y]) {
                    east[x][y] = false;
                    west[x + 1][y] = false;
                    generate(x + 1, y);
                    break;
                } else if (r == 2 && !visited[x][y - 1]) {
                    south[x][y] = false;
                    north[x][y - 1] = false;
                    generate(x, y - 1);
                    break;
                } else if (r == 3 && !visited[x - 1][y]) {
                    west[x][y] = false;
                    east[x - 1][y] = false;
                    generate(x - 1, y);
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
        int theY = abs(y - finish.y);
        int theX = abs(x - finish.x);
        return theY + theX;
    }

}
