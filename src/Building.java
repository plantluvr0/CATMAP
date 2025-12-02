

public class Building extends Node{
    private String name;
    private boolean accessible;
    public Building(String name, boolean accessible, int xpos, int ypos) {
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
    public String toString() {
        String str = String.format("%s,%s", name, xpos, ypos);
        return str;
    }

}
