package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppUnitTests
{
    static App app;
    static DataHolder dataHolder;

    @BeforeAll
    static void init()
    {
        app = new App();
        dataHolder = new DataHolder();
    }

    @Test
    void testConnect(){
        app = new App();
        app.connect("localhost:33060", 30000);
        assertTrue(app.isConnected());
    }

    @Test
    void testDisconnect(){
        app = new App();
        app.connect("localhost:33060", 30000);
        app.disconnect();
        assertFalse(app.isConnected());
    }

    @Test
    void testCreateDataHolder(){
        dataHolder = app.createDataHolder();
        assertTrue(dataHolder.isConnected());
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