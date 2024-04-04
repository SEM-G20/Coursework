package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DataHolderIntegrationTests {
    static App app;
    static DataHolder dataHolder;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);
        dataHolder = app.createDataHolder();
    }

    @Test
    void testLoadData(){
        dataHolder.loadData();
        assertFalse(dataHolder.getContinents().isEmpty());
        assertFalse(dataHolder.getRegions().isEmpty());
        assertFalse(dataHolder.getCountries().isEmpty());
        assertFalse(dataHolder.getDistricts().isEmpty());
        assertFalse(dataHolder.getCities().isEmpty());
    }

    /**
     * Check if the Region Object from the main hashmap in App object points to the same memory address
     * as the Region saved in the internal collection of the Continent in DataHolder object
     */
    @Test
    public void testRegionsCollectionEquality(){

        // loop through each continent in the main continents hashmap
        for(Map.Entry<String, Continent> continentKeyValue : dataHolder.getContinents().entrySet()){

            String continentName = continentKeyValue.getKey();
            Continent continentObject = continentKeyValue.getValue();

            // get each continent hashmap of its regions
            HashMap<String, Region> continentRegions = continentObject.getRegions();

            // loop through each region in the main regions hashmap
            for(Map.Entry<String, Region> regionKeyValue : dataHolder.getRegions().entrySet()){
                String regionName = regionKeyValue.getKey();
                Region regionObject = regionKeyValue.getValue();

                // check if the region saved in the internal continent's collection
                // points to the same memory address as the region from main regions hashmap
                if(continentRegions.containsKey(regionName)){

                    assertEquals(continentRegions.get(regionName), regionObject);

                }
            }
        }
    }

    /**
     * Helper for testCitiesDataMatching method.
     * Get an array of rows from the database containing all cities in the specified country data.
     */
    private ArrayList<ArrayList<String>> helperGetCitiesData(String country){
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
            Statement stmt = dataHolder.getStatement();

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
    @Test
    public void testCitiesDataMatching(){

        // get array with rows from database that contains cities in the UK
        ArrayList<ArrayList<String>> rows = helperGetCitiesData("United Kingdom");

        for(ArrayList<String> row : rows){
            Integer cityCode = Integer.valueOf(row.get(0));

            // creating a new object from data from database to compare

            // get data from database that should match the one in the Objects
            String cityIdDB = row.get(0);
            String cityNameDB = row.get(1);
            String cityDistrictDB = row.get(2);
            int cityPopulationDB = Integer.parseInt(row.get(3));
            String countryNameDB = row.get(4);
            //String continentDB = row.get(5);
            //String regionDB = row.get(6);
            //int countryPopulationDB = Integer.parseInt(row.get(7));

            String expected = "city id=" + cityIdDB + ", name=" + cityNameDB + ", " + "population=" + cityPopulationDB
                    + ", country=" + countryNameDB + ", district=" + cityDistrictDB;
            String actual = dataHolder.getCities().get(cityCode).toString();

            assertEquals(expected, actual);

        }
    }
}
