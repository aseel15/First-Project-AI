package com.example.FirstProject;

import java.util.Comparator;

public class Node implements Comparator<Node> {
    City cityNode;
    boolean visited;
    double f = Double.MAX_VALUE;
    double g;
    double h;
    City path;


    @Override
    public int compare(Node o1, Node o2) {

        if (o1.f > o2.f)
            return 1;
        if (o1.f < o2.f)
            return -1;
        return 0;
    }

}
