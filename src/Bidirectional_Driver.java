import edu.princeton.cs.algs4.StdDraw;

public class Bidirectional_Driver {
    private static final int DIM = 6;

    public static void main(String[] args) {
        // Bi-directional
        Maze maze = new Maze(DIM);
        Bidirectional biD = new Bidirectional(maze);
        Display display = new Display(DIM);
        try {
            biD.thread1.join();
            biD.thread2.join();
        } catch (InterruptedException e) {
            return;
        }
        
        display.animateShortestPath(biD.biDPathList[0], StdDraw.BLUE, 0.25);
        display.animateShortestPath(biD.biDPathList[1], StdDraw.ORANGE, 0.15);
        int pathCount1 = display.getShortestPathLength(biD.biDPathList[0]);
        int pathCount2 = display.getShortestPathLength(biD.biDPathList[1]);
        System.out.println("Bi-directional combined shortest path count: " + pathCount1 + " + " + pathCount2 + 
            " + 1 (path connection) = " + (pathCount1 + pathCount2 + 1));
    }
}