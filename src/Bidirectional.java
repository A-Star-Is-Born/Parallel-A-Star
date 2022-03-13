import java.util.ArrayList;
import java.util.PriorityQueue;

// TODO: Improvement: only check for meeting in middle after traveling up to halfway between S & E?

public class Bidirectional {
    private Maze maze;
    public ArrayList<Node> finalPath;
    public Thread thread1, thread2;
    public Node[] biDPathList;
    public PriorityQueue<Node>[] visited = new PriorityQueue[2];
    public PriorityQueue<Node>[] frontier = new PriorityQueue[2];

    public Bidirectional(Maze maze) {
        this.maze = maze;
        biDPathList = new Node[2];
        visited[0] = new PriorityQueue<>();
        visited[1] = new PriorityQueue<>();
        frontier[0] = new PriorityQueue<>();
        frontier[1] = new PriorityQueue<>();
        this.finalPath = new ArrayList<Node>();
        this.thread1 = new Thread(new HalfPath(0, maze.getStart(), maze.getTarget()));
        this.thread2 = new Thread(new HalfPath(1, maze.getTarget(), maze.getStart()));
        thread1.start();
        thread2.start();
    }

    class HalfPath implements Runnable {
        private Node start, end;
        private int threadName;

        public HalfPath(int threadName, Node s, Node e) {
            this.threadName = threadName;
            start = s;
            end = e;
            start.g = 0.0;
            start.h = maze.getHeuristic(start.x, start.y, end.getCoordinates());
            start.f = start.g + start.h;
            frontier[threadName].add(start);
        }

        @Override
        public void run() {
            System.out.println("Thread" + threadName + " start: [" + start.x + "][" + start.y + "]");
            Node current = null;
            while(!frontier[threadName].isEmpty()) {
                if (Thread.interrupted()) {
                    System.out.println("Thread" + threadName + " was interrupted.");
                    return;
                }
                current = frontier[threadName].peek();
                for (Node neigh : maze.getNeighbors(current)) {
                    if (neigh.seen[1 - threadName]) {
                        if (threadName == 0 ) {
                            thread2.interrupt();
                        } else {
                            thread1.interrupt();
                        }
                        Node c = current;
                        biDPathList[threadName] = c;
                        System.out.println("Meeting midway, with Thread" + threadName + " ending at: [" + c.x + "][" + c.y + 
                            "] and Thread" + (1 - threadName) + " ending at: [" + neigh.x + "][" + neigh.y + "]");
                        biDPathList[1 - threadName] = neigh;
                        return;
                    }
                }
    
                // call on the maze to find the neighbors of that node
                ArrayList<Node> neighbors = maze.getNeighbors(current);
    
                // look at neighbors, and
                for (Node node : neighbors) {
                    Node m = node;
                    m.seen[threadName] = true;
                    double totalWeight = current.g + m.weight;
    
                    // if we have not been to the node, and it
                    // is not already in the list to explore...
                    if(!frontier[threadName].contains(m) && !visited[threadName].contains(m)) {
                        // it knows this node is how to get to it
                        m.parent = current;
                        // System.out.println((threadName == 0 ? "    " : " ") + "thread" + threadName + ": [" + m.x + "][" + m.y + "]");
                        // and we calculate weight and put it in the frontier
                        m.g = totalWeight;
                        m.f = m.g + maze.getHeuristic(m.x, m.y, end.getCoordinates());
                        frontier[threadName].add(m);
                    } else {
                        if (totalWeight < m.g) {
                            m.parent = current;
                            // String offset = threadName == 0 ? "    " : " ";
                            // System.out.println(offset + "thread" + threadName + ": [" + m.x + "][" + m.y + "]");
                            m.g = totalWeight;
                            m.f = m.g + maze.getHeuristic(m.x, m.y, end.getCoordinates());
                            if (visited[threadName].contains(m)) {
                                visited[threadName].remove(m);
                                frontier[threadName].add(m);
                            }
                        }
                    }
                }
                frontier[threadName].remove(current);
                visited[threadName].add(current);
            }
            return;
        }
    }
}