

public class Building extends Node{
    private String name;
    private boolean accessible;
    public Building(String name, boolean accessible, double xpos, double ypos) {
        super(name, xpos, ypos);
        this.name = name;
        this.accessible = accessible;
    }

    public String getName() {
        return name;
    }

    public boolean isAccessible() {
        return accessible;
    }

    @Override
    public boolean isBuilding() {
        return true;
    }

    @Override
    public String toString() {
        String str = String.format("%s,%s", name, super.getXCord(), super.getYCord());
        return str;
    }
}
