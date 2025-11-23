/*
Driver that configures and seeds the DB
 */

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DBInit {
    public static void main(String[] args) {
        Map map = new Map();
        Node a = new Node("A", 45.90, 25.70);
        Node b = new Node("B", 15.40, 56.90);
        Node c = new Node("C", 12.20, 78.80);
        Node d = new Node("D", 78.14, 12.60);
        Node e = new Node("E", 90.15, 2.70);

        a.addNeighbor(b, 3.20);
        a.addNeighbor(c, 4.20);
        a.addNeighbor(e, 1.20);

        b.addNeighbor(d, 1.20);
        b.addNeighbor(e, 1.20);

        c.addNeighbor(a, 1.20);
        c.addNeighbor(b, 1.12);

        map.addNode(a);
        map.addNode(b);
        map.addNode(c);
        map.addNode(d);
        map.addNode(e);

        mapToDB(map);

        //add whatever nodes are needed
        //add to null spaces
        //add to the db
    }

    public static void mapToDB(Map map) {
        String jdbcUrl = "jdbc:postgresql:///postgres?"
                + "cloudSqlInstance=catmap-474717:us-central1:catmapsql1"
                + "&socketFactory=com.google.cloud.sql.postgres.SocketFactory";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));) {
            for (Node node: map.getNodes()) {
                ArrayList<double[]> neighbors = node.getNeighbors();
                ArrayList<HashMap<String, Double>> jsonBArray = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();

                //setting up the json array
                for  (double[] neighbor: neighbors) {
                    HashMap<String,Double> hashMap = new HashMap<>();
                    hashMap.put("id", neighbor[0]);
                    hashMap.put("weight", neighbor[1]);
                    hashMap.put("counter", neighbor[2]);
                    jsonBArray.add(hashMap);
                }

                //adding the node
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO nodes VALUES (?, ?, ?, ?, ?)")) {
                    //sets ?'s
                    pstmt.setInt(1, node.getId());
                    pstmt.setString(2, node.getName());
                    pstmt.setDouble(3, node.getXCord());
                    pstmt.setDouble(4, node.getYCord());
                    pstmt.setObject(5, mapper.writeValueAsString(jsonBArray), Types.OTHER);

                    int rowsUpdated = pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(String.format("Prepared statement to add the node %s to the node table: "
                            + e.getMessage(), node.getName()));
                } catch ( JsonProcessingException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Tried to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
