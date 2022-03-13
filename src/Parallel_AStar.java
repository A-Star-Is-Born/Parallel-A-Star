import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class Parallel_AStar {
    private static final int DIM = 4;

    public static void main(String[] args) {

        //Initializing. Not super relevant now
        Maze maze = new Maze(DIM);
        // AStar aStar = new AStar(maze);
        Bidirectional biD = new Bidirectional(maze);
        Display display = new Display(DIM);
        try {
            biD.thread1.join();
            biD.thread2.join();
        } catch (InterruptedException e) {
            return;
        }
        Node t1c = biD.biDPathList[0].clone();
        System.out.println("Thread0 path: ");
        while (t1c != null) {
            System.out.print("[" + t1c.x + "][" + t1c.y + "]-->");
            t1c = t1c.parent;
        }

        Node t2c = biD.biDPathList[1].clone();
        System.out.println("\nThread1 path: ");
        while (t2c != null) {
            System.out.print("[" + t2c.x + "][" + t2c.y + "]-->");
            t2c = t2c.parent;
        }
        
        display.animateShortestPath(biD.biDPathList[0], StdDraw.BLUE, 0.25);
        display.animateShortestPath(biD.biDPathList[1], StdDraw.ORANGE, 0.15);
        System.out.println("------COMPLETE------");
        // Display display2 = new Display(DIM);

        // Take AStar, and ask it to run the maze.
        // Astar will ask for a start and finish location
        // Node res = aStar.run();
        
        // Do we even want a display path? Not sure.
        // might make sense to just turn it into testing only.
        // String printable = display.getPath(res);
        // System.out.println(printable);
        // display.print(res);
        
        // System.out.println("Shortest path length: " + display.getShortestPathLength(res));
        
        // display.animateShortestPath(res, StdDraw.BLUE, 0.25);
        // ArrayList<Node> res1List = display.getShortestPathList(res);
        // maze.clearNodeParents();
        // aStar.swap();
        // aStar.print();
        // Node res2 = aStar.run();
        // ArrayList<Node> res2List = display.getShortestPathList(res2);
        // display2.print(res2);
        // display.animateShortestPath(res2, StdDraw.ORANGE, 0.15);
        
        // int i = 0;
        // while (res1List.get(i) != res2List.get(i)) {
        //     System.out.println("e1: [" + res1List.get(i).x + "][" + res1List.get(i).y + "] - e2: [" + res2List.get(i).x + "][" + res2List.get(i).y + "]");
        //     i++;
        // }
        // System.out.println("MET IN MIDDLE AT: [" + res1List.get(i).x + "][" + res1List.get(i).y + "]");
    }

}
