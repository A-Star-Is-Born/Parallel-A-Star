public class Parallel_AStar {

    public static void main(String[] args) {

        //Initializing. Not super relevant now
        AStar aStar = new AStar();
        Display display = new Display();

        // though maze does take a dimension of 10
        Maze maze = new Maze(4);
        // System.out.println("maze init/ gen finished");

        // Take AStar, and ask it to run the maze.
        // Astar will ask for a start and finish location
        Node res = aStar.run(maze);

        // Do we even want a display path? Not sure.
        // might make sense to just turn it into testing only.
        String printable = display.getPath(res);

        System.out.println(printable);


    }

}
