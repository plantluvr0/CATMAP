

public class Path {
    private double length;
    private double incline;
    private final double SPEED_ON_FLAT = 1.2;
    private final double SPEED_ON_INCLINE = 0.83;

    public Path(double len, double incl){
        length = len;
        incline = incl;
    }
    public double time(){
        if (incline > 0) {
            return (length/SPEED_ON_INCLINE);
        } else {
            return (length/SPEED_ON_FLAT);
        }
    }
    /**
     *
     * @param len
     * @param incl
     * @return
     */
    //TODO CHANGE ALL THIS
    public double cost(){
        double len = this.length;
        double incl = this.incline;
        return ((len*0.35+incl*0.65)/(len+incl));
    }
    public boolean touching(){

        return true;
    }
}
