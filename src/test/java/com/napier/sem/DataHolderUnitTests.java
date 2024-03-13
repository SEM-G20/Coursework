package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DataHolderUnitTests {

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
    void testCountriesByPop(){
        assertNotNull(dataHolder.countriesByPop());
    }

    @Test
    void testcontentCountriesByPop(){
        ArrayList<Country> countries = dataHolder.countriesByPop();
        countries.forEach(c -> assertNotNull(c));
    }

    @Test
    void testCountriesByPopInContinent(){
        assertNotNull(dataHolder.countriesByPopInContinent("Europe"));
        // todo: test for null argument
        // todo: test for invalid argument e.g. "qd3wd17"
    }

    @Test
    void testCountriesByPopInRegion()
    {
        // todo: test for valid argument
        // todo: test for null argument
        // todo: test for invalid argument e.g. "qd3wd17"
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
