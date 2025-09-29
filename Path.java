public class Path {
    private Step[] steps;
    private double incline = 0, totalDis = 0, time;
    //constants
    private final double SPEED_ON_FLAT = 1.2;
    private final double SPEED_ON_INCLINE = 0.83;

    public Path(Step[] steps){
        this.steps = steps;
        for (Step step : steps) {
            incline += step.getIncline();
            totalDis += step.getDistance();
        }
        time = totalDis * SPEED_ON_FLAT;
    }

    public Step[] getSteps(){
        return steps;
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
