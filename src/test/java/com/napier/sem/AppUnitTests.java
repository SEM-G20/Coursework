package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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