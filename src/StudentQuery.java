import java.sql.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class StudentQuery {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mams_db";
        String username = "root";
        String password = "ehacker321";

        try (Connection con = DriverManager.getConnection(url, username, password);
             Scanner scanner = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            // Get user input
            String name = null;
            String number = null;
            Date dob = null;

            // Prompt for name
            System.out.print("Enter student name (leave blank to skip): ");
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                name = input;
            }

            // Prompt for number
            System.out.print("Enter student number (leave blank to skip): ");
            input = scanner.nextLine();
            if (!input.isBlank()) {
                number = input;
            }

            // Prompt for DoB
            System.out.print("Enter student date of birth (yyyy-mm-dd) (leave blank to skip): ");
            input = scanner.nextLine();
            if (!input.isBlank()) {
                dob = Date.valueOf(input);
            }

            // Build SQL query
            String query = "SELECT @number, @name, @DoB FROM student WHERE 1=1";
            if (name != null) {
                query += " AND @name = ?";
            }
            if (number != null) {
                query += " AND @number = ?";
            }
            if (dob != null) {
                query += " AND @DoB = ?";
            }

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                int i = 1;
                if (name != null) {
                    stmt.setString(i++, name);
                }
                if (number != null) {
                    stmt.setString(i++, number);
                }
                if (dob != null) {
                    stmt.setDate(i++, dob);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    // Display results
                    while (rs.next()) {
                        String snumber = rs.getString("number");
                        String sname = rs.getString("name");
                        Date dob2 = rs.getDate("DoB");
                        System.out.println(snumber + "\t" + sname + "\t" + dob2);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
