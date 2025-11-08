/*
Class that represents a map of campus
 */
import java.util.ArrayList;

public class Map {
    //Fields
    private ArrayList<ArrayList<Node>> map;
    private ArrayList<Node> nodes;
    private int width;

    public Map(int width) {
        map = new ArrayList<ArrayList<Node>>(width);
    }

    public boolean addNode(Node newNode) {
        if (map.get(newNode.getXCord()).get(newNode.getYCord()) == null) {
            map.get(newNode.getXCord()).set(newNode.getYCord(), newNode);
            nodes.add(newNode);
            return true;
        } else {
            return false;
        }
    }

    public void removeNode(int x, int y) {
        map.get(x).set(y, null);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
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

    public PathAstar(Node start, Node goal) {
        //TODO check if the nodes are on the map

        Edge[] edges = new Edge[50];
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
            Edge bestEdge = null;
            Edge[] closestEdges = curr.getClosest();
            for (Edge edge : closestEdges) {
                double stepH = h(edge, goal);
                if (stepH < lowestTotalDis) {
                    lowestTotalDis = stepH;
                    bestEdge = edge;
                }
            }

            assert bestEdge != null;
            edges[i] = bestEdge;
            i++;
            fn += bestEdge.getDistance();
            curr = bestEdge.getEnd();
        }

        return new Path(edges);
    }

    public double h(Edge edge, Node goal) {
        int nextXCord = edge.getEnd().getXCord();
        int nextYCord = edge.getEnd().getYCord();
        int goalXCord = goal.getXCord();
        int goalYCord = goal.getYCord();

        double nextToGoal = Math.sqrt(Math.pow(Math.abs(nextXCord - goalXCord), 2)
                + Math.pow(Math.abs(nextYCord - goalYCord), 2));

        return edge.getDistance() + nextToGoal;
    }
}
