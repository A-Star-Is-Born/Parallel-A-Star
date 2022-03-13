import java.awt.*;

public class Node implements Comparable<Node> {

    public int y;
    public int x;
    public double weight = 1.0;
    public Node parent;
    public boolean[] seen;

    public Point getCoordinates() {
        return new Point(x, y);
    }

    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    public double h;


    public int getX() {return x;}
    public int getY() {return y;}

    public Node(int x, int y, Node parent, double h) {
        this.y = y;
        this.x = x;
        this.h = h;
        this.parent = parent;
        seen = new boolean[2];
    }

    public Node clone() {
        return new Node(x, y, parent, h);
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.f);
    }

}