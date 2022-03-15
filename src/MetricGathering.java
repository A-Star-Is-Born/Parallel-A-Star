/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * "Seattle University, CPSC5600, Winter 2022"
 *
 * Metric (latency time) gathering for sequential and parallel A* Star Search.
 * /

/**
 *  This class will gather latency metrics for the sequential and both parallel versions.
 */
public class MetricGathering {
    private static final int DIM = 12; // Dimension of the maze

    /**
     *  The main entry point of this program
     * @param args not used
     */
    public static void main(String[] args) {
        // Generate the metrics we used for our report
        /**
         * TODO: for anyone running this code
         * This class doesn't work well with graphics, therefore you will want
         * to comment out the following code in Maze.java:
         *
         * In the Maze.java constructor, comment out these:
         * 57        StdDraw.setXscale(0, n+2);
         * 58        StdDraw.setYscale(0, n+2);
         *
         * 73        draw();
         */
        
        // Test with DIM of 20, 200, 500 at 20 times each (PQ for 1, 2, 4, 8 threads each)
        testTimes(20, 20);
        // testTimes(200, 20);
        // testTimes(500, 20);
    }

    /**
     *
     * @param dimensions the dimension of the maze
     * @param numTimesToTest number of times to test
     */
    public static void testTimes(int dimensions, int numTimesToTest) {
        Maze maze; // the maze to run the search
        Sequential aStar; // sequential A Star search
        ParallelPriorityQueue pqStar; // Parallel A Star search using concurrent priority queue
        Bidirectional bidirectionalStar; // Parallel A Star search using bi-directional A-Star


        System.out.println("Testing dim: " + dimensions);
        // ------------------------
        // SEQUENTIAL
        long sequentialTotal = 0; // Total time elapsed
        for (int i = 0; i < numTimesToTest; i++) {
            maze = new Maze(dimensions);  // create new maze
            aStar = new Sequential(maze); // create new A Star search instance
            long timingStart = System.nanoTime(); // start time
            Node res = aStar.run(); // run sequential A Star
            long timingEnd = System.nanoTime(); // end time
            sequentialTotal += timingEnd - timingStart; // calculate duration and add it to total time
        }
        long sequentialTimePer = sequentialTotal / numTimesToTest; // calculate time per run

        System.out.println("Timing sequential = " + sequentialTimePer);


        // ------------------------
        // BIDIRECTIONAL
        long bidirectionalTotal = 0; // Total time elapsed
        for (int i = 0; i < numTimesToTest; i++) {
            maze = new Maze(dimensions); // generate to new maze
            long timingStart = System.nanoTime(); // time start
            bidirectionalStar = new Bidirectional(maze); // create new bidirectional A Star search instance
            try {
                bidirectionalStar.thread0.join(); // join thread 1
                bidirectionalStar.thread1.join(); // join thread 2
            } catch (InterruptedException e) {
                return;
            }
            long timingEnd = System.nanoTime(); // record ending time
            bidirectionalTotal += timingEnd - timingStart; // calculate duration and add to total time
        }
        long bidirectionalTimePer = bidirectionalTotal / numTimesToTest; // calculate time per run

        System.out.println("Timing bidirectional = " + bidirectionalTimePer);
        // Calculate speedup = sequential / bi-directional time
        System.out.println("Calculating bi speedup = " + (double) sequentialTimePer / bidirectionalTimePer);

        // ------------------------
        // PRIORITY QUEUEUEUEUE
        long pqTimePer;
        for (int numThreads = 1; numThreads <= 8; numThreads *= 2) {
            long pqTotal = 0; // Total time for priority queue
            for (int i = 0; i < numTimesToTest; i++) {
                maze = new Maze(dimensions); // generate new maze
                pqStar = new ParallelPriorityQueue(numThreads); //generate new priority queue A Star instance
                long pqStart = System.nanoTime(); // record start time
                Node res = pqStar.run(maze); // run the parallel concurrent priority queue program and get the result
                long pqEnd = System.nanoTime(); // record ending time
                pqTotal += pqEnd - pqStart; // calculate duration and add to total time
            }
            pqTimePer = pqTotal / numTimesToTest; // calculate time per run
            System.out.println("Timing parallel = " + pqTimePer + " for " + numThreads + " threads");
            // Calculate speedup = sequential / parallel time
            System.out.println("Calculating pq speedup = " + (double) sequentialTimePer / pqTimePer);
        }
        System.out.println("Calculating bi = " + (double) sequentialTimePer / bidirectionalTimePer);

        System.out.println("\n\n");

    }

}