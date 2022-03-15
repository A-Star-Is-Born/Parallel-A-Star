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
        // To run this test comment out all maze display and animation code from all other drivers.

        // Test with dimension of 20 x 20 for 1, 2, 4, 8 threads at 20 times each
        for (int i = 1; i <= 8; i *= 2) {
            testTimes(20, 20, i);
        }
        // Test with dimension of 200 x 200 for 1, 2, 4, 8 threads at 20 times each
        for (int i = 1; i <= 8; i *= 2) {
            testTimes(200, 20, i);
        }
        // Test with dimension of 500 x 500 for 1, 2, 4, 8 threads at 20 times each
        for (int i = 1; i <= 8; i *= 2) {
            testTimes(500, 20, i);
        }

    }

    /**
     *
     * @param dimensions the dimension of the maze
     * @param numTimesToTest number of times to test
     * @param numThreads number of threads to use
     */
    public static void testTimes(int dimensions, int numTimesToTest, int numThreads) {
        Maze maze; // the maze to run the search
        Sequential aStar; // sequential A Star search
        ParallelPriorityQueue pqStar; // Parallel A Star search using concurrent priority queue
        Bidirectional bidirectionalStar; // Parallel A Star search using bi-directional A-Star


        System.out.println("Testing dim: " + dimensions + " numThreads: " + numThreads);
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

        // ------------------------
        // PRIORITY QUEUEUEUEUE
        long pqTotal = 0; // Total time for priority queue
        for (int i = 0; i < numTimesToTest; i++) {
            maze = new Maze(dimensions); // generate new maze
            pqStar = new ParallelPriorityQueue(numThreads); //generate new priority queue A Star instance
            long pqStart = System.nanoTime(); // record start time
            Node res = pqStar.run(maze); // run the parallel concurrent priority queue program and get the result
            long pqEnd = System.nanoTime(); // record ending time
            pqTotal += pqEnd - pqStart; // calculate duration and add to total time
        }
        long pqTimePer = pqTotal / numTimesToTest; // calculate time per run

        System.out.println("Timing parallel = " + pqTimePer);


        System.out.println("Calculating");
        // Calculate speedup = sequential / parallel time
        System.out.println("pq = " + (double) sequentialTimePer / pqTimePer);
        System.out.println("bi = " + (double) sequentialTimePer / bidirectionalTimePer);

        System.out.println("\n\n");

    }

}