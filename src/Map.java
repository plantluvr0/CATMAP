/*
Class that represents a map of campus
 */

import java.util.ArrayList;

public class Map {
    //Fields
    private ArrayList<Node> nodes;
    private int width;

    public Map() {
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node newNode) {
        if (!nodes.contains(newNode)) {
            nodes.add(newNode);
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public Node getNode(int id) {
        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public int getWidth() {
        return width;
    }


}
