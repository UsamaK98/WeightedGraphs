/**
 * A demo program to show how jdbc works with postgresql
 * Nick Fankhauser
 * nickf@ontko.com or nick@fankhausers.com
 * This program may be freely copied and modified
 * Please keep this header intact on unmodified versions
 * The rest of the documentation that came with this demo program
 * may be found at http://www.fankhausers.com/postgresql/jdbc
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloPostgresql2
{
    Connection       db;        // A connection to the database
    Statement        sql;       // Our statement to run queries with
    DatabaseMetaData dbmd;      // This is basically info the driver delivers
    // about the DB it just connected to. I use
    // it to get the DB version to confirm the
    // connection in this example.

    public HelloPostgresql2(String argv[]) throws ClassNotFoundException, SQLException
    {
        String database = argv[0];
        String username = argv[1];
        String password = argv[2];
        Class.forName("org.postgresql.Driver"); //load the driver
        db = DriverManager.getConnection("jdbc:postgresql://"+database+"/",username,password); //connect to the db
        //System.out.println(db.getMetaData());
        dbmd = db.getMetaData(); //get MetaData to confirm connection
        System.out.println("Connection to "+dbmd.getDatabaseProductName()+" "+
                dbmd.getDatabaseProductVersion()+" successful.\n");
        sql = db.createStatement(); //create a statement that we can use later

    }


    public static void WeightedGraphBuilder (Connection db, String input) throws SQLException {
        // Query the Part_Structure table
        Statement stmt = db.createStatement();
        PreparedStatement pstmt = db.prepareStatement(
                "SELECT * FROM Part_Structure WHERE MAJOR_P >= ?"
        );
        pstmt.setString(1, input);
        ResultSet rs = pstmt.executeQuery();
        //ResultSet rs = stmt.executeQuery("SELECT MAJOR_P, MINOR_P, QTY FROM Part_Structure");

        // Build the weighted graph
        Map<String, List<Edge>> graph = new HashMap<>();
        while (rs.next()) {
            String from = rs.getString("MAJOR_P");
            String to = rs.getString("MINOR_P");
            int weight = rs.getInt("QTY");
            Edge edge = new Edge(from, to, weight);
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(edge);
        }

        Map<String, Integer> pathWeights = new HashMap<>();
        for (String node : graph.keySet()) {
            List<String> path = new ArrayList<>();
            path.add(node);
            getPathToLeaf(graph, path, pathWeights, input);
        }

        for (String leafNode : pathWeights.keySet()) {
            System.out.println("Paths ending at " + leafNode + " -> " + pathWeights.get(leafNode));
        }

        // Print out the graph with all paths to leaf nodes
        //graph.printGraphWithPaths();


        // Close the database connection
        rs.close();
        stmt.close();
        //db.close();
        }

    private static void getPathToLeaf(Map<String, List<Edge>> graph, List<String> path, Map<String, Integer> pathWeights, String root) {
        String currentNode = path.get(path.size() - 1);
        if (!graph.containsKey(currentNode)) {
            if (path.get(0).equals(root)) {
                int weight = 1;
                for (int i = 0; i < path.size() - 1; i++) {
                    List<Edge> edges = graph.get(path.get(i));
                    for (Edge edge : edges) {
                        if (edge.to.equals(path.get(i + 1))) {
                            weight *= edge.weight;
                            break;
                        }
                    }
                }
                String leafNode = path.get(path.size() - 1);
                pathWeights.put(leafNode, pathWeights.getOrDefault(leafNode, 0) + weight);
            }
        } else {
            for (Edge edge : graph.get(currentNode)) {
                path.add(edge.to);
                getPathToLeaf(graph, path, pathWeights, root);
                path.remove(path.size() - 1);
            }
        }
    }




    static class Edge {
        String from;
        String to;
        int weight;

        Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}





