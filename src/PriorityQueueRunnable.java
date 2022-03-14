import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class PriorityQueueRunnable implements Runnable {
    private final PriorityBlockingQueue<Node> frontier;
    private final PriorityBlockingQueue<Node> visited;
    private final SynchronousQueue<Node> targetQueue;
    private final Maze maze;
    private final Node target;
    private double currentTargetWeight;
    private int numThreads;
    private static int blockingThreads;


    public PriorityQueueRunnable (PriorityBlockingQueue<Node> frontier,
                                  PriorityBlockingQueue<Node> visited,
                                  SynchronousQueue<Node> targetQueue,
                                  int numThreads,
                                  Maze maze ) {
        this.frontier = frontier;
        this.visited = visited;
        this.numThreads = numThreads;
        this.maze = maze;
        this.targetQueue = targetQueue;
        this.target = maze.getTarget();
        this.blockingThreads = 0;
        this.currentTargetWeight = Double.MAX_VALUE;

    }

    private synchronized void setAssumedFinalWeight(double weight) {
        if (currentTargetWeight > weight) {
            currentTargetWeight = weight;
        }
    }

    private synchronized void processGValue(Node current, Node neighbor, double totalWight, double fValue) {
        if (totalWight < neighbor.g) {
            neighbor.g = totalWight;
            neighbor.f = fValue;
            neighbor.parent = current;
        }
    }

    private synchronized void incrementThreads(boolean up) {
        System.out.println("incoming " + blockingThreads);
        if (up) {
            blockingThreads++;
            System.out.println("set at " + blockingThreads);
            if (numThreads == blockingThreads) {
                try {
                    System.out.println("HOORAY");
                    targetQueue.put(target);
                    return;
                } catch (InterruptedException e) {
                    System.out.println("Put to targetQueue failed from PQR");
                }
            }
        } else {
            System.out.println("decrement: " + blockingThreads);
            blockingThreads--;
        }
        System.out.println("Outgoing: " + blockingThreads);
    }

    public void run() {
        Node current;

        while(true) {

            if (Thread.interrupted()) {
                return;
            }

            try {
                incrementThreads(true);
                current = frontier.take();

                incrementThreads(false);

                if (current == target) {
                    setAssumedFinalWeight(current.weight);
                    continue;
                }
            } catch (InterruptedException e) {
                return;
            }

            if (current.weight > currentTargetWeight) {
                continue;
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

            if (!frontier.contains(current)) {
                visited.add(current);
            }

        }
    }
}
