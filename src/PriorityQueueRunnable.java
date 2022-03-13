import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class PriorityQueueRunnable implements Runnable {
    private final PriorityBlockingQueue<Node> frontier;
    private final PriorityBlockingQueue<Node> visited;
    private final SynchronousQueue<Node> targetQueue;
    private final Maze maze;
    private final Node target;

    public PriorityQueueRunnable (PriorityBlockingQueue<Node> frontier,
                                  PriorityBlockingQueue<Node> visited,
                                  SynchronousQueue<Node> targetQueue,
                                  Maze maze ) {
        this.frontier = frontier;
        this.visited = visited;
        this.maze = maze;
        this.targetQueue = targetQueue;
        this.target = maze.getTarget();
    }

    public void run() {

        Node current;

        // todo: this assumes we will always find something
        while(true) {

            if (Thread.interrupted())
                return;

            //TODO: Check this: do we need to stop other threads from creating a new current in the PQ
            try {
                current = frontier.take();
                if (current == target) {
                    //TODO: update internals
                    targetQueue.put(current);
                    return;
                }
            } catch (InterruptedException e) {
                return;
            }

            ArrayList<Node> neighbors = maze.getNeighbors(current);

            for (Node neighbor : neighbors) {
                double totalWeight = current.g + neighbor.weight;

                // TODO: figure out how to prevent recursion here
                if (!frontier.contains(neighbor) && !visited.contains(neighbor)) {

                    double fValue = totalWeight + maze.getHeuristic(neighbor.x, neighbor.y, target.getCoordinates());
                    processGValue(current, neighbor, totalWeight, fValue);

                    frontier.add(neighbor); // TODO: this could be optimized to prevent recursion.
                } else {
                    // if the frontier or neighborhood already has the node, we do this

                    // if the cost to get to the node is less than the currently recorded cost at that node
                    if (totalWeight < neighbor.g) {
                        // synchronized check if you have the lower g value, and if so, set that gValue
                        // then continue to set the fValue, if you win the gValue contention
                        double fValue = totalWeight + maze.getHeuristic(neighbor.x, neighbor.y, target.getCoordinates());
                        processGValue(current, neighbor, totalWeight, fValue);

                        if (visited.contains(neighbor)) {
                            try {
                                visited.remove(neighbor);
                                frontier.add(neighbor);
                            } catch (UnsupportedOperationException e) {
                                System.out.println("PQRunnable: tried to remove a neighbor that wasn't there");
                            }
                        }
                    }
                }
            }// TODO: this is the problem sir, this right here, we added this to stop an error
            if (!frontier.contains(current))
                visited.add(current);
        }
    }

    public synchronized void processGValue(Node current, Node neighbor, double totalWight, double fValue) {
        if (totalWight < neighbor.g) {
            neighbor.g = totalWight;
            neighbor.f = fValue;
            neighbor.parent = current;
        }
    }

}
