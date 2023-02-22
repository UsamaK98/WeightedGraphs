import java.sql.*;
import java.util.Scanner;

public class SQLInterface {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "password123";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            // display a welcome message
            System.out.println("Welcome to the SQL Interface");
            System.out.println("Type 'exit' to quit");

            // loop until the user types 'exit'
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // prompt the user for a SQL statement
                System.out.print("> ");
                String sql = scanner.nextLine().trim();

                if (sql.equalsIgnoreCase("exit")) {
                    // exit the loop if the user types 'exit'
                    break;
                }

                try {
                    // execute the SQL statement
                    boolean isResultSet = stmt.execute(sql);

                    if (isResultSet) {
                        // print the query result
                        ResultSet rs = stmt.getResultSet();
                        ResultSetMetaData meta = rs.getMetaData();
                        int numColumns = meta.getColumnCount();

                        for (int i = 1; i <= numColumns; i++) {
                            System.out.print(meta.getColumnLabel(i) + "\t");
                        }
                        System.out.println();

                        while (rs.next()) {
                            for (int i = 1; i <= numColumns; i++) {
                                System.out.print(rs.getString(i) + "\t");
                            }
                            System.out.println();
                        }

                        rs.close();
                    } else {
                        // print the update status
                        int numRowsAffected = stmt.getUpdateCount();
                        System.out.println(numRowsAffected + " rows affected");
                    }
                } catch (SQLException e) {
                    System.err.println("Error executing SQL statement: " + e.getMessage());
                }
            }

            // close the statement and connection
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }
}
