public class Path {
    private Edge[] edges;
    private double incline = 0, totalDis = 0, time;
    //constants
    private final double SPEED_ON_FLAT = 1.2;
    private final double SPEED_ON_INCLINE = 0.83;

    public Path(Edge[] edges){
        this.edges = edges;
        for (Edge edge : edges) {
            incline += edge.getIncline();
            totalDis += edge.getDistance();
        }
        time = totalDis * SPEED_ON_FLAT;
    }

    public Edge[] getSteps(){
        return edges;
    }

    public double getIncline(){
        return incline;
    }

    public double getDistance(){
        return totalDis;
    }

    public double getTime(){
        return time;
    }
}
