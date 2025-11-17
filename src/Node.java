import java.util.ArrayList;

public class Node {
    //global fields
    private static int nodeCount = 0;

    private int id;
    private String name;
    private int x_cord;
    private int y_cord;
    private ArrayList<double[]> neighbors;

    public Node(String name, int x_cord, int y_cord) {
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

    public int getXCord() {
     return x_cord;
    }

    public int getYCord() {
     return y_cord;
    }

    public boolean addNeighbor(double id, double weight, double count) {
        boolean isThere  = false;
        for (double[] neighbor : neighbors) {
            if (neighbor[0] == id) {
                isThere = true;
                break;
            }
        }
        if (!isThere) {
            neighbors.add(new double[]{id, weight, count});
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
