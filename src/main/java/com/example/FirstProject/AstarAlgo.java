package com.example.FirstProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AstarAlgo {
    City[] cities;
    Node[] nodes;
    HashMap<String, Node> hash;
    HashMap<String, City> hashCities;
    PriorityQueue<Node> q;
    double allDisatnace;
    ArrayList<City> pathList = new ArrayList<>();
    String str = "";
    int time=0, space=0;

    public AstarAlgo(City[] cities1) {
        this.cities = new City[cities1.length];
        this.hash = new HashMap<String, Node>();
        this.hashCities = new HashMap<String, City>();
        this.nodes = new Node[cities1.length];
        this.q = new PriorityQueue<Node>(cities1.length, new Node());

        for (int i = 0; i < cities.length; i++) {
            nodes[i] = new Node();
            nodes[i].cityNode = cities1[i];
            nodes[i].visited = false;
            nodes[i].f = Double.MAX_VALUE;
            nodes[i].path = null;


            hash.put(cities1[i].cityName, nodes[i]);

        }

    }
   //to calculate the heuristic distance between the current city and destination by the straight line equation
    public double SLD(City src, City dest) {
        double s = Math.sqrt(Math.pow(src.x - dest.x, 2) + Math.pow(src.y - dest.y, 2));
        return s;
    }
    //A* method to get the shortest path
    public void shortestPath(City src, City dest) {
        Node srcNode = hash.get(src.cityName);
        srcNode.f = 0;
        q.add(srcNode);//add the source to the priority queue
        space++;//increment the space
        while (!q.isEmpty()) {
            Node srcCity = q.poll();//poll the city that has the least f

            if (!srcCity.visited) {
                srcCity.visited = true;//this city in the minimum f
                //the polled city is the destination
                if (srcCity.cityNode.cityName.equals(dest.cityName)) {
                    allDisatnace = srcCity.f;
                    break;
                }
                //if the city is not visited then visit its branches
                for (int i = 0; i < srcCity.cityNode.citiesBranches.size(); i++) {
                    time++;

                    Node temp = hash.get(srcCity.cityNode.citiesBranches.get(i).cityName);
                    //if the city is not polled that means it is not in the min distance
                    if (!temp.visited) {
                        temp.g = (srcCity.cityNode.citiesBranches.get(i).dist + srcCity.g);
                        temp.h = SLD(srcCity.cityNode.citiesBranches.get(i), dest);
                        if (temp.f > (temp.g + temp.h)) {
                            temp.f = temp.g + temp.h;

                            temp.path = srcCity.cityNode;//set the polled city to previous city of the added city
                            q.add(temp);
                            space++;//the num of cities in the priority queue is increased
                        }
                    }
                }
            }


        }


    }
//method to track the path from source to destination
    public String printPathSrc2Dest(City destination) {
        if (hash.get(destination.cityName).path != null) {
            printPathSrc2Dest(hash.get(destination.cityName).path);
            str += "⬇\n";
        }
        str += hash.get(destination.cityName).cityNode.cityName + "\n";
        pathList.add(hash.get(destination.cityName).cityNode);
        return str;

    }

}
