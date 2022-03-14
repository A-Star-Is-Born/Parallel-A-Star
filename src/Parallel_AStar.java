import edu.princeton.cs.algs4.StdDraw;

public class Parallel_AStar {
    private static final int DIM = 150;

    public static void main(String[] args) {
        standardVisualization();

        // This is not guaranteed to work if you have not gone through maze.java
        // and commented out all lines of code to do with display.
        testTimes(400);

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

    public static void testTimes(int numTimesToTest) {
        Maze maze;
        AStar aStar;
        ParallelPriorityQueue pqStar;
        Bidirectional bidirectionalStar;

        // ------------------------
        // SEQUENTIAL
        long sequentialTotal = 0;
        for (int i = 0; i < 100; i++) {
            maze = new Maze(DIM);
            aStar = new AStar(maze);
            long timingStart = System.currentTimeMillis();
            Node res = aStar.run();
            long timingEnd = System.currentTimeMillis();
            sequentialTotal += timingEnd - timingStart;
        }
        long sequentialTimePer = sequentialTotal / numTimesToTest;

        // ------------------------
        // BIDIRECTIONAL
        long bidirectionalTotal = 0;
        for (int i = 0; i < 100; i++) {
            maze = new Maze(DIM);
            long timingStart = System.currentTimeMillis();
            bidirectionalStar = new Bidirectional(maze);
            try {
                bidirectionalStar.thread1.join();
                bidirectionalStar.thread2.join();
            } catch (InterruptedException e) {
                return;
            }
            long timingEnd = System.currentTimeMillis();
            bidirectionalTotal += timingEnd - timingStart;
        }
        long bidirectionalTimePer = bidirectionalTotal / numTimesToTest;

        // ------------------------
        // PRIORITY QUEUEUEUEUE
        long pqTotal = 0;
        for (int i = 0; i < 100; i++) {
            maze = new Maze(DIM);
            pqStar = new ParallelPriorityQueue(4);
            long pqStart = System.currentTimeMillis();
            Node res = pqStar.run(maze);
            long pqEnd = System.currentTimeMillis();
            pqTotal += pqEnd - pqStart;
        }
        long pqTimePer = pqTotal / numTimesToTest;

        System.out.println("Timing sequential = " + sequentialTimePer);
        System.out.println("Timing parallel = " + pqTimePer);
        System.out.println("Timing bidirectional = " + bidirectionalTimePer);

    }

}