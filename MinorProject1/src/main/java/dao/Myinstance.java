package dao;

import java.sql.Connection;
import java.sql.DriverManager;
public class Myinstance {
    private static Myinstance instance = new Myinstance();
    private static Connection con;
    private Myinstance() {
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loadedd\n");

            // Connect to the database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Surya", "root", "root");
            System.out.println("Connection established\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Myinstance getInstance() {
       return instance;
    }

    public static Connection getConnection() {  
        return con;
    }
}
