package com.napier.sem;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;
    static DataHolder dataHolder;

    @BeforeAll
    static void init()
    {
        // this block below is what it needs to be done to get valid dataHolder object
        // !! BUT it doesn't connect to the database
        app = new App();
        app.connect();
        dataHolder = app.createDataHolder();
        dataHolder.loadData();
    }

    @Test
    void countriesByPopTest()
    {

        //dataHolder.countriesByPop();
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