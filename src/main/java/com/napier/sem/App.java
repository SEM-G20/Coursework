package com.napier.sem;
import java.sql.*;
import java.util.HashMap;

public class App {

    /**
     * Main method
     */
    public static void main(String[] args)
    {
        // Creating  an instance of the App class
        App a = new App();

        // Connect to the database
        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        // Print a message indicating successful connection
        System.out.println("Connected!");

        // Load data from database into the app
        DataHolder dataHolder = a.createDataHolder();
        dataHolder.loadData();

        // Check sample data if objects are created and connected correctly
        if(dataHolder.testingRegionAddresses()){
            if(dataHolder.testingCitiesDataMatching()){
                System.out.println("Data load successful.");
            }
        }

        // Access objects
        HashMap<String, Continent> continents = dataHolder.getContinents();
        HashMap<String, Region> regions = dataHolder.getRegions();
        HashMap<String, Country> countries = dataHolder.getCountries();
        HashMap<String, District> districts = dataHolder.getDistricts();
        HashMap<Integer, City> cities = dataHolder.getCities();

        // Disconnect from the database
        a.disconnect();
    }

    // Connection object to hold the connection to the database
    private Connection con = null;

    /**
     * Connect to the database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " +                                  Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public DataHolder createDataHolder(){
        return new DataHolder(con);
    }

    /**
     * Disconnect from the database.
     */
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
