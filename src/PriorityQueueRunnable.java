import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class PriorityQueueRunnable implements Runnable {
    private PriorityBlockingQueue frontier;
    private PriorityBlockingQueue visited;
    private SynchronousQueue targetQueue;
    private Maze maze;
    private Node target, start;

    public PriorityQueueRunnable (PriorityBlockingQueue frontier, PriorityBlockingQueue visited, SynchronousQueue targetQueue,
                                                           Maze maze ) {
        this.frontier = frontier;
        this.visited = visited;
        this.maze = maze;
        this.targetQueue = targetQueue;
        this.start = maze.getStart();
        this.target = maze.getTarget();

    }

    public void run() {
        //TODO: Redo is empty logic
        boolean sentinal = true;
        Node current;

        while(sentinal) {
            //TODO: Check this
            try {
                current = (Node) frontier.take();
                if (current == target) {
                    //TODO: update internals
                    targetQueue.put(current);
                    return;
                }

            } catch (InterruptedException e) {
                return;
            }
            ArrayList<Node> neighbors = maze.getNeighbors(current);
        }

/*
        while(!frontier.isEmpty()) {
            // Get the next priority node
            Node current = frontier.peek();
            if (current == target){
                return current;
            }

            // call on the maze to find the neighbors of that node
            ArrayList<Node> neighbors = maze.getNeighbors(current);

            // look at neighbors, and
            for (Node node : neighbors) {
                Node m = node;
                double totalWeight = current.g + m.weight;


                // if we have not been to the node, and it
                // is not already in the list to explore...
                if(!frontier.contains(m) && !visited.contains(m)) {
                    // it knows this node is how to get to it
                    m.parent = current;
                    // and we calculate weight and put it in the frontier
                    m.g = totalWeight;
                    m.f = m.g + maze.getHeuristic(m.x, m.y, target.getCoordinates());
                    frontier.add(m);
                } else {
                    if (totalWeight < m.g) {
                        m.parent = current;
                        m.g = totalWeight;
                        m.f = m.g + maze.getHeuristic(m.x, m.y, target.getCoordinates());
                        if (visited.contains(m)) {
                            visited.remove(m);
                            frontier.add(m);
                        }
                    }
                }
            }

            frontier.remove(current);
            visited.add(current);
        }
        return null;

*/

    }
}
