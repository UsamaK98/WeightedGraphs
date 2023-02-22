/**
 * A demo program to show how jdbc works with postgresql
 * Nick Fankhauser
 * nickf@ontko.com or nick@fankhausers.com
 * This program may be freely copied and modified
 * Please keep this header intact on unmodified versions
 * The rest of the documentation that came with this demo program
 * may be found at http://www.fankhausers.com/postgresql/jdbc
 */

import java.sql.*;   // All we need for JDBC
import java.text.*;
import java.io.*;
import java.util.*;

public class HelloPostgresql {
    Connection db;        // A connection to the database
    Statement sql;       // Our statement to run queries with
    DatabaseMetaData dbmd;      // This is basically info the driver delivers
    // about the DB it just connected to. I use
    // it to get the DB version to confirm the
    // connection in this example.

    public HelloPostgresql(String argv[]) throws ClassNotFoundException, SQLException {
        String database = argv[0];
        String username = argv[1];
        String password = argv[2];
        Class.forName("org.postgresql.Driver"); //load the driver
        db = DriverManager.getConnection("jdbc:postgresql://" + database + "/", username, password); //connect to the db
        //System.out.println(db.getMetaData());
        dbmd = db.getMetaData(); //get MetaData to confirm connection
        System.out.println("Connection to " + dbmd.getDatabaseProductName() + " " +
                dbmd.getDatabaseProductVersion() + " successful.\n");
        sql = db.createStatement(); //create a statement that we can use later

        /*String sqlText = "create table jdbc_demo (code int, text varchar(20))";
        System.out.println("Executing this command: "+sqlText+"\n");
        sql.executeUpdate(sqlText);


        sqlText = "insert into jdbc_demo values (1,'One')";
        System.out.println("Executing this command: "+sqlText+"\n");
        sql.executeUpdate(sqlText);


        sqlText = "insert into jdbc_demo values (3,'Four')";
        System.out.println("Executing this command twice: "+sqlText+"\n");
        sql.executeUpdate(sqlText);
        sql.executeUpdate(sqlText);


        sqlText = "update jdbc_demo set text = 'Three' where code = 3";
        System.out.println("Executing this command: "+sqlText+"\n");
        sql.executeUpdate(sqlText);
        System.out.println (sql.getUpdateCount()+
                " rows were update by this statement\n");


        System.out.println("\n\nNow demostrating a prepared statement...");
        sqlText = "insert into jdbc_demo values (?,?)";
        System.out.println("The Statement looks like this: "+sqlText+"\n");
        System.out.println("Looping three times filling in the fields...\n");
        PreparedStatement ps = db.prepareStatement(sqlText);
        for (int i=10;i<13;i++)
        {
            System.out.println(i+"...\n");
            ps.setInt(1,i);         //set column one (code) to i
            ps.setString(2,"HiHo"); //Column two gets a string
            ps.executeUpdate();
        }
        ps.close();


        System.out.println("Now executing the command: "+
                "select * from jdbc_demo");
        ResultSet results = sql.executeQuery("select * from jdbc_demo");
        if (results != null)
        {
            while (results.next())
            {
                System.out.println("code = "+results.getInt("code")+
                        "; text = "+results.getString(2)+"\n");
            }
        }
        results.close();


        sqlText = "drop table jdbc_demo";
        System.out.println("Executing this command: "+sqlText+"\n");
        sql.executeUpdate(sqlText);
*/

        //db.close();
    }

    public static void recursion(String upperP, Connection db) {
        System.out.print(upperP + " ");
        String lowerP = "";
        try {
            PreparedStatement pstmt = db.prepareStatement(
                    "SELECT MINOR_P FROM PART_STRUCTURE " +
                            "WHERE MAJOR_P = ? AND MINOR_P > ? " +
                            "ORDER BY MINOR_P"
            );
            pstmt.setString(1, upperP);
            pstmt.setString(2, lowerP);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lowerP = rs.getString("minor_p");
                recursion(lowerP, db);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






