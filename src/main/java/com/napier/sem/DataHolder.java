package com.napier.sem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds data with objects created from the database data.
 */
public class DataHolder {

    private Connection con = null;
    private HashMap<String, Continent> continents = new HashMap<>();
    private HashMap<String, Region> regions = new HashMap<>();
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, District> districts = new HashMap<>();
    private HashMap<Integer, City> cities = new HashMap<>();

    /**
     * Assign connection to the app database to read data.
     */
    public DataHolder(Connection con){
        this.con = con;
    }

    public DataHolder() {

    }
    public boolean isConnected(){
        return con != null;
    }

    public HashMap<String, Continent> getContinents(){ return continents; }
    public HashMap<String, Region> getRegions(){ return regions; }
    public HashMap<String, Country> getCountries(){ return countries; }
    public HashMap<String, District> getDistricts(){ return districts; }
    public HashMap<Integer, City> getCities(){ return cities; }

    /**
     * Create objects from database data.
     */
    public void loadData()
    {
        try{
            // Create string for SQL statement
            String strSelect =
                    "SELECT " +
                            "country.name, country.continent, country.region, country.population, country.capital, " +
                            "city.id, city.name, city.district, city.population, " +
                            "countrylanguage.language, countrylanguage.percentage, countrylanguage.isofficial "
                            + "FROM country, city, countrylanguage "
                            + "WHERE country.code = city.countrycode "
                            + "AND country.code = countrylanguage.countrycode ";

            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Read row by row and create objects out of the data
            while (rset.next()) {

                // Get continent name
                String countryContinent = rset.getString("country.continent");

                // Get region name
                String countryRegion = rset.getString("country.region");

                // Get country basic details
                String countryName = rset.getString("country.name");
                int countryPopulation = rset.getInt("country.population");
                String countryCapital = rset.getString("country.capital");
                // Get country languages details
                String countryLanguage = rset.getString("countrylanguage.language");
                float countryLanguagePercent = rset.getFloat("countrylanguage.percentage");
                String countryLanguageOfficial = rset.getString("countrylanguage.isofficial");

                // Get district name
                String cityDistrict = rset.getString("city.district");

                // Get city basic details
                int cityID = rset.getInt("city.id");
                String cityName = rset.getString("city.name");
                int cityPopulation = rset.getInt("city.population");


                // Catch existing objects, or create new ones and save to the main HashMap

                // continent:
                Continent continent = getContinent(countryContinent);
                // region:
                Region region = getRegion(countryRegion, continent);
                // country:
                Country country = getCountry(countryName, countryPopulation, continent, region);
                // district:
                District district = getDistrict(cityDistrict, country);
                // city:
                City city = getCity(cityID, cityName, cityPopulation, country, district);

                // Catch and save official languages
                if(countryLanguageOfficial.equals("T")){
                    country.addLanguage(countryLanguage, countryLanguagePercent);
                }

                // Catch and save the capital city
                if(city.getName().equals(countryCapital)) {
                    country.setCapital(city);
                }

                // Link entries
                district.addCity(cityID, city);
                country.addDistrict(cityDistrict, district);
                region.addCountry(countryRegion, country);
                continent.addRegion(countryRegion, region);
            }

            // Assign main languages
            setMainLanguages();

            // Assess missing populations
            setPopulations();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Failed to get data.");
        }
    }

