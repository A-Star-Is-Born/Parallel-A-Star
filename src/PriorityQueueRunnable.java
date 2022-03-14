import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class PriorityQueueRunnable implements Runnable {
    private final PriorityBlockingQueue<Node> frontier;
    private final PriorityBlockingQueue<Node> visited;
    private final SynchronousQueue<Node> targetQueue;
    private final Maze maze;
    private final Node target;
    private double assumedFinalWeight;

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

    private synchronized void setAssumedFinalWeight(double weight) {
        if (assumedFinalWeight > weight) {
            assumedFinalWeight = weight;
        }
    }

    private synchronized void processGValue(Node current, Node neighbor, double totalWight, double fValue) {
        if (totalWight < neighbor.g) {
            neighbor.g = totalWight;
            neighbor.f = fValue;
            neighbor.parent = current;
        }
    }

    public void run() {
        Node current;

        while(true) {

            if (Thread.interrupted())
                return;

            try {
                current = frontier.take();
                if (current == target) {
                    setAssumedFinalWeight(current.weight);
                    targetQueue.put(current);
                    return;
                }
            } catch (InterruptedException e) {
                return;
            }

            if (current.weight > assumedFinalWeight) {
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
                                System.out.println("PQRunnable: tried to remove a neighbor that wasn't there");
                            }
                        }
                    }
                }
            }
            if (!frontier.contains(current))
                visited.add(current);
        }
    }
}
