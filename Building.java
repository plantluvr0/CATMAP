import java.util.ArrayList;
import java.util.Scanner;

public class Building {
    private String name;
    private String campus;
    private int campusSelection;
    private final String[] campusOption = {"Athletic", "Central", "Redstone", "Trinity"};
    public Building(String name, String campus){
        this.name = name;
        this.campus = campus;
    }

    public String getName() {
        return name;
    }

    public String setName() {
        System.out.println("Enter a Building Name:");
        Scanner sc = new Scanner(System.in);
        String building = sc.nextLine();
        sc.close();
        return building;
    }

    public String getCampus() {
        return campus;
    }

    public String setCampus() {
        boolean isInt = false;
        while (isInt == false) {
            try {
                System.out.println("1,Athletic\n2,Central\n3,Redstone\n4,Trinity");
                Scanner selection = new Scanner(System.in);
                int choice = selection.nextInt();
                selection.close();
                campusSelection = choice-1;
                isInt = true;
                return campusOption[campusSelection];

            } catch (Exception mismatchTypeException) {
                System.out.println("error do a int");
            }
        }
        return "N/A";
    }

    public Building addBuilding(){
        name = setName();
        campus = setCampus();
        return new Building(name, campus);
    }

    @Override
    public String toString() {
        String str = String.format("%s,%s", name, campus);
        return str;
    }

}
