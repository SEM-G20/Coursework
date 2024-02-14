package com.napier.sem;
import java.sql.*;

public class App {
    public static void main(String[] args)
    {
        // Creating  an instance of the App class
        App a = new App();

        // Connecting to the database
        a.connect();

        // Print a message indicating successful connection
        System.out.println("Connected!");

        // Disconnect from the database
        a.disconnect();
    }

    // Connection object to hold the connection to the database
    private Connection con = null;

    // Method to connect to the database
    public void connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Print error message if driver is not found
            System.out.println("Could not load SQL driver");
            // Exit the program with an error code
            System.exit(-1);
        }

        // Number of retries to connect to the database
        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            // Print message indicating attempt to connect
            System.out.println("Connecting to database...");
            try {
                // Wait for some time before attempting to connect again
                Thread.sleep(30000);
                // Connect to the database using DriverManager
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                // Print message indicating successful connection
                System.out.println("Successfully connected");
                // Break out of the loop as the connection is successful
                break;
            } catch (SQLException sqle) {
                // Print error message if connection fails
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                // Print the error message received from SQLException
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                // Print error message if thread is interrupted unexpectedly
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    // Method to disconnect from the database
    public void disconnect()
    {
        // Check if the connection object is not null
        if (con != null)
        {
            try
            {
                // Close the database connection
                con.close();
            }
            catch (Exception e)
            {
                // Print error message if there is an exception while closing the connection
                System.out.println("Error closing connection to database");
            }
        }
    }

}
