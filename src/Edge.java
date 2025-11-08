public class Edge {
    //global
    private static int edgeCount = 0;

    private int id;
    final private Node start, end;
    final private double distance;
    final private double incline;

    public Edge(Node start, Node end, double incline) {
        this.id =  edgeCount++;
        this.start = start;
        this.end = end;
        this.incline = incline;

        distance = Math.sqrt( Math.pow(Math.abs(start.getXCord() - end.getXCord()), 2) +
                Math.pow(Math.abs(end.getYCord() - start.getYCord()), 2));
    }

    public int getId() {
        return id;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getDistance() {
        return distance;
    }

    public double getIncline() {
        return incline;
    }
}
