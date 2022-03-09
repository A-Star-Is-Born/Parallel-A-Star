package main.astar;
import main.maze.Maze;
import main.maze.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * and report statistics on that navigation.
     */
    public Node run(Maze maze){
        // TODO
        Node start = maze.getStart();
        Node target = maze.getTarget();

        PriorityQueue<Node> visited = new PriorityQueue<>();
        PriorityQueue<Node> frontier = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(target);
        frontier.add(start);

        while(!frontier.isEmpty()){
            Node current = frontier.peek();
            if (current == target){
                return current;
            }

            ArrayList<Node> neighbors = maze.getNeighbors(current);

            for (Node edge : neighbors) {
                Node m = edge;
                double totalWeight = current.g + m.weight;

                if(!frontier.contains(m) && !visited.contains(m)){
                    m.parent = current;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(target);
                    frontier.add(m);
                } else {
                    if(totalWeight < m.g){
                        m.parent = current;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target);

                        if(visited.contains(m)){
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

}
