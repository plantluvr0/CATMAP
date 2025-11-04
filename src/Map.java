/*
Class that represents a map of campus
 */
import java.util.ArrayList;

public class Map {
    //Fields
    private ArrayList<ArrayList<Node>> map;
    private int width;

    public Map(int width) {
        map = new ArrayList<ArrayList<Node>>(width);
    }

    public void addNode(Node newNode) {
        map.get(newNode.getXCord()).set(newNode.getYCord(), newNode);
    }

    public void removeNode(int x, int y) {
        map.get(x).set(y, null);
    }

    public Node getNode(int x, int y) {
        return map.get(x).get(y);
    }

    public ArrayList<Node> getRow(int x) {
        return map.get(x);
    }

    public void setNull(int x, int y) {
        map.get(x).set(y, null);
    }

    public int getWidth() {
        return width;
    }

    public Path Astar(Node start, Node goal) {
        //TODO check if the nodes are on the map

        Step[] steps = new Step[50];
        int i = 0;

        double fn = 0;
        Node curr;
        curr = start;
        while (!curr.equals(goal)) {
            //figure out the cost of the path
            //we follow the lowest path
            //we store the descending cost nodes in a stack if we need them later
            //repeat until we get to goal

            double lowestTotalDis = 0;
            Step bestStep = null;
            Step[] closestSteps = curr.getClosest();
            for (Step step : closestSteps) {
                double stepH = h(step, goal);
                if (stepH < lowestTotalDis) {
                    lowestTotalDis = stepH;
                    bestStep = step;
                }
            }

            assert bestStep != null;
            steps[i] = bestStep;
            i++;
            fn += bestStep.getDistance();
            curr = bestStep.getEnd();
        }

        return new Path(steps);
    }

    public double h(Step step, Node goal) {
        int nextXCord = step.getEnd().getXCord();
        int nextYCord = step.getEnd().getYCord();
        int goalXCord = goal.getXCord();
        int goalYCord = goal.getYCord();

        double nextToGoal = Math.sqrt(Math.pow(Math.abs(nextXCord - goalXCord), 2)
                + Math.pow(Math.abs(nextYCord - goalYCord), 2));

        return step.getDistance() + nextToGoal;
    }
}
