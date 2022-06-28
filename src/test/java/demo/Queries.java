package demo;

import java.sql.*;

public class Queries {

    public static void main(String[] args) throws SQLException {
        try {
            // loading the driver into the JVM
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver could not be loaded.");
        }

        String query = "SELECT * from Student";
        Connection conn = DriverManager.getConnection("db-url", "username", "password");
        Statement stmt = conn.createStatement();
        //Statement stmt = Conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmeta = rs.getMetaData();
        int numCols = rsmeta.getColumnCount();

        for (int i = 1; i <= numCols; i++) { // print schema info
            System.out.println("Column #" + i + ": " +
                    rsmeta.getColumnName(i) + " of type "
                    + rsmeta.getColumnTypeName(i));
        }// for
        while (rs.next()) {
            for (int i = 1; i <= numCols; i++) {
                if (rsmeta.getColumnTypeName(i).equals("VARCHAR")) {
                    String value1 = rs.getString(i);
                    System.out.print(value1 + "\t");
                }
                if (rsmeta.getColumnTypeName(i).equals("INT")) {
                    int value2 = rs.getInt(i);
                    System.out.print(value2 + "\t");
                }
                // image other if-statements for more times
                System.out.println();
            } //for
        } //while
        rs.close();
        stmt.close();
        conn.close();
    } //main
} //Queries
