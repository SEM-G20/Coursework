package com.napier.sem;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class App {

    private HashMap<String, Continent> continents;
    private HashMap<String, Region> regions;
    private HashMap<String, Country> countries;


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

        // Lines 38-42 are used to generate extra reports (hardcoded requests)
        a.continents = dataHolder.getContinents();
        a.regions = dataHolder.getRegions();
        a.countries = dataHolder.getCountries();
        ArrayList<String[]> extraReports = a.setExtraReports();
        extraReports.forEach(a::manageMenu);

        // Replace those with the line below (45) to enable user report requests in runtime
        //a.providePopulationInfoOnRequest(dataHolder);

        // Disconnect from the database
        a.disconnect();
    }

    // Connection object to hold the connection to the database
    private Connection con = null;
    private boolean isConnected = false;

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
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                isConnected = true;
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
                isConnected = false;
                System.out.println("Successfully disconnected");
            }
            catch (Exception e)
            {
                // Print error message if there is an exception while closing the connection
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Return status of the connection with database.
     * @return
     */
    public boolean isConnected()
    {
        return isConnected;
    }

    /**
     * Display to console menu with command options to user.
     */
    public void displayMenu(){
        System.out.println(
                "Type in:\n" +
                        "'world' to get world's population\n" +
                        "'continent x', where x is a name of the continent, to get specified continent population\n" +
                        "'region x', where x is a name of the region, to get specified region population\n" +
                        "'country x', where x is a name of country, to get specified country population\n" +
                        "'district x', where x is a name of district, to get specified district population\n" +
                        "'city x', where x is a name of city, to get specified city population\n" +
                        "'e' to exit the program");
    }

    // no idea how you've produced all reports without writing to file functionality or even calling the methods
    // src: https://www.digitalocean.com/community/tutorials/java-write-to-file
    /**
     * Create and write to md file.
     * @param data
     * @param filename
     * @return
     */
    public File writeToFile(String data, String filename) {

        File file = null;

        // src: https://www.educative.io/answers/how-to-get-a-current-working-directory-in-java
        Path currRelativePath = Paths.get("");
        String currAbsolutePathString = currRelativePath.toAbsolutePath().toString();
        currAbsolutePathString += "/population-requests/";

        if(filename == null) {
            System.out.println("Error - file name is null.");
            return file;
        }
        else if(!filename.endsWith(".md")){
            System.out.println("Error - file name has no md extension.");
            return file;
        }
        else{
            currAbsolutePathString += filename;
        }

        try {
            file = new File(currAbsolutePathString);
        } catch (Exception e){
            System.out.println("Error - file path invalid.");
        }

        FileWriter fr = null;

        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Convert string to "Title Case" style, each word starts with upper case and each string is divided with space.
     * Use when working with runtime report requests functionality.
     * @param stringsArray whole user command from console
     * @return
     */
    public String stringToTitleCase(String[] stringsArray, int startingIndex){

        if(stringsArray == null){
            System.out.println("Error - array of strings is null.");
            return null;
        }
        else if(stringsArray.length == 0){
            System.out.println("Error - array of strings is empty.");
            return null;
        }
        else if(startingIndex >= stringsArray.length || startingIndex < 0){
            System.out.println("Error - startingIndex out of range.");
            return null;
        }
        else{
            // Build valid name from user input e.g. "North America"
            StringBuilder nameSB = new StringBuilder();
            // Combine all arguments after first one (first is command, rest is the name)
            for(int i = startingIndex; i < stringsArray.length; i++){

                // Capitalise first letter of every word
                String strCamelCase = stringsArray[i].substring(0, 1).toUpperCase() +
                        stringsArray[i].substring(1).toLowerCase();
                // Concatenate all words together as name
                nameSB.append(strCamelCase);
                // Add space between each word except last one
                if(i < stringsArray.length - 1){
                    nameSB.append(" ");
                }
            }
            return nameSB.toString();
        }

    }

    /**
     * Convert string to "Title Case" style, each word starts with upper case and each string is divided with space.
     * Use when working with hardcoded report requests functionality.
     * @param string
     * @return
     */
    public String stringToTitleCase(String string){

        string = string.toLowerCase();
        String[] stringsArray = string.split("[\\s,;:/.]+");

        if(string == null){
            System.out.println("Error - provided string is null.");
            return null;
        }
        else if(string.isEmpty()){
            System.out.println("Error - provided string is empty.");
            return null;
        }
        else{
            // Build valid name from user input e.g. "North America"
            StringBuilder nameSB = new StringBuilder();

            for(int i = 0; i < stringsArray.length; i++){

                // Capitalise first letter of every word
                String strCamelCase = stringsArray[i].substring(0, 1).toUpperCase() +
                        stringsArray[i].substring(1).toLowerCase();

                // Concatenate all words together as name
                nameSB.append(strCamelCase);

                // Add space between each word except last one
                if(i < stringsArray.length - 1){
                    nameSB.append(" ");
                }
            }

            return nameSB.toString();
        }

    }
    /**
     * Convert string into string of lower case letters connected with dash, with the ending "population.md".
     * @param str
     * @return
     */
    public String getFilename(String str){

        if(str == null){
            System.out.println("Error - string is null.");
            return null;
        }
        else if(str.isEmpty()){
            System.out.println("Error - string is empty.");
            return null;
        }
        else{
            StringBuilder filenameSB = new StringBuilder();
            str = str.toLowerCase();
            String[] stringsArray = str.split("[\\s,;:/.]+");

            for(int i = 0; i < stringsArray.length; i++){
                filenameSB.append(stringsArray[i]);
                filenameSB.append("-");
            }
            filenameSB.append("population.md");
            return filenameSB.toString();
        }
    }

    /**
     * Get a reports request list.
     * @return
     */
    public ArrayList<String[]> setExtraReports(){
        ArrayList<String[]> extraReports = new ArrayList<>();

        String[] report1 = {"world"};
        extraReports.add(report1);

        String[] report2 = {"continent", "north america"};
        extraReports.add(report2);

        String[] report3 = {"region", "middle east"};
        extraReports.add(report3);

        String[] report4 = {"country", "united arab emirates"};
        extraReports.add(report4);

        String[] report5 = {"district", "england", "united kingdom"};
        extraReports.add(report5);

        String[] report6 = {"city", "berlin", "germany"};
        extraReports.add(report6);

        return extraReports;
    }

    /**
     * Recognise report request command and get associated data.
     * Use to generate hardcoded reports.
     * @param request an array of strings for the report to generate
     */
    public void manageMenu(String[] request){

        String firstCmdStr = request[0].toLowerCase();

        int population = 0;

        // Handle option selection
        if(firstCmdStr.equals("world")){

            for(Map.Entry<String, Continent> continentKeyValue : continents.entrySet()) {
                population += continentKeyValue.getValue().getPopulation();
            }
            String data = String.format("World population=%s", population);
            writeToFile(data, "world-population.md");

        }
        else{

            // Check if command has more than one argument
            if(request.length < 2){
                System.out.println("Not enough arguments to generate reports, please try again.");
            }
            else{

                String name = stringToTitleCase(request[1]);
                String filename = getFilename(name);

                switch (firstCmdStr) {

                    case "continent":
                        // Check if name exists in the collection
                        if (!continents.containsKey(name)) {
                            System.out.println("Continent name not found.");
                        }
                        else {
                            // Get continent's population
                            population = continents.get(name).getPopulation();

                            // Prepare data to be written to file
                            String data = String.format("Continent=%s, population=%s", name, population);

                            System.out.println(data);
                            writeToFile(data, filename);
                        }
                        break;

                    case "region":
                        // Check if name exists in the collection
                        if (!regions.containsKey(name)) {
                            System.out.println("Region name not found.");
                        }
                        else {
                            // Get region's population
                            population = regions.get(name).getPopulation();

                            // Prepare data to be written to file
                            String data = String.format("Region=%s, population=%s", name, population);

                            System.out.println(data);
                            writeToFile(data, filename);
                        }
                        break;

                    case "country":
                        // Check if name exists in the collection
                        if (!countries.containsKey(name)) {
                            System.out.println("Country name not found.");
                        }
                        else {
                            // Get country's population
                            population = countries.get(name).getPopulation();

                            // Prepare data to be written to file
                            String data = String.format("Country=%s, population=%s", name, population);

                            System.out.println(data);
                            writeToFile(data, filename);
                        }
                        break;

                    case "district":

                        if(request.length < 3){
                            System.out.println("Not enough arguments, please try again.");
                        }
                        else{

                            String districtCountryName = stringToTitleCase(request[2]);

                            // Check if country exists in the collection
                            if (!countries.containsKey(districtCountryName)) {
                                System.out.println("Country name not found.");
                                System.out.println(districtCountryName);
                            }
                            else {
                                // Check if country contains a district of provided name
                                if(!countries.get(districtCountryName).getDistricts().containsKey(name)){
                                    System.out.println("District name not found.");
                                }
                                else{
                                    // Get a searched district's population from the collection of districts contained
                                    // in the selected country
                                    population = countries.get(districtCountryName).getDistricts().get(name).getPopulation();

                                    // Prepare data to be written to file
                                    String data = String.format("District=%s, Country=%s, population=%s",
                                            name, districtCountryName, population);

                                    System.out.println(data);
                                    writeToFile(data, filename);
                                }
                            }
                        }

                        break;

                    case "city":

                        if(request.length < 3){
                            System.out.println("Not enough arguments, please try again.");
                        }
                        else{
                            String cityCountryName = stringToTitleCase(request[2]);

                            // Check if country exists in the collection
                            if (!countries.containsKey(cityCountryName)) {
                                System.out.println("Country name not found.");
                            }
                            else {

                                City searchedCity = new City(-1, "", -1);

                                HashMap<String, District> districtsInCountry = countries.get(cityCountryName).getDistricts();

                                // Get city by its name and country
                                for(Map.Entry<String, District> districtEntry : districtsInCountry.entrySet()){

                                    District district = districtEntry.getValue();

                                    // Search each district's collection of cities if it contains a searched city
                                    for(Map.Entry<Integer, City> cityEntry : district.getCities().entrySet()){
                                        City city = cityEntry.getValue();
                                        if(city.getName().equals(name)){
                                            searchedCity = city;
                                            break;
                                        }
                                    }
                                }

                                if(searchedCity.getId() == -1){
                                    System.out.println("City name not found.");
                                }
                                else{

                                    population = searchedCity.getPopulation();

                                    // Prepare data to be written to file
                                    String data = String.format("City=%s, Country=%s, population=%s",
                                            name, cityCountryName, population);

                                    System.out.println(data);
                                    writeToFile(data, filename);
                                }
                            }
                        }

                        break;

                    default:
                        System.out.println("Report request command not recognised, try again please.");
                        break;
                }
            }
        }


    }

    /**
     * Access population information on user request in runtime.
     * Produce md file with name and population of the world, continent, region, country, district or city,
     * where name of the searched element is provided by a user.
     * @param dataHolder
     */
    public void providePopulationInfoOnRequest(DataHolder dataHolder){

        HashMap<String, Continent> continents = dataHolder.getContinents();
        HashMap<String, Region> regions = dataHolder.getRegions();
        HashMap<String, Country> countries = dataHolder.getCountries();

        // Accessing population info on request functionality
        Scanner scanner = new Scanner(System.in);
        boolean getPopulation = true;
        while(getPopulation){

            this.displayMenu();
            String command = scanner.nextLine();

            command = command.toLowerCase();
            String[] commandStrings = command.split("[\\s,;:/.]+");

            String firstCmdStr = commandStrings[0];

            int population = 0;

            // Handle option selection
            if(firstCmdStr.equals("e")){
                getPopulation = false;
            }
            else if(firstCmdStr.equals("world")){

                for(Map.Entry<String, Continent> continentKeyValue : continents.entrySet()) {
                    population += continentKeyValue.getValue().getPopulation();
                }
                String data = String.format("World population=%s", population);
                System.out.println(data);
                writeToFile(data, "world-population.md");

            }
            else{

                // Check if command has more than one argument
                if(commandStrings.length < 2){
                    System.out.println("Not enough arguments, please try again.");
                }
                else{

                    String name = stringToTitleCase(commandStrings, 1);
                    String filename = getFilename(name);

                    switch (firstCmdStr) {

                        case "continent":
                            // Check if name exists in the collection
                            if (!continents.containsKey(name)) {
                                System.out.println("Continent name not found.");
                            }
                            else {
                                // Get continent's population
                                population = continents.get(name).getPopulation();

                                // Prepare data to be written to file
                                String data = String.format("Continent=%s, population=%s", name, population);

                                System.out.println(data);
                                writeToFile(data, filename);
                            }
                            break;

                        case "region":
                            // Check if name exists in the collection
                            if (!regions.containsKey(name)) {
                                System.out.println("Region name not found.");
                            }
                            else {
                                // Get region's population
                                population = regions.get(name).getPopulation();

                                // Prepare data to be written to file
                                String data = String.format("Region=%s, population=%s", name, population);

                                System.out.println(data);
                                writeToFile(data, filename);
                            }
                            break;

                        case "country":
                            // Check if name exists in the collection
                            if (!countries.containsKey(name)) {
                                System.out.println("Country name not found.");
                            }
                            else {
                                // Get country's population
                                population = countries.get(name).getPopulation();

                                // Prepare data to be written to file
                                String data = String.format("Country=%s, population=%s", name, population);

                                System.out.println(data);
                                writeToFile(data, filename);
                            }
                            break;

                        case "district":

                            // Ask for country, there may be districts of the same names in the world
                            System.out.println("Type in the name of the country:");
                            String districtCountryUserInput = scanner.nextLine();

                            // Convert country name to a format for searching in collection
                            districtCountryUserInput = districtCountryUserInput.toLowerCase();
                            String[] districtCountryStrings = districtCountryUserInput.split("[\\s,;:/.]+");
                            String districtCountryName = stringToTitleCase(districtCountryStrings, 0);

                            // Check if country exists in the collection
                            if (!countries.containsKey(districtCountryName)) {
                                System.out.println("Country name not found.");
                                System.out.println(districtCountryName);
                            }
                            else {
                                // Check if country contains a district of provided name
                                if(!countries.get(districtCountryName).getDistricts().containsKey(name)){
                                    System.out.println("District name not found.");
                                }
                                else{
                                    // Get a searched district's population from the collection of districts contained
                                    // in the selected country
                                    population = countries.get(districtCountryName).getDistricts().get(name).getPopulation();

                                    // Prepare data to be written to file
                                    String data = String.format("District=%s, Country=%s, population=%s",
                                            name, districtCountryName, population);

                                    System.out.println(data);
                                    writeToFile(data, filename);
                                }
                            }
                            break;

                        case "city":

                            // Ask for country, there may be cities of the same names in the world
                            System.out.println("Type in the name of the country:");
                            String cityCountryUserInput = scanner.nextLine();

                            // Convert country name to a format for searching in collection
                            cityCountryUserInput = cityCountryUserInput.toLowerCase();
                            String[] cityCountryStrings = cityCountryUserInput.split("[\\s,;:/.]+");
                            String cityCountryName = stringToTitleCase(cityCountryStrings, 0);

                            // Check if country exists in the collection
                            if (!countries.containsKey(cityCountryName)) {
                                System.out.println("Country name not found.");
                            }
                            else {

                                City searchedCity = new City(-1, "", -1);

                                HashMap<String, District> districtsInCountry = countries.get(cityCountryName).getDistricts();

                                // Get city by its name and country
                                for(Map.Entry<String, District> districtEntry : districtsInCountry.entrySet()){

                                    District district = districtEntry.getValue();

                                    // Search each district's collection of cities if it contains a seached city
                                    for(Map.Entry<Integer, City> cityEntry : district.getCities().entrySet()){
                                        City city = cityEntry.getValue();
                                        if(city.getName().equals(name)){
                                            searchedCity = city;
                                            break;
                                        }
                                    }
                                }

                                if(searchedCity.getId() == -1){
                                    System.out.println("City name not found.");
                                }
                                else{

                                    population = searchedCity.getPopulation();

                                    // Prepare data to be written to file
                                    String data = String.format("City=%s, Country=%s, population=%s",
                                            name, cityCountryName, population);

                                    System.out.println(data);
                                    writeToFile(data, filename);
                                }
                            }
                            break;

                        default:
                            System.out.println("Command not recognised, try again please.");
                            break;
                    }
                }
            }
        }
    }
}
