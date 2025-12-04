/*
Driver that configures and seeds the DB
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DBInit {
    public static void main(String[] args) {
        Map map = new Map();
        Node Ready = new Node("Ready", 44.485156 , 73.193409);
        Node Mcann = new Node("Mcann", 44.484698 , 73.192807);
        Node Richardson = new Node("Richardson",44.484894 , 73.193595 );
        Node Sichel = new Node("Sichel",44.484872 , 73.193647 );
        Node Hunt = new Node("Hunt",44.484959 , 73.193050 );
        Node b5 = new Node("b5", 44.484856 , 73.193182);
        b5.addNeighbor(Ready);
        b5.addNeighbor(Mcann);
        b5.addNeighbor(Richardson);
        b5.addNeighbor(Sichel);
        b5.addNeighbor(Hunt);
        //trin
        Node Mercy = new Node("Mercy", 44.483835 , 73.192515);
        Node Mcauley = new Node("Mcauley", 44.483316 , 73.191772);
        Node Thill = new Node("Thill",  44.481999 , 73.193447);
        Node Farrell = new Node("Farrell", 44.483180 , 73.193583);
        Node Mann = new Node("Mann", 44.482498 , 73.193506);
        Node Delehantey = new Node("Delehantey", 44.482646 , 73.194139);
        Node Tint = new Node("Tint", 44.482006 , 73.193458);
        Node Tcross = new Node("Tcross", 44.481242 , 73.195156);
        Mercy.addNeighbor(Mcauley);
        Mercy.addNeighbor(b5);
        Mercy.addNeighbor(Thill);
        Thill.addNeighbor(Farrell);
        Mann.addNeighbor(Mercy);
        Mann.addNeighbor(Mcauley);
        Mann.addNeighbor(Delehantey);
        Mann.addNeighbor(Tint);
        Delehantey.addNeighbor(Farrell);
        Tcross.addNeighbor(Tint);

        //central
        Node UVMMED = new Node("UVMMED", 44.480164, 73.195551);
        Node Fleming = new Node("Fleming", 44.479941, 73.197184 );
        Node Cint1 = new Node("Cint1", 44.479819, 73.196234 );
        Node Perkins = new Node("Perkins", 44.479809, 73.197814);
        Node Kalkin = new Node("Kalkin", 44.479148, 73.197512);
        Node Votey = new Node("Votey", 44.479113, 73.197952 );
        Node Grossman = new Node("Grossman", 44.479220, 73.197269 );
        Node Cint2 = new Node("Cint2", 44.479085, 73.197367);
        Node Innovation = new Node("Innovation", 44.478389 , 73.197546);
        Node Discovery = new Node("Discovery", 44.478526 , 73.197994);
        Node Williams = new Node("Williams", 44.478560 , 73.198898);
        Node Billings = new Node("Billings", 44.479277 , 73.199074);
        Node Converse = new Node("Converse",44.478454, 73.194582);
        Node Rowell = new Node("Rowell", 44.477942, 73.194554);
        Node VBWDint = new Node("VBWDint", 44.478984, 73.198565);
        Node Lafayette = new Node("Lafayette", 44.477872 , 73.198509);
        Node Central = new Node("Central", 44.478217 , 73.196264);
        Node Howe = new Node("Howe", 44.477252 , 73.196380);
        Node Davint = new Node("Davint", 44.476786, 73.196387);
        Node Davis = new Node("Davis", 44.476178, 73.196427);
        Node MarshLife = new Node("MarshLife", 44.476793, 73.195607);
        Node Aiken = new Node("Aiken", 44.476065, 73.195169);
        Node Jeffords = new Node("Jeffords", 44.475401, 73.193934);
        Tcross.addNeighbor(UVMMED);
        Perkins.addNeighbor(Fleming);
        Perkins.addNeighbor(Votey);
        Perkins.addNeighbor(Grossman);
        Votey.addNeighbor(Grossman);
        Billings.addNeighbor(Votey);
        Billings.addNeighbor(Williams);
        Williams.addNeighbor(Lafayette);
        Grossman.addNeighbor(Innovation);
        Innovation.addNeighbor(Discovery);
        Discovery.addNeighbor(Williams);
        Discovery.addNeighbor(Lafayette);
        Innovation.addNeighbor(Central);
        Central.addNeighbor(Howe);
        Howe.addNeighbor(MarshLife);
        Howe.addNeighbor(Davint);
        Davint.addNeighbor(MarshLife);
        Davint.addNeighbor(Davis);
        MarshLife.addNeighbor(Aiken);
        Aiken.addNeighbor(Jeffords);
        Cint1.addNeighbor(UVMMED);
        Cint1.addNeighbor(Fleming);
        Cint2.addNeighbor(Innovation);
        Cint2.addNeighbor(Grossman);
        Cint2.addNeighbor(Votey);

        //maps
        map.addNode(Ready);
        map.addNode(Mcann);
        map.addNode(Richardson);
        map.addNode(Sichel);
        map.addNode(Hunt);
        map.addNode(b5);
        map.addNode(Mercy);
        map.addNode(Mcauley);
        map.addNode(Thill);
        map.addNode(Farrell);
        map.addNode(Mann);
        map.addNode(Delehantey);
        map.addNode(Tint);
        map.addNode(Tcross);
        map.addNode(UVMMED);
        map.addNode(Fleming);
        map.addNode(Cint1);
        map.addNode(Perkins);
        map.addNode(Kalkin);
        map.addNode(Votey);
        map.addNode(Grossman);
        map.addNode(Cint2);
        map.addNode(Innovation);
        map.addNode(Discovery);
        map.addNode(Williams);
        map.addNode(Billings);
        map.addNode(VBWDint);
        map.addNode(Lafayette);
        map.addNode(Central);
        map.addNode(Howe);
        map.addNode(Davint);
        map.addNode(Davis);
        map.addNode(MarshLife);
        map.addNode(Aiken);
        map.addNode(Jeffords);

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