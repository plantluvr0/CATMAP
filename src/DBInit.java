/*
Driver that configures and seeds the DB
 */

import java.sql.Connection;
import java.sql.*;

public class DBInit {
    public static void main(String[] args) {
        Map map = new Map(100);

        //add whatever nodes are needed
        //add to null spaces
        //add to the db
    }

    public void mapToDB(Map map) throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://localhost:5432/catmap";

        try (Connection conn = DriverManager.getConnection(url, System.getenv("DB_USER"), System.getenv("DB_PASS"));) {
            for (Node node: map.getNodes()) {
                Edge[] closest = node.getClosest();

                //setting up the sql array
                Integer[] closestIds = new Integer[closest.length];
                for (int i =  0; i < closest.length; i++) {
                    closestIds[i] = closest[i].getId();
                }
                java.sql.Array sqlArray = conn.createArrayOf("INTEGER", closestIds);

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
                    System.out.println("Prepared statement to add the nodes to the node table: " + e.getMessage());
                }

                //adding the edges
                for (Edge edge: closest) {
                    try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO edges (*) VALUES (?, ?, ?, ?, ?)")) {
                        //sets ?'s
                        pstmt.setInt(1, edge.getId());
                        pstmt.setInt(2, edge.getStart().getId());
                        pstmt.setInt(3, edge.getEnd().getId());
                        pstmt.setDouble(4, edge.getDistance());
                        pstmt.setDouble(5, edge.getIncline());

                        int rowsUpdated = pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("Prepared statement to add edge with id " + edge.getId() + ":" + e.getMessage());
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Trying to connect to the database" + e.getMessage());
        }
    }
}
