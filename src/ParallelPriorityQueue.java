import java.util.concurrent.*;

public class ParallelPriorityQueue {

    private final int N_THREADS;
    private static SynchronousQueue<Node> targetQueue;

    public ParallelPriorityQueue(int numThreads) {
        this.N_THREADS = numThreads;
        targetQueue = new SynchronousQueue<>();
    }

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * and report statistics on that navigation.
     */
    public Node run(Maze maze) throws InterruptedException {
        // initialize data structures
        PriorityBlockingQueue<Node> frontier = new PriorityBlockingQueue<>();
        PriorityBlockingQueue<Node> visited = new PriorityBlockingQueue<>();

        // initialize the first node and put it in the queue
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

        Node result = targetQueue.take();
        for (Thread t : pqArray)
            t.interrupt();

        for (Thread t : pqArray)
            t.join();

        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        int DIM = 20;
        int numThreads = 4;

        ParallelPriorityQueue parallelV1 = new ParallelPriorityQueue(numThreads);
        Display display = new Display(DIM);
        Maze maze = new Maze(DIM);
        Node res = parallelV1.run(maze);
        String printable = display.getPath(res);
        System.out.println(printable);
        display.print(res);

        System.out.println("Shortest path length: " + display.getShortestPathLength(res));

        display.animateShortestPath(res);
    }

}