    /**
     * Check if the Region Object from the main hashmap points to the same memory address
     * as the Region saved in the internal collection of the Continent
     */
    public boolean testingRegionAddresses(){

        // loop through each continent in the main continents hashmap
        for(Map.Entry<String, Continent> continentKeyValue : continents.entrySet()){

            String continentName = continentKeyValue.getKey();
            Continent continentObject = continentKeyValue.getValue();

            // get each continent hashmap of its regions
            HashMap<String, Region> continentRegions = continentObject.getRegions();

            // loop through each region in the main regions hashmap
            for(Map.Entry<String, Region> regionKeyValue : regions.entrySet()){
                String regionName = regionKeyValue.getKey();
                Region regionObject = regionKeyValue.getValue();

                // check if the region saved in the internal continent's collection
                // points to the same memory address as the region from main regions hashmap
                if(continentRegions.containsKey(regionName)){
                    if(continentRegions.get(regionName) == regionObject){
                        // region saved in specific continent collection is the exact same object
                        // that is saved in main regions hashmap
                    }
                    else{
                        System.out.println("Region in main HashMap and Region in Continent's collection " +
                                "are NOT the same objects.");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Helper for testingCitiesDataMatching method.
     * Get an array of rows from the database containing all cities in the specified country data.
     */
    public ArrayList<ArrayList<String>> testingGettingCitiesData(String country){
        try{

            String strSelect =
                    "SELECT " +
                            "country.name, country.continent, country.region, country.population, country.capital, " +
                            "city.id, city.name, city.district, city.population, " +
                            "countrylanguage.language, countrylanguage.percentage, countrylanguage.isofficial "
                            + "FROM country, city, countrylanguage "
                            + "WHERE country.code = city.countrycode "
                            + "AND country.code = countrylanguage.countrycode "
                            + "AND country.name = '" + country + "' "
                            + "ORDER BY city.id ";

            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            ArrayList<ArrayList<String>> rows = new ArrayList<>();

            String cityCode = "";
            String cityName = "";
            String cityDistrict = "";
            String cityPopulation = "";

            String countryName = "";
            String countryContinent = "";
            String countryRegion = "";
            String countryPopulation = "";
            //String countryLanguage = "";

            while (rset.next()) {

                ArrayList<String> row = new ArrayList<>();

                cityCode = rset.getString("city.id");
                cityName = rset.getString("city.name");
                cityDistrict = rset.getString("city.district");
                cityPopulation = rset.getString("city.population");

                countryName = rset.getString("country.name");
                countryContinent = rset.getString("country.continent");
                countryRegion = rset.getString("country.region");
                countryPopulation = rset.getString("country.population");
                // country language is the language used in a city, not a main language in the country
                // (one country may have two or more languages used)
                //countryLanguage = rset.getString("countrylanguage.language");

                row.add(cityCode); //0
                row.add(cityName); //1
                row.add(cityDistrict); //2
                row.add(cityPopulation); //3

                row.add(countryName); //4
                row.add(countryContinent); //5
                row.add(countryRegion); //6
                row.add(countryPopulation); //7

                rows.add(row);
            }
            return rows;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Failed to get data.");
            return null;
        }
    }

    /**
     * Testing method.
     * Check if the data contained in the City Objects matches data from the database.
     */
    public boolean testingCitiesDataMatching(){

        // get array with rows from database that contains cities in the UK
        // [0] in each inner array is a cityCode
        ArrayList<ArrayList<String>> rows = testingGettingCitiesData("United Kingdom");

        for(ArrayList<String> row : rows){
            Integer cityCode = Integer.valueOf(row.get(0));
            if(cities.containsKey(cityCode)){

                City city = cities.get(cityCode);

                // get city info from main cities HashMap
                String cityName = city.getName();
                String cityDistrict = city.getDistrict().getName();
                int cityPopulation = city.getPopulation();
                // get associated info from main cities HashMap
                String countryName = city.getCountry().getName();
                String continent = city.getCountry().getContinent().getName();
                String region = city.getCountry().getRegion().getName();
                int countryPopulation = city.getCountry().getPopulation();

                // get data from database that should match the one in the Objects
                String cityNameDB = row.get(1);
                String cityDistrictDB = row.get(2);
                int cityPopulationDB = Integer.parseInt(row.get(3));
                String countryNameDB = row.get(4);
                String continentDB = row.get(5);
                String regionDB = row.get(6);
                int countryPopulationDB = Integer.parseInt(row.get(7));

                // compare data from main HashMap to the data from database
                if(cityName.equals(cityNameDB) && cityDistrict.equals(cityDistrictDB)){
                    if(countryName.equals(countryNameDB) && continent.equals(continentDB) && region.equals(regionDB)){
                        if(cityPopulation==cityPopulationDB && countryPopulation==countryPopulationDB){
                        }
                        else{
                            System.out.println("Data doesn't match - city or country population.");
                            return false;
                        }
                    }
                    else{
                        System.out.println("Data doesn't match - country, continent or region.");
                        return false;
                    }
                }
                else{
                    System.out.println("Data doesn't match - city or district.");
                    return false;
                }
            }
            else{
                System.out.println("Main HashMap cities does NOT contain a city with id=" + cityCode);
                return false;
            }
        }
        return true;
    }


    /**
     * Get existing Continent or create new one.
     * @param name of the continent
     * @return new or existing Continent object
     */
    private Continent getContinent(String name){

        Continent continent;
        if(!continents.containsKey(name)) {
            continent = new Continent(name);
            continents.put(name, continent);
        }
        else{ continent = continents.get(name); }

        return continent;
    }

    /**
     * Get existing Region or create new one.
     * @param name of the region
     * @param continent that region is assigned to
     * @return new or existing Region object
     */
    private Region getRegion(String name, Continent continent){

        Region region;

        if(!regions.containsKey(name)) {
            region = new Region(name);
            regions.put(name, region);
            region.setContinent(continent);
        }
        else{ region = regions.get(name); }

        return region;
    }

    /**
     * Get existing Country or create new one.
     * @param name of the country
     * @param population of the country
     * @param continent that country is assigned to
     * @param region that country is assigned to
     * @return new or existing Country object
     */
    private Country getCountry(String name, int population, Continent continent, Region region){

        Country country;

        if(!countries.containsKey(name)){
            country = new Country(name, population);
            countries.put(name, country);
            country.setContinent(continent);
            country.setRegion(region);
        }
        else{ country = countries.get(name); }

        return country;
    }

    /**
     * Get existing District or create new one.
     * @param name of the district
     * @param country that district is assigned to
     * @return new or existing District object
     */
    private District getDistrict(String name, Country country){

        District district;

        if(!districts.containsKey(name)){
            district = new District(name);
            districts.put(name, district);
            district.setCountry(country);
        }
        else{ district = districts.get(name); }

        return district;
    }

    /**
     * Get existing City or create new one.
     * @param id of the city
     * @param name of the city
     * @param population of the city
     * @param country that city is assigned to
     * @param district that city is assigned to
     * @return new or existing District object
     */
    private City getCity(int id, String name, int population, Country country, District district){

        City city;

        if(!cities.containsKey(id)){
            city = new City(id, name, population);
            cities.put(id, city);
            city.setCountry(country);
            city.setDistrict(district);
        }
        else{ city = cities.get(id); }

        return city;
    }

    /**
     * Set population counts for districts, regions and continents.
     */
    private void setPopulations(){

        // set districts population
        districts.forEach((name, district) -> { district.setPopulation(); });

        // set regions population
        regions.forEach((name, region) -> { region.setPopulation(); });

        // set continents population
        continents.forEach((name, continent) -> { continent.setPopulation(); });
    }

    /**
     * Set main language for each country.
     */
    private void setMainLanguages(){
        countries.forEach((name, country) -> { country.setMainLanguage(); });
    }

    public ArrayList<Country> countriesByPop()
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.Continent, country.region, country.population, city.Name "
                            + "FROM country "
                            + "JOIN city ON country.Code = city.Countrycode "
                            + "WHERE country.capital=city.ID "
                            + "ORDER BY country.population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country country = new Country();
                country.setCode(rset.getString("Country.Code"));
                country.setName(rset.getString("Country.Name"));
                country.setContinent(new Continent((rset.getString("Country.Continent"))));
                country.setRegion(new Region(rset.getString("Country.Region")));
                country.setPopulation(rset.getInt("Country.Population"));
                country.setCapital(new City(rset.getString("City.Name")));

                countries.add(country);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "Code", "Name", "Continent", "Region", "Population", "Capital"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "---", "---", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (Country i : countries)
            {
                String country_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |",
                                i.getCode(), i.getName(), i.getContinent().getName(), i.getRegion().getName(), i.getPopulation(), i.getCapital().getName());
                System.out.println(country_string);
            }


            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<Country> countriesByPopInContinent(String continent)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.Continent, country.region, country.population, city.Name "
                            + "FROM country "
                            + "JOIN city ON country.Code = city.Countrycode "
                            + "WHERE country.capital=city.ID "
                            + "AND country.Continent='" + continent + "'"
                            + "ORDER BY country.population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country country = new Country();
                country.setCode(rset.getString("Country.Code"));
                country.setName(rset.getString("Country.Name"));
                country.setContinent(new Continent((rset.getString("Country.Continent"))));
                country.setRegion(new Region(rset.getString("Country.Region")));
                country.setPopulation(rset.getInt("Country.Population"));
                country.setCapital(new City(rset.getString("City.Name")));

                countries.add(country);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "Code", "Name", "Continent", "Region", "Population", "Capital"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "---", "---", "---", "---", "---", "---"));
            // Loop over all employees in the list
            for (Country i : countries)
            {
                String country_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |",
                                i.getCode(), i.getName(), i.getContinent().getName(), i.getRegion().getName(), i.getPopulation(), i.getCapital().getName());
                System.out.println(country_string);
            }


            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<Country> countriesByPopInRegion(String region)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.Continent, country.region, country.population, city.Name "
                            + "FROM country "
                            + "JOIN city ON country.Code = city.Countrycode "
                            + "WHERE country.capital=city.ID "
                            + "AND country.region='" + region + "'"
                            + "ORDER BY country.population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country country = new Country();
                country.setCode(rset.getString("Country.Code"));
                country.setName(rset.getString("Country.Name"));
                country.setContinent(new Continent((rset.getString("Country.Continent"))));
                country.setRegion(new Region(rset.getString("Country.Region")));
                country.setPopulation(rset.getInt("Country.Population"));
                country.setCapital(new City(rset.getString("City.Name")));

                countries.add(country);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "Code", "Name", "Continent", "Region", "Population", "Capital"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "---", "---", "---", "---", "---", "---"));
            // Loop over all employees in the list
            for (Country i : countries)
            {
                String country_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |",
                                i.getCode(), i.getName(), i.getContinent().getName(), i.getRegion().getName(), i.getPopulation(), i.getCapital().getName());
                System.out.println(country_string);
            }


            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<Country> NcountriesByPop(Integer N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.Continent, country.region, country.population, city.Name "
                            + "FROM country "
                            + "JOIN city ON country.Code = city.Countrycode "
                            + "WHERE country.capital=city.ID "
                            + "ORDER BY country.population DESC "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country country = new Country();
                country.setCode(rset.getString("Country.Code"));
                country.setName(rset.getString("Country.Name"));
                country.setContinent(new Continent((rset.getString("Country.Continent"))));
                country.setRegion(new Region(rset.getString("Country.Region")));
                country.setPopulation(rset.getInt("Country.Population"));
                country.setCapital(new City(rset.getString("City.Name")));

                countries.add(country);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "Code", "Name", "Continent", "Region", "Population", "Capital"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "---", "---", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (Country i : countries)
            {
                String country_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |",
                                i.getCode(), i.getName(), i.getContinent().getName(), i.getRegion().getName(), i.getPopulation(), i.getCapital().getName());
                System.out.println(country_string);
            }


            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<Country> NcountriesByPopInContinent(String continent, Integer N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.Continent, country.region, country.population, city.Name "
                            + "FROM country "
                            + "JOIN city ON country.Code = city.Countrycode "
                            + "WHERE country.capital=city.ID "
                            + "AND country.Continent='" + continent + "'"
                            + "ORDER BY country.population DESC "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country country = new Country();
                country.setCode(rset.getString("Country.Code"));
                country.setName(rset.getString("Country.Name"));
                country.setContinent(new Continent((rset.getString("Country.Continent"))));
                country.setRegion(new Region(rset.getString("Country.Region")));
                country.setPopulation(rset.getInt("Country.Population"));
                country.setCapital(new City(rset.getString("City.Name")));

                countries.add(country);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "Code", "Name", "Continent", "Region", "Population", "Capital"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "---", "---", "---", "---", "---", "---"));
            // Loop over all employees in the list
            for (Country i : countries)
            {
                String country_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |",
                                i.getCode(), i.getName(), i.getContinent().getName(), i.getRegion().getName(), i.getPopulation(), i.getCapital().getName());
                System.out.println(country_string);
            }


            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<Country> NcountriesByPopInRegion(String region, Integer N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.code, country.name, country.Continent, country.region, country.population, city.Name "
                            + "FROM country "
                            + "JOIN city ON country.Code = city.Countrycode "
                            + "WHERE country.capital=city.ID "
                            + "AND country.region='" + region + "'"
                            + "ORDER BY country.population DESC "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country country = new Country();
                country.setCode(rset.getString("Country.Code"));
                country.setName(rset.getString("Country.Name"));
                country.setContinent(new Continent((rset.getString("Country.Continent"))));
                country.setRegion(new Region(rset.getString("Country.Region")));
                country.setPopulation(rset.getInt("Country.Population"));
                country.setCapital(new City(rset.getString("City.Name")));

                countries.add(country);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "Code", "Name", "Continent", "Region", "Population", "Capital"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |", "---", "---", "---", "---", "---", "---"));
            // Loop over all employees in the list
            for (Country i : countries)
            {
                String country_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s | %-10s | %-10s |",
                                i.getCode(), i.getName(), i.getContinent().getName(), i.getRegion().getName(), i.getPopulation(), i.getCapital().getName());
                System.out.println(country_string);
            }


            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public ArrayList<City> citiesByPop()
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "ORDER BY city.Population desc ";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> citiesInContinentByPop(String continent)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE country.Continent='" + continent + "'"
                            + "ORDER BY city.Population desc" ;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> citiesInRegionByPop(String region)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE country.Region='" + region + "'"
                            + "ORDER BY city.Population desc" ;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> citiesInCountryByPop(String country)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE country.name='" + country + "'"
                            + "ORDER BY city.Population desc" ;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }
    public ArrayList<City> citiesInDistrictByPop(String district)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE city.District='" + district + "'"
                            + "ORDER BY city.Population desc" ;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> NcitiesByPop(int N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "ORDER BY city.Population desc "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> NcitiesInContinentByPop(String continent, int N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE country.Continent='" + continent + "'"
                            + "ORDER BY city.Population desc "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> NcitiesInRegionByPop(String region, int N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE country.Region='" + region + "'"
                            + "ORDER BY city.Population desc "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    public ArrayList<City> NcitiesInCountryByPop(String country, int N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "AND country.name='" + country + "'"
                            + "ORDER BY city.Population desc "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));

            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }
    public ArrayList<City> NcitiesInDistrictByPop(String district, int N)
    {

        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.NAME, country.name, district, city.population "
                            + "FROM city "
                            + "JOIN country ON country.code = city.CountryCode "
                            + "WHERE city.District='" + district + "'"
                            + "ORDER BY city.Population desc "
                            + "LIMIT " + N;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City city = new City();

                city.setName(rset.getString("city.Name"));
                city.setCountry(new Country(rset.getString("Country.Name")));
                city.setDistrict(new District(rset.getString("city.District")));
                city.setPopulation(rset.getInt("city.population"));



                cities.add(city);
            }

            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "Name", "Country", "District", "Population"));
            System.out.println(String.format("| %-4s | %-20s | %-10s | %-10s |", "---", "---", "---", "---"));


            // Loop over all employees in the list
            for (City i : cities)
            {
                String city_string =
                        String.format("| %-4s | %-20s | %-10s | %-10s |",
                                i.getName(), i.getCountry().getName(), i.getDistrict().getName(), i.getPopulation());
                System.out.println(city_string);
            }


            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }



}

