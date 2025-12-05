import java.util.ArrayList;

public class Node {
    //global fields
    private static int nodeCount = 0;

    private final int id;
    private final String name;
    private final double x_cord;
    private final double y_cord;
    private final ArrayList<double[]> neighbors;

    public Node(String name, double x_cord, double y_cord) {
        nodeCount++;
        this.id = nodeCount;
        this.name = name;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
        neighbors = new ArrayList<>();
    }

    public Node(int id, String name, double x_cord, double y_cord) {
        this.id = id;
        this.name = name;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
        neighbors = new ArrayList<>();
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

    public void addNeighbor(Node node) {
        boolean isThere  = false;
        double weight = (Math.abs(this.getXCord()-node.getXCord())) + Math.abs((this.getYCord()-node.getYCord()));
        for ( double[] neighbor : neighbors) {
            if (neighbor[0] == node.id) {
                isThere = true;
                break;
            }
        }
        if (!isThere) {
            double[] newNeighbor = new double[3];
            newNeighbor[0] = node.id;
            newNeighbor[1] = weight;
            neighbors.add(newNeighbor);
            node.addNeighbor(this);
        }
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

    public boolean isBuilding() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        Node oNode = (Node) obj;
        return name.equals(oNode.getName());
    }

}
