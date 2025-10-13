/*
app that checks location
 */

import java.util.*;

public class CatMap {
    public ArrayList<Building> buildingsList;
    public ArrayList<Path> pathsList;
    public ArrayList<Path> fullpath;
    private double[] position = new double[1];
    private double[] target = new double[1];

    public double timeToPosition(ArrayList<Path> fullpath){
        double timeCtr = 0;
        for (Path path : fullpath) {
            timeCtr += path.time();
        }
        return timeCtr;
    }

    public static void main(String[] args) {

    }
}
