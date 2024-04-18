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

            String expected = "id=" + cityIdDB + ", name=" + cityNameDB + ", " + "population=" + cityPopulationDB
                    + ", country=" + countryNameDB + ", district=" + cityDistrictDB;
            String actual = dataHolder.getCities().get(cityCode).toString();

            assertEquals(expected, actual);

        }
    }

    @Test
    public void testCountriesByPop(){
        ArrayList<Country> countries = dataHolder.countriesByPop();

        Country country = countries.get(0);

        assertEquals(country.getCode(),"CHN");
        assertEquals(country.getName(),"China");
        assertEquals(country.getContinent().getName(),"Asia");
        assertEquals(country.getRegion().getName(),"Eastern Asia");
        assertEquals(country.getPopulation(),1277558000);
        assertEquals(country.getCapital().getName(),"Peking");
    }





    @Test
    public void testCapitalCitiesInContinent() {

        ArrayList<City> cities = dataHolder.capitalCitiesInContinentByPop("Africa");

        City city = cities.get(0);

        assertEquals(city.getName(),"Cairo");
        assertEquals(city.getCountry().getName(),"Egypt");
        assertEquals(city.getPopulation(),6789479);
    }
    @Test
    public void testCapitalCitiesInRegion() {

        ArrayList<City> cities = dataHolder.capitalCitiesInRegionByPop("Caribbean");

        City city = cities.get(0);

        assertEquals(city.getName(),"La Habana");
        assertEquals(city.getCountry().getName(),"Cuba");
        assertEquals(city.getPopulation(),2256000);
    }

    @Test
    public void testCapitalCitiesInWorld() {

        ArrayList<City> cities = dataHolder.capitalCitiesByPop();

        City city = cities.get(0);

        assertEquals(city.getName(),"Seoul");
        assertEquals(city.getCountry().getName(),"South Korea");
        assertEquals(city.getPopulation(),9981619);
    }

    @Test
    public void testCitiesInWorld() {

        ArrayList<City> cities = dataHolder.citiesByPop();

        City city = cities.get(0);

        assertEquals(city.getName(),"Mumbai (Bombay)");
        assertEquals(city.getCountry().getName(),"India");
        assertEquals(city.getDistrict().getName(),"Maharashtra");
        assertEquals(city.getPopulation(),10500000);
    }

    @Test
    public void testCitiesInContinent() {

        ArrayList<City> cities = dataHolder.citiesInContinentByPop("Europe");

        City city = cities.get(0);

        assertEquals(city.getName(),"Moscow");
        assertEquals(city.getCountry().getName(),"Russian Federation");
        assertEquals(city.getDistrict().getName(),"Moscow (City)");
        assertEquals(city.getPopulation(),8389200);
    }

    @Test
    public void testCitiesInRegion() {

        ArrayList<City> cities = dataHolder.citiesInRegionByPop("Caribbean");

        City city = cities.get(0);

        assertEquals(city.getName(),"La Habana");
        assertEquals(city.getCountry().getName(),"Cuba");
        assertEquals(city.getDistrict().getName(),"La Habana");
        assertEquals(city.getPopulation(),2256000);
    }

    @Test
    public void testCitiesInCountry() {

        ArrayList<City> cities = dataHolder.citiesInCountryByPop("Spain");

        City city = cities.get(0);

        assertEquals(city.getName(),"Madrid");
        assertEquals(city.getCountry().getName(),"Spain");
        assertEquals(city.getDistrict().getName(),"Madrid");
        assertEquals(city.getPopulation(),2879052);
    }

    @Test
    public void testCitiesInDistrict() {

        ArrayList<City> cities = dataHolder.citiesInDistrictByPop("Aichi");

        City city = cities.get(0);

        assertEquals(city.getName(),"Nagoya");
        assertEquals(city.getCountry().getName(),"Japan");
        assertEquals(city.getDistrict().getName(),"Aichi");
        assertEquals(city.getPopulation(),2154376);
    }

    @Test
    public void testCountriesInContinent() {

        ArrayList<Country> countries = dataHolder.countriesByPopInContinent("Europe");

        Country country = countries.get(0);

        assertEquals(country.getCode(),"RUS");
        assertEquals(country.getName(),"Russian Federation");
        assertEquals(country.getContinent().getName(),"Europe");
        assertEquals(country.getRegion().getName(),"Eastern Europe");
        assertEquals(country.getPopulation(),146934000);
        assertEquals(country.getCapital().getName(),"Moscow");

    }

    @Test
    public void testCountriesInRegion() {

        ArrayList<Country> countries = dataHolder.countriesByPopInRegion("Western Europe");

        Country country = countries.get(0);

        assertEquals(country.getCode(),"DEU");
        assertEquals(country.getName(),"Germany");
        assertEquals(country.getContinent().getName(),"Europe");
        assertEquals(country.getPopulation(),82164700);
        assertEquals(country.getCapital().getName(),"Berlin");

    }


    @Test
    public void testNCapitalsInContinent() {

        ArrayList<City> cities = dataHolder.NcapitalCitiesInContinentByPop("Africa",5);

        City city = cities.get(0);

        assertEquals(city.getName(),"Cairo");
        assertEquals(city.getCountry().getName(),"Egypt");
        assertEquals(city.getPopulation(),6789479);
    }

    @Test
    public void testNCapitalsInWorld() {

        ArrayList<City> cities = dataHolder.NcapitalCitiesByPop(5);

        City city = cities.get(0);

        assertEquals(city.getName(),"Seoul");
        assertEquals(city.getCountry().getName(),"South Korea");
        assertEquals(city.getPopulation(),9981619);
    }

    @Test
    public void testNCapitalsInRegion() {

        ArrayList<City> cities = dataHolder.NcapitalCitiesInRegionByPop("Caribbean",5);

        City city = cities.get(0);

        assertEquals(city.getName(),"La Habana");
        assertEquals(city.getCountry().getName(),"Cuba");
        assertEquals(city.getPopulation(),2256000);
    }

    @Test
    public void testNCitiesInWorld() {

        ArrayList<City> cities = dataHolder.NcitiesByPop(5);

        City city = cities.get(0);

        assertEquals(city.getName(),"Mumbai (Bombay)");
        assertEquals(city.getCountry().getName(),"India");
        assertEquals(city.getDistrict().getName(),"Maharashtra");
        assertEquals(city.getPopulation(),10500000);
    }

    @Test
    public void testNCitiesInContinent() {

        ArrayList<City> cities = dataHolder.NcitiesInContinentByPop("Europe",5);

        City city = cities.get(0);

        assertEquals(city.getName(),"Moscow");
        assertEquals(city.getCountry().getName(),"Russian Federation");
        assertEquals(city.getDistrict().getName(),"Moscow (City)");
        assertEquals(city.getPopulation(),8389200);
    }

    @Test
    public void testNCitiesInRegion() {

        ArrayList<City> cities = dataHolder.NcitiesInRegionByPop("Caribbean",5);

        City city = cities.get(0);

        assertEquals(city.getName(),"La Habana");
        assertEquals(city.getCountry().getName(),"Cuba");
        assertEquals(city.getDistrict().getName(),"La Habana");
        assertEquals(city.getPopulation(),2256000);
    }

    @Test
    public void testNCitiesInCountry() {

        ArrayList<City> cities = dataHolder.NcitiesInCountryByPop("Spain",5);

        City city = cities.get(0);

        assertEquals(city.getName(),"Madrid");
        assertEquals(city.getCountry().getName(),"Spain");
        assertEquals(city.getDistrict().getName(),"Madrid");
        assertEquals(city.getPopulation(),2879052);
    }

    @Test
    public void testNCitiesInDistrict() {

        ArrayList<City> cities = dataHolder.NcitiesInDistrictByPop("Aichi",3);

        City city = cities.get(0);

        assertEquals(city.getName(),"Nagoya");
        assertEquals(city.getCountry().getName(),"Japan");
        assertEquals(city.getDistrict().getName(),"Aichi");
        assertEquals(city.getPopulation(),2154376);
    }

    @Test
    public void testNCountriesInContinent() {

        ArrayList<Country> countries = dataHolder.NcountriesByPopInContinent("Europe",5);

        Country country = countries.get(0);

        assertEquals(country.getCode(),"RUS");
        assertEquals(country.getName(),"Russian Federation");
        assertEquals(country.getContinent().getName(),"Europe");
        assertEquals(country.getRegion().getName(),"Eastern Europe");
        assertEquals(country.getPopulation(),146934000);
        assertEquals(country.getCapital().getName(),"Moscow");

    }

    @Test
    public void testNCountriesInWorld() {

        ArrayList<Country> countries = dataHolder.NcountriesByPop(5);

        Country country = countries.get(0);

        assertEquals(country.getCode(),"CHN");
        assertEquals(country.getName(),"China");
        assertEquals(country.getContinent().getName(),"Asia");
        assertEquals(country.getRegion().getName(),"Eastern Asia");
        assertEquals(country.getPopulation(),1277558000);
        assertEquals(country.getCapital().getName(),"Peking");

    }

    @Test
    public void testNCountriesInRegion() {

        ArrayList<Country> countries = dataHolder.NcountriesByPopInRegion("Western Europe",4);

        Country country = countries.get(0);

        assertEquals(country.getCode(),"DEU");
        assertEquals(country.getName(),"Germany");
        assertEquals(country.getContinent().getName(),"Europe");
        assertEquals(country.getPopulation(),82164700);
        assertEquals(country.getCapital().getName(),"Berlin");

    }

    @Test
    public void testGetWorldPop() {

        ArrayList<Country> countries = dataHolder.getWorldPop();

        Country country = countries.get(0);

        assertEquals(country.getName(),"World");
        assertEquals(country.getPopulation(),6078749450L);

    }

    @Test
    public void testGetRegionPop() {
        ArrayList<Country> countries = dataHolder.getRegionPop("Caribbean");

        Country country = countries.get(0);

        assertEquals(country.getRegion().getName(),"Caribbean");
        assertEquals(country.getPopulation(),38140000);
    }

    @Test
    public void testGetDistrictPop() {
        ArrayList<City> cities = dataHolder.getDistricPop("Galicia");

        City city = cities.get(0);

        assertEquals(city.getDistrict().getName(),"Galicia");
        assertEquals(city.getPopulation(),729937);
    }

    @Test
    public void testGetCountryPop() {
        ArrayList<Country> countries = dataHolder.getCountryPop("Albania");

        Country country = countries.get(0);

        assertEquals(country.getName(),"Albania");
        assertEquals(country.getPopulation(),3401200);
    }

    @Test
    public void testGetContinentPop() {
        ArrayList<Country> countries = dataHolder.getContinentPop("Asia");

        Country country = countries.get(0);

        assertEquals(country.getContinent().getName(),"Asia");
        assertEquals(country.getPopulation(),3705025700L);
    }

    @Test
    public void testGetCityPop() {
        ArrayList<City> cities = dataHolder.getCityPop("Aba");

        City city = cities.get(0);

        assertEquals(city.getName(),"Aba");
        assertEquals(city.getPopulation(),298900);
    }



}
