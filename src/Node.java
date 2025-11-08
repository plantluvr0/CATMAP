public class Node {
    //global fields
    private static int nodeCount = 0;

    private int id;
    private String name;
    private int x_cord;
    private int y_cord;
    private Edge[] closest;

    public Node(String name, int x_cord, int y_cord, Edge[] closest) {
        nodeCount++;
        this.id = nodeCount;
        this.name = name;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
        this.closest = closest;
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

    public Edge[] getClosest() {
     return closest;
    }

    public boolean equals(Object obj) {
        Node oNode = (Node) obj;
        return name.equals(oNode.getName());
    }
}
