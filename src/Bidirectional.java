import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Collections;

// TODO: Improvement: only check for meeting in middle after traveling up to halfway between S & E?

public class Bidirectional {
    private Maze maze;
    public ArrayList<Node> finalPath;
    // private Thread[] threads = new Thread[2];
    public Thread thread1, thread2;
    private int thread1Count = 0;
    private int thread2Count = 0;
    public Node[] biDPathList;
    public Node[] biDCurrNode;
    public Node[] biDVisited;
    public PriorityQueue<Node>[] visited = new PriorityQueue[2];
    public PriorityQueue<Node>[] frontier = new PriorityQueue[2];

    public Bidirectional(Maze maze) {
        this.maze = maze;
        biDPathList = new Node[2];
        biDCurrNode = new Node[2];
        biDVisited = new Node[2];
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

    // public void print() {
    //     System.out.println("S: [" + start.x + "][" + start.y + "]");
    //     System.out.println("E: [" + target.x + "][" + target.y + "]");
    // }

    /**
     * Given a maze, applies a heuristic in order to navigate through that maze
     * and report statistics on that navigation.
     */
    class HalfPath implements Runnable {
        private Node start, end;
        // public PriorityQueue<Node> visited = new PriorityQueue<>();
        // public PriorityQueue<Node> frontier = new PriorityQueue<>();
        private ArrayList<Node> halfPath;
        // private int count = 0;
        private int threadName;

        public HalfPath(int threadName, Node s, Node e) {
            this.threadName = threadName;
            start = s;
            end = e;
            halfPath = new ArrayList<>();
            start.g = 0.0;
            start.h = maze.getHeuristic(start.x, start.y, end.getCoordinates());
            start.f = start.g + start.h;
            frontier[threadName].add(start);
        }

        @Override
        public void run() {
            System.out.println("THREAD " + threadName + " start: [" + start.x + "][" + start.y + "]");
            Node current = null;
            // while(!frontier.isEmpty()) {
            while(!frontier[threadName].isEmpty()) {
                // Get the next priority node
                if (Thread.interrupted()) {
                    // biDPathList[threadName] = current;
                    System.out.println("Thread" + threadName + " was interrupted.");
                    return;
                }
                current = frontier[threadName].peek();
                biDCurrNode[threadName] = current;
                    // if (current == end) {
                    // if (count == 6) {
                    // if (biDCurrNode[1 - threadName] != null && current.x == biDCurrNode[1 - threadName].x && current.y == biDCurrNode[1 - threadName].y) {
                    // if (frontier[threadName == 0 ? 1 : 0].contains(current) || visited[threadName == 0 ? 1 : 0].contains(current) || biDCurrNode[threadName == 0 ? 1 : 0] == current) {
                    for (Node neigh : maze.getNeighbors(current)) {
                        if (neigh.seen[1 - threadName]) {
                            if (threadName == 0 ) {
                                thread2.interrupt();
                            } else {
                                thread1.interrupt();
                            }
                            Node c = current;
                            biDPathList[threadName] = c;
                            System.out.println("thread" + threadName + " final: [" + c.x + "][" + c.y + "]");
                            System.out.println("OTHER thread(" + (1 - threadName) + ") final: [" + neigh.x + "][" + neigh.y + "]");
                            biDPathList[1 - threadName] = neigh;
                            
                            return;
                            // while (c.parent != null) {
                            //     halfPath.add(c);
                            //     c = c.parent;
                            // }
                            // Collections.reverse(halfPath);
                            // System.out.println("THREAD " + threadName + " ENDED!\n");
                            // for (Node d : halfPath) {
                            //     System.out.print("t" + threadName + ": [" + d.x + "][" + d.y + "]-->");
                            // }
                            // // System.out.println("\nCount: " + count);
                            // return;
                        }
                    }
    
                // call on the maze to find the neighbors of that node
                ArrayList<Node> neighbors = maze.getNeighbors(current);
    
                // look at neighbors, and
                for (Node node : neighbors) {
                    Node m = node;
                    m.seen[threadName] = true;
                    double totalWeight = current.g + m.weight;
    
                    /*
                    System.out.println("in Bidirectional, looking at the node");
                    System.out.println("size: " + frontier.size());
                    System.out.println("x = " + frontier.peek().x);
                    System.out.println("y = " + frontier.peek().y);
                    */
    
                    // if we have not been to the node, and it
                    // is not already in the list to explore...
                    if(!frontier[threadName].contains(m) && !visited[threadName].contains(m)) {
                        // count++;
                        // it knows this node is how to get to it
                        m.parent = current;
                        // finalPath.add(current);
                        System.out.println((threadName == 0 ? "    " : " ") + "thread" + threadName + ": [" + m.x + "][" + m.y + "]");
                        // and we calculate weight and put it in the frontier
                        m.g = totalWeight;
                        m.f = m.g + maze.getHeuristic(m.x, m.y, end.getCoordinates());
                        frontier[threadName].add(m);
                        // System.out.println("ADDED! [" + m.x + "][" + m.y + "]");
                    } else {
                        if (totalWeight < m.g) {
                            m.parent = current;
                            // finalPath.add(current);
                            String offset = threadName == 0 ? "    " : " ";
                            System.out.println(offset + "thread" + threadName + ": [" + m.x + "][" + m.y + "]");
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
