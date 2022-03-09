package main.display;

import main.maze.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Display {
    
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

        System.out.println("If what follows are mem addresses, reconfigure the toString method for points in Display.java");
        for(Point step : path){
            str.append(step.toString() + " ");
        }
        return str.toString();
    }

}
