package main;

import main.astar.AStar;
import main.display.Display;
import main.maze.Maze;
import main.maze.Node;

public class Parallel_AStar {

    public static void main(String[] args) {
        AStar aStar = new AStar();
        Maze maze = new Maze(10);
        Node res = aStar.run(maze);
        Display display = new Display();
        String printable = display.getPath(res);

        // logging = setUpLogging()
        // settings = settings(logging)
        // mazes = setUpMazes()
        // aStar = setUpAStar(mazes, settings)
        // results = aStar.run()
        // viz = Visuals
        // viz(results)

        /*
        Node head = new Node(3);
        head.g = 0;
        Node n1 = new Node(2);
        Node n2 = new Node(2);
        Node n3 = new Node(2);
        Node n4 = new Node(1);
        Node n5 = new Node(1);
        Node target = new Node(0);
        head.addBranch(1, n1);
        head.addBranch(5, n2);
        head.addBranch(2, n3);
        n1.addBranch(7, n4);
        n2.addBranch(4, n5);
        n3.addBranch(1, n2);
        n3.addBranch(6, n4);
        n4.addBranch(3, target);
        n5.addBranch(1, n4);
        n5.addBranch(3, target);
        Maze maze = new Maze(10);
        // Display display = new Display();
        Node res = aStar.run(maze, head, target);
        // String printable = display.getPath(res);
        // System.out.println(printable);

         */
    }

}
