
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Pathfinder {

    public static ArrayList<ArrayList<ArrayList<Integer>>> constructAdj(int[][] edges, int V){
        ArrayList<ArrayList<ArrayList<Integer>>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            //fill with paths
        }
        return adj;
    }


    public int[] dijkstra(int V, int[][] edges, int initial){
        ArrayList<ArrayList<ArrayList<Integer>>> adj = constructAdj(edges, V);
        PriorityQueue<ArrayList<Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.get(0)));
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[initial] = 0;
        ArrayList<Integer> start = new ArrayList<>();
        start.add(0);
        start.add(initial);
        pq.offer(start);

        while (!pq.isEmpty()) {
            ArrayList<Integer> curr = pq.poll();
            int d = curr.get(0);
            int u = curr.get(1);

            for (ArrayList<Integer> neighbor : adj.get(u)) {
                int v = neighbor.get(0);
                int weight = neighbor.get(1);

                if (dist[v] > dist[u] + weight) {
                    dist[v] = dist[u] + weight;

                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(dist[v]);
                    temp.add(v);
                    pq.offer(temp);
                }
            }

        }
        return dist;

    }
}
