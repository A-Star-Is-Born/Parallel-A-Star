public class Parallel_AStar {
    private static final int DIM = 4;

    public static void main(String[] args) {

        //Initializing. Not super relevant now
        AStar aStar = new AStar();
        Display display = new Display(DIM);

        // though maze does take a dimension of 10
        Maze maze = new Maze(DIM);
        // System.out.println("maze init/ gen finished");

        // Take AStar, and ask it to run the maze.
        // Astar will ask for a start and finish location
        Node res = aStar.run(maze);

        // Do we even want a display path? Not sure.
        // might make sense to just turn it into testing only.
        String printable = display.getPath(res);
        System.out.println(printable);
        display.print(res);

        System.out.println("Shortest path length: " + display.getShortestPathLength(res));

        display.animateShortestPath(res);


    }

}
