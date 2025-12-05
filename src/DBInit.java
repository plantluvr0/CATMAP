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
        //buildings

        //paths

        //add neighbors


        mapToDB(map);
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
