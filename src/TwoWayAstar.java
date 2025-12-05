import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;


public class TwoWayAstar {
    private final ArrayList<Node> openSetStart;
    private final ArrayList<Node> openSetGoal;
    private final ArrayList<Node> closedSetStart;
    private final ArrayList<Node> closedSetGoal;
    private ArrayList<Node> path;

    public TwoWayAstar() {
        openSetStart = new ArrayList<>();
        openSetGoal = new ArrayList<>();
        closedSetStart = new ArrayList<>();
        closedSetGoal = new ArrayList<>();
        path = new ArrayList<>();
    }


    public void findPath(Node start, Node goal) {
        // parent maps for each search direction (do not rely on Node having parent field)
        HashMap<Node, Node> parentStart = new HashMap<>();
        HashMap<Node, Node> parentGoal = new HashMap<>();
        HashMap<Node, Double> gStart = new HashMap<>();
        HashMap<Node, Double> gGoal = new HashMap<>();

        openSetStart.clear();
        openSetGoal.clear();
        closedSetStart.clear();
        closedSetGoal.clear();
        path.clear();

        openSetStart.add(start);
        openSetGoal.add(goal);
        parentStart.put(start, null);
        parentGoal.put(goal, null);
        gStart.put(start, 0.0);
        gGoal.put(goal, 0.0);

        while (!openSetStart.isEmpty() && !openSetGoal.isEmpty()) {
            Node currentStart = popLowestF(openSetStart, gStart, goal);
            Node currentGoal = popLowestF(openSetGoal, gGoal, start);

            closedSetStart.add(currentStart);
            closedSetGoal.add(currentGoal);

            // Check intersection
            Node meeting = null;
            for (Node n : closedSetStart) {
                if (closedSetGoal.contains(n)) {
                    meeting = n;
                    break;
                }
            }
            if (meeting != null) {
                path = reconstructPath(meeting, parentStart, parentGoal);
                printPath(path);
                return;
            }

            // Expand neighbors from start side
            List<Node> neighStart = getNeighbors(currentStart);
            for (Node neighbor : neighStart) {
                if ( closedSetStart.contains(neighbor)) continue;
                double tentativeG = gStart.getOrDefault(currentStart, Double.POSITIVE_INFINITY) + 1.0;
                if (tentativeG < gStart.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    parentStart.put(neighbor, currentStart);
                    gStart.put(neighbor, tentativeG);
                    if (!openSetStart.contains(neighbor)) openSetStart.add(neighbor);
                }
            }

            // Expand neighbors from goal side
            List<Node> neighGoal = getNeighbors(currentGoal);
            for (Node neighbor : neighGoal) {
                if ( closedSetGoal.contains(neighbor)) continue;
                double tentativeG = gGoal.getOrDefault(currentGoal, Double.POSITIVE_INFINITY) + 1.0;
                if (tentativeG < gGoal.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    parentGoal.put(neighbor, currentGoal);
                    gGoal.put(neighbor, tentativeG);
                    if (!openSetGoal.contains(neighbor)) openSetGoal.add(neighbor);
                }
            }
        }
        System.out.println("No path found.");
    }

    /**
     * Calculates the Manhattan distance heuristic between two nodes.
     * @param a The first node.
     * @param b The second node.
     * @return The Manhattan distance between node a and node b.
     */
    public double heuristic(Node a, Node b) {
        return Math.abs(a.getXCord() - b.getXCord()) + Math.abs(a.getYCord() - b.getYCord());
    }

    public void printPath(ArrayList<Node> path) {
        System.out.println("Path (" + path.size() + "):");
        for (Node node : path) {
            System.out.println(node);
        }
    }

    /**
     * Reconstructs full path from start to goal given meeting node and both parent maps.
     */
    public ArrayList<Node> reconstructPath(Node meeting, HashMap<Node, Node> parentStart, HashMap<Node, Node> parentGoal) {
        ArrayList<Node> pathFromStart = new ArrayList<>();
        Node cur = meeting;
        while (cur != null) {
            pathFromStart.add(cur);
            cur = parentStart.get(cur);
        }
        // pathFromStart currently is meeting -> ... -> start, reverse to start -> ... -> meeting
        Collections.reverse(pathFromStart);

        ArrayList<Node> pathFromGoal = new ArrayList<>();
        cur = parentGoal.get(meeting); // note: parentGoal maps node -> next toward goal, so start from meeting's child toward goal
        while (cur != null) {
            pathFromGoal.add(cur);
            cur = parentGoal.get(cur);
        }
        // Combine: start->...->meeting + meeting's goal-side path (already meeting excluded)
        ArrayList<Node> full = new ArrayList<>(pathFromStart);
        full.addAll(pathFromGoal);
        return full;
    }

    // Helper: choose and remove node from open with lowest f = g + heuristic(..., target)
    private Node popLowestF(ArrayList<Node> open, HashMap<Node, Double> gMap, Node target) {
        Node best = null;
        double bestF = Double.POSITIVE_INFINITY;
        for (Node n : open) {
            double g = gMap.getOrDefault(n, Double.POSITIVE_INFINITY);
            double f = g + heuristic(n, target);
            if (f < bestF) {
                bestF = f;
                best = n;
            }
        }
        if (best != null) open.remove(best);
        return best;
    }

    // Helper: attempt to retrieve neighbors via reflection (field "neighbors") or method getNeighbors()
    @SuppressWarnings("unchecked")
    private List<Node> getNeighbors(Node node) {
        try {
            // try field "neighbors"
            Class<?> cls = node.getClass();
            while (cls != null) {
                try {
                    java.lang.reflect.Field f = cls.getDeclaredField("neighbors");
                    f.setAccessible(true);
                    Object val = f.get(node);
                    if (val == null) return Collections.emptyList();
                    if (val instanceof List) return (List<Node>) val;
                    if (val instanceof Node[]) return Arrays.asList((Node[]) val);
                    if (val instanceof Iterable) {
                        List<Node> res = new ArrayList<>();
                        for (Object o : (Iterable<?>) val) if (o instanceof Node) res.add((Node) o);
                        return res;
                    }
                } catch (NoSuchFieldException e) {
                    cls = cls.getSuperclass();
                    continue;
                }
                break;
            }
            // try method getNeighbors()
            try {
                java.lang.reflect.Method m = node.getClass().getMethod("getNeighbors");
                Object val = m.invoke(node);

                if (val instanceof List) return (List<Node>) val;
                if (val instanceof Node[]) return Arrays.asList((Node[]) val);
                if (val instanceof Iterable) {
                    List<Node> res = new ArrayList<>();
                    for (Object o : (Iterable<?>) val) if (o instanceof Node) res.add((Node) o);
                    return res;
                }
            } catch (NoSuchMethodException ignore) {}
        } catch (Exception ignored) {}
        return Collections.emptyList();
    }
    public String[] givepath(ArrayList<Node> path){
        String[] nodespath = new String[path.size()];
        int ctr = 0;
        for(Node n : path){
            nodespath[ctr] = n.getName();
            ctr++;
        }
        return nodespath;
    }
    public ArrayList<Node> getPath(){
        return path;
    }

}


