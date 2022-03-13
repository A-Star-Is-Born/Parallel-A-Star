import edu.princeton.cs.algs4.StdDraw;

public class Parallel_AStar {
    private static final int DIM = 12;

    public static void main(String[] args) {
        Maze maze = new Maze(DIM);
        AStar aStar = new AStar(maze);
        Display display = new Display(DIM);

        Node res = aStar.run();
        
        String printable = display.getPath(res);
        System.out.println(printable);
        display.print(res);
        
        System.out.println("Shortest path length: " + display.getShortestPathLength(res));
        
        display.animateShortestPath(res, StdDraw.BLUE, 0.25);
    }
}