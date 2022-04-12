package com.example.FirstProject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class SampleController implements Initializable {
    City[] cities;
    HashMap<String, City> citiesHash;
    HashMap<String, Node> nodes;
    ArrayList<Circle> circlesLoc;
    AstarAlgo a;
    ArrayList<Line> lines = new ArrayList<>();
    static int counter = 0;

    @FXML
    private ComboBox<String> currentCB;
    @FXML
    private TextField distanceTxt;
    @FXML
    private ComboBox<String> goalCB;
    @FXML
    private TextArea pathArea;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private Button showButton;
    @FXML
    private TextField spaceTxt;
    @FXML
    private TextField timeText;
//when choose from the combo box
    public void showPath() {
        a = new AstarAlgo(cities);
        a.shortestPath(citiesHash.get(currentCB.getValue()), citiesHash.get(goalCB.getValue()));
        pathArea.setText(a.printPathSrc2Dest(citiesHash.get(goalCB.getValue())));
        DecimalFormat twoDig = new DecimalFormat("#.00");
        distanceTxt.setText(twoDig.format(a.allDisatnace) + " KM");
        timeText.setText(a.time+"");
        spaceTxt.setText(a.space+"");
        drawLines();
    }
//draw the path from source to destination by lines
    public void drawLines() {
        for (int i = 0; i < lines.size(); i++)
            scenePane.getChildren().remove(lines.get(i));

        for (int i = 0, j = 1; i < a.pathList.size() && j < a.pathList.size(); i++, j++) {
            lines.add(new Line());
            lines.get(i).setStartX(a.pathList.get(i).x);
            lines.get(i).setStartY(a.pathList.get(i).y);
            lines.get(i).setEndX(a.pathList.get(j).x);
            lines.get(i).setEndY(a.pathList.get(j).y);
            lines.get(i).setStrokeWidth(2);
            lines.get(i).setStroke(Color.DARKRED);
            scenePane.getChildren().add(lines.get(i));


        }
    }
//when click on the location of the city source and destination then get the path
    City srcCity;
    int j=0;
    public void onClickLoc() {
        for (int i = 0; i < circlesLoc.size(); i++) {
            int x = i;


            circlesLoc.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                //means that this city is the source
                if (counter == 0) {
                    a = new AstarAlgo(cities);
                    srcCity = cities[x];
                    j=x;
                    currentCB.setValue(cities[x].cityName);
                    circlesLoc.get(x).setFill(Color.DARKRED);
                    counter++;
                    //means that this city is the destination
                } else if (counter == 1) {
                    counter = 0;//set counter to 0 to end the previous path
                    goalCB.setValue(cities[x].cityName);
                    a.shortestPath(srcCity, citiesHash.get(cities[x].cityName));
                    pathArea.setText(a.printPathSrc2Dest(citiesHash.get(cities[x].cityName)));
                    DecimalFormat twoDig = new DecimalFormat("#.00");
                    distanceTxt.setText(twoDig.format(a.allDisatnace) + " KM");
                    timeText.setText(a.time+"");
                    spaceTxt.setText(a.space+"");
                    drawLines();
                    circlesLoc.get(j).setFill(Color.WHITE);

                }


            });

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //read the cities file
        File file = new File("towns.csv");

        int numOfCities = 0;

        BufferedReader scans;

        try {
            scans = new BufferedReader(new FileReader(file));


            numOfCities = Integer.parseInt(scans.readLine());


            cities = new City[numOfCities];
            citiesHash = new HashMap<String, City>();


            for (int i = 0; i < numOfCities; i++) {
                String string = scans.readLine();
                String[] stringCity = string.split(",");
                cities[i] = new City(stringCity[0], Integer.parseInt(stringCity[1]), Integer.parseInt(stringCity[2]));
                citiesHash.put(cities[i].cityName, cities[i]);

            }
            scans.close();
            int numOfBranches = 0;
            //read the branches file
            File file2 = new File("roads-2.csv");
            scans = new BufferedReader(new FileReader(file2));
            numOfBranches = Integer.parseInt(scans.readLine());

            for (int i = 0; i < numOfBranches; i++) {
                String string = scans.readLine();
                String[] stringCities = string.split(",");
                City src = citiesHash.get(stringCities[0]);
                City dest = citiesHash.get(stringCities[1]);


                City srcBranch = new City();
                srcBranch.cityName = src.cityName;
                srcBranch.x = src.x;
                srcBranch.y = src.y;
                srcBranch.dist = Double.parseDouble(stringCities[2]);

                City destBranch = new City();
                destBranch.cityName = dest.cityName;
                destBranch.x = dest.x;
                destBranch.y = dest.y;
                destBranch.dist = Double.parseDouble(stringCities[2]);
                //add the destination branch to the source branch
                src.citiesBranches.addFirst(destBranch);
                //add the source branch to the destination branch
                dest.citiesBranches.addFirst(srcBranch);


            }
            scans.close();

            circlesLoc = new ArrayList<>();
            for (int i = 0; i < numOfCities; i++) {
                currentCB.getItems().add(cities[i].cityName);
                goalCB.getItems().add(cities[i].cityName);
                circlesLoc.add(new Circle());
                circlesLoc.get(i).setLayoutX(cities[i].x);
                circlesLoc.get(i).setLayoutY(cities[i].y);
                circlesLoc.get(i).setRadius(5.0);
                circlesLoc.get(i).setFill(Color.WHITE);
                circlesLoc.get(i).setStroke(Color.BLACK);
                scenePane.getChildren().add(circlesLoc.get(i));

            }

            onClickLoc();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}