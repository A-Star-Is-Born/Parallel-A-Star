/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class ParallelPriorityQueue.java
 *
 * A parallel shared-memory A* search
 * Uses multiple threads to find a solution using the A* algorithm in parallel.
 */

import java.awt.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * ParallelPriorityQueue sets up and runs a parallel A* algorithm,
 * using shared memory in the form of SynchronousQueues
 */
public class ParallelPriorityQueue {

    private final int N_THREADS;                        // Number of threads desired
    private static SynchronousQueue<Node> targetQueue;  // How the class communicates with threads

    /**
     * Constructor for the ParallelPriorityQueue
     * @param numThreads The number of threads this algorithm will use.
     */
    public ParallelPriorityQueue(int numThreads) {
        this.N_THREADS = numThreads;
        targetQueue = new SynchronousQueue<>();
    }

    /**
     * Initiates the A* algorithm, sends a number of threads to work in parallel
     * then waits for one of them to return a solution, and initiates the end-state.
     * Finally, returns the the solution for processing elsewhere.
     * @param maze The maze that A* is operating in.
     * @return Returns the target node.
     */
    public Node run(Maze maze) {
        // initialize data structures
        PriorityBlockingQueue<Node> frontier = new PriorityBlockingQueue<>();
        PriorityBlockingQueue<Node> visited = new PriorityBlockingQueue<>();

        // Find the start and the target, initialize the start
        Node start = maze.getStart();
        Node target = maze.getTarget();
        start.g = 0.0;
        start.h = maze.getHeuristic(start.x, start.y, target.getCoordinates());
        start.f = start.g + start.h;
        frontier.add(start);

        // store threads for later
        Thread[] pqArray = new Thread[N_THREADS];
        for (int i = 0; i < N_THREADS; i++) {
            pqArray[i] = new Thread(new PriorityQueueRunnable(frontier, visited, targetQueue, maze));
            pqArray[i].start();
        }
        Node result = null;
        try {
            result = targetQueue.take();
        } catch (InterruptedException e) {
            System.out.println("Error in PPQ trying to take from the queue.");
        }

        // make sure no threads are blocking while waiting for an empty queue
        for (Thread t : pqArray)
            t.interrupt();

        // make sure all threads are done
        try {
            for (Thread t : pqArray)
                t.join();
        } catch (InterruptedException e) {
            System.out.println("Exception joining threads: " + e);
        }

        return result;
    }

    /**
     * Driver class for testing.
     * @param args Not used.
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int DIM = 20;
        int numThreads = 4;

        ParallelPriorityQueue parallelV1 = new ParallelPriorityQueue(numThreads);
        Display display = new Display(DIM);
        Maze maze = new Maze(DIM);
        Node res = parallelV1.run(maze);

        display.printPathAsList(res);
        
        display.printMazePath(res);

        System.out.println("Shortest path length: " + display.getShortestPathLength(res));

        display.animateShortestPath(res, Color.magenta, 0.3);
    }

}
