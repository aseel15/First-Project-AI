package com.example.FirstProject;


import java.util.LinkedList;

public class City {
    String cityName;
    int x;
    int y;
    double dist;
    LinkedList<City> citiesBranches;


    public City() {

    }

    public City(String cityName) {
        this.citiesBranches = new LinkedList();
        this.cityName = cityName;
    }

    public City(String cityName, int x, int y) {
        this.citiesBranches = new LinkedList();
        this.cityName = cityName;
        this.x = x;
        this.y = y;
    }

    public void addBranch(City cityAdj) {
        citiesBranches.addFirst(cityAdj);
    }

    @Override
    public String toString() {
        return cityName + "," + x + "," + y + " the distance = " + dist;

    }


}
