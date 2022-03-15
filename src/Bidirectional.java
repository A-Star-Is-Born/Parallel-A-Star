/**
 * @author Anh Tran, Peter Loyd, Ulysses Lin
 * @date 3/14/2022
 * @see "Seattle University, CPSC5600, Winter 2022"
 * @class Bidirectional.java
 * 
 * Bi-directional A Star search algorithm.
 * Two threads come from opposite ends of the maze and typically meet somewhere in the middle.
 */

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Bidirectional {
    private Maze maze; // maze to solve
    public Thread thread0, thread1; // Threads
    public Node[] biDPathList; // path list taken by each thread
    public PriorityQueue<Node>[] visited = new PriorityQueue[2]; // visited priority queue for threads
    public PriorityQueue<Node>[] frontier = new PriorityQueue[2]; // frontier priority queue for threads
    
    /**
     * Constructs Bidirectional.
     *
     * @param Maze maze to traverse
     */
    public Bidirectional(Maze maze) {
        this.maze = maze;
        biDPathList = new Node[2];
        visited[0] = new PriorityQueue<>();
        visited[1] = new PriorityQueue<>();
        frontier[0] = new PriorityQueue<>();
        frontier[1] = new PriorityQueue<>();
        this.thread0 = new Thread(new HalfPath(0, maze.getStart(), maze.getTarget()));
        this.thread1 = new Thread(new HalfPath(1, maze.getTarget(), maze.getStart()));
        thread0.start();
        thread1.start();
    }

    /**
     * Runnable for the two threads.
     *
     * @class HalfPath
     */
    class HalfPath implements Runnable {
        private Node start, end; // start and end Nodes for this thread
        private int threadName; // 0 or 1

        /**
         * Constructs HalfPath.
         *
         * @param threadName 0 or 1
         * @param s start Node
         * @param e end Node
         */
        public HalfPath(int threadName, Node s, Node e) {
            this.threadName = threadName;
            start = s;
            end = e;
            start.g = 0.0; // initial traveled distance (0)
            start.h = maze.getHeuristic(start.x, start.y, end.getCoordinates()); // heuristic distance to end
            start.f = start.g + start.h; // combined distance
            frontier[threadName].add(start); // kick off by adding start to frontier
        }

        /**
         * Runs the main bi-directional algorithm for a given thread.
         */
        @Override
        public void run() {
            System.out.println("Thread" + threadName + " start: [" + start.x + "][" + start.y + "]"); // state where the thread starts
            Node current = null;
            while(!frontier[threadName].isEmpty()) {
                if (Thread.interrupted()) {
                    System.out.println("Thread" + threadName + " was interrupted.");
                    return;
                }
                current = frontier[threadName].peek();

                // Bi-directional stops when the current Node has a neighbor that has already been "seen" by the other thread.
                // All neighbors of a thread's current Node are "seen" while going through the maze.
                for (Node neigh : maze.getNeighbors(current)) {
                    if (neigh.seen[1 - threadName]) {
                        if (threadName == 0 ) { // stop the other thread when met
                            thread1.interrupt();
                        } else {
                            thread0.interrupt();
                        }
                        Node c = current;
                        biDPathList[threadName] = c; // set final Node of this thread's path
                        System.out.println("Meeting midway, with Thread" + threadName + " ending at: [" + c.x + "][" + c.y + 
                            "] and Thread" + (1 - threadName) + " ending at: [" + neigh.x + "][" + neigh.y + "]");
                        biDPathList[1 - threadName] = neigh; // set final Node of the other's path to the neighbor that was seen
                        return;
                    }
                }
    
                ArrayList<Node> neighbors = maze.getNeighbors(current);
    
                // iterate through neighbors of current Node and put on frontier
                for (Node node : neighbors) {
                    Node m = node;
                    m.seen[threadName] = true;
                    double totalWeight = current.g + m.weight;
    
                    if(!frontier[threadName].contains(m) && !visited[threadName].contains(m)) {
                        m.parent = current;
                        // Uncomment below to print coordinate
                        // System.out.println((threadName == 0 ? "    " : " ") + "thread" + threadName + ": [" + m.x + "][" + m.y + "]");
                        m.g = totalWeight;
                        m.f = m.g + maze.getHeuristic(m.x, m.y, end.getCoordinates());
                        frontier[threadName].add(m);
                    } else {
                        if (totalWeight < m.g) {
                            m.parent = current;
                            // Uncomment below to print coordinate
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