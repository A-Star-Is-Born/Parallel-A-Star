import java.util.PriorityQueue;
import java.util.concurrent.*;

public class ParallelPriorityQueue {

    private static int numThreads;
    private static SynchronousQueue<Node> targetQueue;

    public ParallelPriorityQueue(int numThreads) {
        this.numThreads = numThreads;
        targetQueue = new SynchronousQueue<Node>();
    }

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * and report statistics on that navigation.
     */
    public Node run(Maze maze) throws InterruptedException {
        // initialize data structures
        PriorityBlockingQueue frontier = new PriorityBlockingQueue();
        PriorityBlockingQueue visited = new PriorityBlockingQueue();

        // initialize the first node and put it in the queue
        Node start = maze.getStart();
        Node target = maze.getTarget();
        start.g = 0.0;
        start.h = maze.getHeuristic(start.x, start.y, target.getCoordinates());
        start.f = start.g + start.h;
        frontier.add(start);

        // store threads for later
        Thread[] pqArray = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            pqArray[i] = new Thread(new PriorityQueueRunnable(frontier, visited, targetQueue, maze));
            pqArray[i].start();
        }

        // TODO: Does this work?
        System.out.println("Before take in main ");
        Node result = targetQueue.take();
        System.out.println("After take in main ");

//        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);

        // PriorityQueueRunnable callable = new PriorityQueueRunnable(frontier, visited, targetQueue, maze);

        /*
        while there's something in the priority queue
        treat it like a task and send something to it

         */



        return null;

    }

    public static void main(String[] args) throws InterruptedException {
        int DIM = 20;
        int numThreads = 4;

        ParallelPriorityQueue parallelV1 = new ParallelPriorityQueue(numThreads);
        Display display = new Display(DIM);
        Maze maze = new Maze(DIM);
        Node res = parallelV1.run(maze);
        //String printable = display.getPath(res);
        //System.out.println(printable);
        //display.print(res);

//        System.out.println("Shortest path length: " + display.getShortestPathLength(res));

//        display.animateShortestPath(res);

    }

}
