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
    }

    @Test
    void countriesByPopInContinentTest()
    {
        //dataHolder.countriesByPopInContinent(null);
    }
    @Test
    void countriesByPopInRegionTest()
    {
        //dataHolder.countriesByPopInRegion(null);
    }
    @Test
    void NcountriesByPopTest()
    {
        //dataHolder.NcountriesByPop(null);
    }
    @Test
    void NcountriesByPopInContinentTest()
    {
        //dataHolder.NcountriesByPopInContinent(null,null);
    }
    @Test
    void NcountriesByPopInRegionTest()
    {
        //dataHolder.NcountriesByPopInRegion(null,null);
    }
}
