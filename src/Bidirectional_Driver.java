/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class Bidirectional_Driver.java
 * 
 * Driver for running bi-directional A Star search.
 */

import edu.princeton.cs.algs4.StdDraw;

public class Bidirectional_Driver {
    private static final int DIM = 24; // maze dimension

    public static void main(String[] args) {
        Maze maze = new Maze(DIM);
        Bidirectional biD = new Bidirectional(maze);
        Display display = new Display(DIM);
        try {
            biD.thread0.join();
            biD.thread1.join();
        } catch (InterruptedException e) {
            return;
        }
        
        display.animateShortestPath(biD.biDPathList[0], StdDraw.GREEN, 0.25);
        display.animateShortestPath(biD.biDPathList[1], StdDraw.RED, 0.15);
        int pathCount0 = display.getShortestPathLength(biD.biDPathList[0]);
        int pathCount1 = display.getShortestPathLength(biD.biDPathList[1]);
        System.out.println("Bi-directional combined shortest path count: " + pathCount0 + " + " + pathCount1 + 
            " + 1 (path connection) = " + (pathCount0 + pathCount1 + 1));
    }
}