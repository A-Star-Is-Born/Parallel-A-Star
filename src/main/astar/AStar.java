package main.astar;

import main.Parallel_AStar;
import main.maze.Node;

import java.util.PriorityQueue;

public class AStar {

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * and report statistics on that navigation.
     */
    public Node run(Node start, Node target){
        PriorityQueue<Node> visited = new PriorityQueue<>();
        PriorityQueue<Node> frontier = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(target);
        frontier.add(start);

        while(!frontier.isEmpty()){
            Node n = frontier.peek();
            if(n == target){
                return n;
            }

            for(Node.Edge edge : n.neighbors){
                Node m = edge.node;
                double totalWeight = n.g + edge.weight;

                if(!frontier.contains(m) && !visited.contains(m)){
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(target);
                    frontier.add(m);
                } else {
                    if(totalWeight < m.g){
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target);

                        if(visited.contains(m)){
                            visited.remove(m);
                            frontier.add(m);
                        }
                    }
                }
            }

            frontier.remove(n);
            visited.add(n);
        }
        return null;
    }

}
