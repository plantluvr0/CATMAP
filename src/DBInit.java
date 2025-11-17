/*
Driver that configures and seeds the DB
 */

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;

public class DBInit {
    public static void main(String[] args) {
        Map map = new Map();

        //add whatever nodes are needed
        //add to null spaces
        //add to the db
    }

    public void mapToDB(Map map) throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://localhost:5432/catmap";

        try (Connection conn = DriverManager.getConnection(url, System.getenv("DB_USER"), System.getenv("DB_PASS"));) {
            for (Node node: map.getNodes()) {
                ArrayList<double[]> neighbors = node.getNeighbors();

                //setting up the sql array
                double[][] javaArray = new double[neighbors.size()][3];
                for (int i = 0; i < neighbors.size(); i++) {
                     javaArray[i] = neighbors.get(i);
                }
                java.sql.Array sqlArray = conn.createArrayOf("Double", javaArray);

                //adding the node
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO nodes (*) VALUES (?, ?, ?, ?, ?)")) {
                    //sets ?'s
                    pstmt.setInt(1, node.getId());
                    pstmt.setString(2, node.getName());
                    pstmt.setInt(3, node.getXCord());
                    pstmt.setInt(4, node.getYCord());
                    pstmt.setArray(5, sqlArray);

                    int rowsUpdated = pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(String.format("Prepared statement to add the node %s to the node table: "
                            + e.getMessage(), node.getName()));
                }

            }
        } catch (SQLException e) {
            System.out.println("Tried to connect to the database" + e.getMessage());
        }
    }
}
