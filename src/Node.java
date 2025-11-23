import java.util.ArrayList;

public class Node {
    //global fields
    private static int nodeCount = 0;

    private int id;
    private String name;
    private double x_cord;
    private double y_cord;
    private ArrayList<double[]> neighbors;

    public Node(String name, double x_cord, double y_cord) {
        nodeCount++;
        this.id = nodeCount;
        this.name = name;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
        neighbors = new ArrayList<double[]>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
     return name;
    }

    public double getXCord() {
     return x_cord;
    }

    public double getYCord() {
     return y_cord;
    }

    public boolean addNeighbor(Node node, double weight) {
        boolean isThere  = false;
        for (double[] neighbor : neighbors) {
            if (neighbor[0] == node.id) {
                isThere = true;
                break;
            }
        }
        if (!isThere) {
            neighbors.add(new double[]{node.id, weight, 0});
            return true;
        } else
            return false;
    }

    public ArrayList<double[]> getNeighbors() {
        return neighbors;
    }

    public double[] getNeighbor(double id) {
        for  (double[] neighbor : neighbors) {
            if(id == neighbor[0]) {
                return neighbor;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        Node oNode = (Node) obj;
        return name.equals(oNode.getName());
    }
}
