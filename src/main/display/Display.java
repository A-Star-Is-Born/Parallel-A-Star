package main.display;

import main.maze.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Display {
    
    public String getPath(Node target){
        StringBuilder str = new StringBuilder();

        Node n = target;
        if(n==null)
            return "Something went wrong";

        List<Integer> path = new ArrayList<>();

        while(n.parent != null){
            path.add(n.id);
            n = n.parent;
        }

        path.add(n.id);
        Collections.reverse(path);

        for(int step : path){
            str.append(step + " ");
        }
        return str.toString();
    }
}
