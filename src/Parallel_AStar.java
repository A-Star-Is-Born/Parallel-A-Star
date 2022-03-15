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


    public static void standardVisualization() {
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

    public static void testTimes(int dimensions, int numTimesToTest, int numThreads) {
        Maze maze;
        AStar aStar;
        ParallelPriorityQueue pqStar;
        Bidirectional bidirectionalStar;


        System.out.println("Testing dim: " + dimensions + " numThreads: " + numThreads);
        // ------------------------
        // SEQUENTIAL
        long sequentialTotal = 0;
        for (int i = 0; i < numTimesToTest; i++) {
            maze = new Maze(dimensions);
            aStar = new AStar(maze);
            long timingStart = System.nanoTime();
            Node res = aStar.run();
            long timingEnd = System.nanoTime();
            sequentialTotal += timingEnd - timingStart;
        }
        long sequentialTimePer = sequentialTotal / numTimesToTest;

        System.out.println("Timing sequential = " + sequentialTimePer);


        // ------------------------
        // BIDIRECTIONAL
        long bidirectionalTotal = 0;
        for (int i = 0; i < numTimesToTest; i++) {
            maze = new Maze(dimensions);
            long timingStart = System.nanoTime();
            bidirectionalStar = new Bidirectional(maze);
            try {
                bidirectionalStar.thread1.join();
                bidirectionalStar.thread2.join();
            } catch (InterruptedException e) {
                return;
            }
            long timingEnd = System.nanoTime();
            bidirectionalTotal += timingEnd - timingStart;
        }
        long bidirectionalTimePer = bidirectionalTotal / numTimesToTest;

        System.out.println("Timing bidirectional = " + bidirectionalTimePer);

        // ------------------------
        // PRIORITY QUEUEUEUEUE
        long pqTotal = 0;
        for (int i = 0; i < numTimesToTest; i++) {
            maze = new Maze(dimensions);
            pqStar = new ParallelPriorityQueue(numThreads);
            long pqStart = System.nanoTime();
            Node res = pqStar.run(maze);
            long pqEnd = System.nanoTime();
            pqTotal += pqEnd - pqStart;
        }
        long pqTimePer = pqTotal / numTimesToTest;

        System.out.println("Timing parallel = " + pqTimePer);


        System.out.println("Calculating");
        System.out.println("pq = " + (double) sequentialTimePer / pqTimePer);
        System.out.println("bi = " + (double) sequentialTimePer / bidirectionalTimePer);

        System.out.println("\n\n");

    }

}