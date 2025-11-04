/*
Driver that configures and seeds the DB
 */

import java.sql.*;

public class DBInit {
    public static void main(String[] args) {
        Map map = new Map(100);
        //add whatever nodes are needed
        //add null spaces
        //add to the db

    }

    public void mapToDB(Map map) {
        String url = "jdbc:postgresql://localhost:5432/catmap";

        try (Connection conn = DriverManager.getConnection(url, System.getenv("DB_USER"), System.getenv("DB_PASS"));) {
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getWidth(); y++) {
                    Node node = map.getNode(x, y);

                    //adding the node
                    try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO nodes (*) VALUES (?, ?, )")) {

                        int rowsUpdated = stmt.executeUpdate();
                    }

                    //adding the node id to the map
                    try (PreparedStatement stmt = conn.prepareStatement("UPDATE map SET y_? = ? WHERE x_? = ?")) {

                        int rowsUpdated = stmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
