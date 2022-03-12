import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Display {
    private int dim;
    private String[][] consoleGrid;

    public Display (int dim) {
        this.dim = dim;
        consoleGrid = new String[dim][dim];
    }

    /**
     * Prints path to console
     * 
     * @param target final Node
     */
    public void print(Node target) {
        System.out.println("\n");

        Node n = target;
        if(n==null)
            System.out.println("Something went wrong");

        List<Point> path = new ArrayList<>();

        while(n.parent != null){
            path.add(n.getCoordinates());
            n = n.parent;
        }

        path.add(n.getCoordinates());
        Collections.reverse(path);

        for(int l = 0; l < path.size(); l++) {
            Point curr = path.get(l), next;
            String toUse = "*";

            if (l == 0) {
                toUse = "S";
            } else if (l == path.size() - 1) {
                toUse = "E";
            } else {
                next = path.get(l + 1);
                if (next.x - curr.x == 1) {
                    toUse = ">";
                }
                if (next.x - curr.x == -1) {
                    toUse = "<";
                }
                if (next.y - curr.y == 1) {
                    toUse = "^";
                }
                if (next.y - curr.y == -1) {
                    toUse = "V";
                }
            }
               
            consoleGrid[curr.y - 1][curr.x - 1] = toUse;
        }
        Collections.reverse(Arrays.asList(consoleGrid));

        for (int y = -1; y < dim + 1; y++) {
                System.out.print("|");
            for (int x = 0; x < dim; x++) {
                if (y == -1 || y == dim) {
                    System.out.print("-");
                } else {
                    System.out.print(consoleGrid[y][x] == null ? " " : consoleGrid[y][x]);
                }
            }
            System.out.print("|\n");
        }

        System.out.println("\n");
    }

    public String getPath(Node target){
        StringBuilder str = new StringBuilder();

        Node n = target;
        if(n==null)
            return "Something went wrong";

        List<Point> path = new ArrayList<>();

        while(n.parent != null){
            path.add(n.getCoordinates());
            n = n.parent;
        }

        path.add(n.getCoordinates());
        Collections.reverse(path);

        for(int l = 0; l < path.size(); l++) {
            if (l == 0) {
                str.append("Start: ");
            }
            str.append("[" + path.get(l).x + "][" + path.get(l).y + "]" + (l == path.size() - 1 ? " (End)" : "-->"));
        }

        return str.toString();
    }

    public int getShortestPathLength(Node target) {
        Node n = target;
        int count = 0;

        if(n==null)
            System.out.println("Something went wrong");

        while(n.parent != null){
            count++;
            n = n.parent;
        }

        return count;
    }
}
