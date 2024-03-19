package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataHolderUnitTests {
    static DataHolder dataHolder;

    @BeforeAll
    static void init()
    {
        dataHolder = new DataHolder();
    }

    // Report Unit Tests
    @Test
    void testCountriesByPop(){

        //assertNotNull(dataHolder.countriesByPop());
    }
    @Test
    void testCountriesByPopContentEmpty(){
        //ArrayList<Country> countries = dataHolder.countriesByPop();
        //assertNotEquals(countries.size(), 0);
    }
    @Test
    void testCountriesByPopContentNull(){
        //ArrayList<Country> countries = dataHolder.countriesByPop();
        //countries.forEach(c -> assertNotNull(c));
    }
    @Test
    void testCountriesByPopInContinentIfValid(){
        //assertNotNull(dataHolder.countriesByPopInContinent("Europe"));
    }
    @Test
    void testCountriesByPopInContinentIfNull(){
        //assertNull(dataHolder.countriesByPopInContinent(null));
    }
    @Test
    void testCountriesByPopInContinentIfInvalid(){
        //assertNull(dataHolder.countriesByPopInContinent("qd3wd17"));
    }
    @Test
    void testCountriesByPopInRegionIfValid()
    {
        //assertNotNull(dataHolder.countriesByPopInRegion("Northern Europe"));
    }
    @Test
    void testCountriesByPopInRegionIfNull()
    {
        //assertNull(dataHolder.countriesByPopInRegion(null));
    }
    @Test
    void testCountriesByPopInRegionIfNotValid()
    {
        //assertNull(dataHolder.countriesByPopInRegion("qd3wd17"));
    }
    @Test
    void testNCountriesByPop()
    {
        // todo: test for valid argument
        // todo: test for null argument
        // todo: test for invalid argument e.g. "qd3wd17"
    }
    @Test
    void testNCountriesByPopInContinent()
    {
        // todo: test for valid argument
        // todo: test for null argument
        // todo: test for invalid argument e.g. "qd3wd17"
    }
    @Test
    void testNCountriesByPopInRegion()
    {
        // todo: test for valid argument
        // todo: test for null argument
        // todo: test for invalid argument e.g. "qd3wd17"
    }
}
