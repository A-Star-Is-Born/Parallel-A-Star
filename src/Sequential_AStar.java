import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Sequential_AStar {
    private static final int DIM = 20;
    private Node start;
    private Node target;
    private Maze maze;

    public Sequential_AStar(Maze maze) {
        this.maze = maze;
        this.start = maze.getStart();
        this.target = maze.getTarget();
    }

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * and report statistics on that navigation.
     */
    public Node run(){
        PriorityQueue<Node> visited = new PriorityQueue<>();
        PriorityQueue<Node> frontier = new PriorityQueue<>();

        start.g = 0.0;
        start.h = maze.getHeuristic(start.x, start.y, target.getCoordinates());
        start.f = start.g + start.h;
        frontier.add(start);

        // System.out.println("in Astar, just before alg starts");


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

            // look at neighbors, and
            for (Node node : neighbors) {
                Node m = node;
                double totalWeight = current.g + m.weight;

                /*
                System.out.println("in Astar, looking at the node");
                System.out.println("size: " + frontier.size());
                System.out.println("x = " + frontier.peek().x);
                System.out.println("y = " + frontier.peek().y);
                */

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

    }

    /**
     * Driver for running sequential A Star search.
     */
    public static void main(String[] args) {
        Maze maze = new Maze(DIM);
        Sequential_AStar aStar = new Sequential_AStar(maze);
        Display display = new Display(DIM);

        Node res = aStar.run();
        
        display.printPathAsList(res);
        
        display.printMazePath(res);
        
        System.out.println("Shortest path length: " + display.getShortestPathLength(res));
        
        display.animateShortestPath(res, Color.blue, 0.25);
    }
}
