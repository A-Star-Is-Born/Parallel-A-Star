/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class PriorityQueueRunnable.java
 *
 * A parallel shared-memory A* search algorithm
 * This class is run in parallel to effectively create multithreaded A* path-search.
 */
import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * PriorityQueueRunnable performs A* search in parallel.
 * It uses shared priority queues, and calls on a maze to locate
 * neighboring vertices.
 */
public class PriorityQueueRunnable implements Runnable {
    private final PriorityBlockingQueue<Node> frontier;     // Observed nodes, to be processed
    private final PriorityBlockingQueue<Node> visited;      // Visited nodes
    private final SynchronousQueue<Node> targetQueue;       // Signals the work is done
    private final Maze maze;        // contains information about what nodes are connected
    private final Node target;      // contains the location of the target node to be reached
    private static double assumedFinalWeight;

    /**
     * Initialization works just like it would for sequential A* search,
     * we create two queues that track where we're going and where we've been,
     * and we establish what our goal is.
     * @param frontier Tracks what nodes we're considering going to next.
     * @param visited Tracks what nodes we've visited in the past.
     * @param targetQueue The vehicle by which a node signals the search is over.
     * @param maze Allows the algorithm to find neighboring nodes, and understand wall locations.
     */
    public PriorityQueueRunnable (PriorityBlockingQueue<Node> frontier,
                                  PriorityBlockingQueue<Node> visited,
                                  SynchronousQueue<Node> targetQueue,
                                  Maze maze ) {
        this.frontier = frontier;
        this.visited = visited;
        this.maze = maze;
        this.targetQueue = targetQueue;
        this.target = maze.getTarget();
        this.assumedFinalWeight = Double.MAX_VALUE;
    }

    /**
     * A* search works by putting nodes onto a priority queue, which is then
     * accessed to find the best next node to check. When it accesses a node
     * it will find its neighbors using the maze object, and put its neighbors
     * onto the queue as well.
     * Upon finding the target it initiates an end-state in which nodes cannot work
     * on anything that's less optimal than the current path to the target.
     */
    public void run() {
        Node current;

        while(true) {

            //A failsafe, in case interrupt is called but the node is still working on low-priority nodes.
            if (Thread.interrupted())
                return;

            try {
                // Grab the most promising location, and if it's the target initiate end-state.
                current = frontier.take();
                if (current == target) {
                    targetQueue.put(current);
                    setAssumedFinalWeight(current.f);
                    return;
                }
                // No need to work on something we know is worse than optimal
                if (current.f > assumedFinalWeight) {
                    continue;
                }
            } catch (InterruptedException e) {
                return;
            }

            ArrayList<Node> neighbors = maze.getNeighbors(current);

            for (Node neighbor : neighbors) {
                double totalWeight = current.g + neighbor.weight;
                if (!frontier.contains(neighbor) && !visited.contains(neighbor)) {

                    double fValue = totalWeight + maze.getHeuristic(neighbor.x, neighbor.y, target.getCoordinates());
                    processGValue(current, neighbor, totalWeight, fValue);
                    frontier.add(neighbor);
                } else {
                    if (totalWeight < neighbor.g) {
                        double fValue = totalWeight + maze.getHeuristic(neighbor.x, neighbor.y, target.getCoordinates());
                        processGValue(current, neighbor, totalWeight, fValue);

                        if (visited.contains(neighbor)) {
                            try {
                                visited.remove(neighbor);
                                frontier.add(neighbor);
                            } catch (UnsupportedOperationException e) {
                                // This is an acceptable state of affairs, it does not affect functionality.
                            }
                        }
                    }
                }
            }
            if (!frontier.contains(current)) {
                visited.add(current);
            }
        }
    }

    /**
     * Synchronized access to node information is necessary, as otherwise
     * parallel threads could improperly overwrite or improperly sort a node.
     * @param current       // The node A* is currently on
     * @param neighbor      // The node being updated with weights
     * @param totalWight    // the totalWeight up to this point
     * @param fValue        // the final value of this node, as measured by the priority queue
     */
    public synchronized void processGValue(Node current, Node neighbor, double totalWight, double fValue) {
        if (totalWight < neighbor.g) {
            neighbor.g = totalWight;
            neighbor.f = fValue;
            neighbor.parent = current;
        }
    }


    private synchronized void setAssumedFinalWeight(double weight) {
        if (assumedFinalWeight > weight) {
            assumedFinalWeight = weight;
        }
    }

}
