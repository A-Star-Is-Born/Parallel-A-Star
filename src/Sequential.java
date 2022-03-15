/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class AStar.java
 *
 * A sequential shared-memory A* search
 * Uses multiple threads to find a solution using the A* algorithm in parallel.
 */

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A standard A* graph search algorithm.
 * Takes in a maze, a start location and a target,
 * returns the target, which can be used to derive the path.
 */
public class Sequential {
    private Node start;     // the node we're starting at
    private Node target;    // the node we're searching for
    private Maze maze;      // the maze we poll for graph information

    /**
     * Constructor sets up the nature of the problem.
     * @param maze The object that will be polled for graph information.
     */
    public Sequential(Maze maze) {
        this.maze = maze;
        this.start = maze.getStart();
        this.target = maze.getTarget();
    }

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * adds nodes to a priority queue based on their closeness (as defined by
     * the heuristic) to the target. Then retrieves nodes based on how close
     * they are to the target + the cost it took to get there.
     */
    public Node run(){
        PriorityQueue<Node> visited = new PriorityQueue<>();
        PriorityQueue<Node> frontier = new PriorityQueue<>();

        // The weight of the start is 0, since start is 0 distance from itself.
        start.g = 0.0;
        start.h = maze.getHeuristic(start.x, start.y, target.getCoordinates());
        start.f = start.g + start.h;
        frontier.add(start);

        /*
        This will run until the frontier, nodes that are visible but
        not yet explored, is empty. It takes the priority node,
        adds all its as-yet unexplored neighbors to the queue with their
        priorities, then finds the next node.
         */
        while(!frontier.isEmpty()) {
            // Get the next priority node
            Node current = frontier.peek();
            if (current == target){
                return current;
            }

            // call on the maze to find the neighbors of that node
            ArrayList<Node> neighbors = maze.getNeighbors(current);

            // look at neighbors, and process them.
            for (Node node : neighbors) {
                Node m = node;
                double totalWeight = current.g + m.weight;

                // if we have not been to the node, and it
                // is not already in the list to explore, we go here
                if(!frontier.contains(m) && !visited.contains(m)) {
                    // it knows this node is how to get to it
                    m.parent = current;
                    // and we calculate weight and put it in the frontier
                    m.g = totalWeight;
                    m.f = m.g + maze.getHeuristic(m.x, m.y, target.getCoordinates());
                    frontier.add(m);
                } else {
                    // else if we are re-calculating a place we've already seen
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
        // return null if there is no solution.
        return null;
    }

}
