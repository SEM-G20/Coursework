package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.testng.annotations.Test;

import javax.xml.crypto.Data;

public class AppTest
{
    static DataHolder dataHolder;

    @BeforeAll
    static void init()
    {
        DataHolder dataHolder = new DataHolder();
    }

    @Test
    void countriesByPopTest()
    {
        dataHolder.countriesByPop();
    }
    void countriesByPopInContinentTest()
    {
        dataHolder.countriesByPopInContinent(null);
    }
    void countriesByPopInRegionTest()
    {
        dataHolder.countriesByPopInRegion(null);
    }
    void NcountriesByPopTest()
    {
        dataHolder.NcountriesByPop(null);
    }
    void NcountriesByPopInContinentTest()
    {
        dataHolder.NcountriesByPopInContinent(null,null);
    }
    void NcountriesByPopInRegionTest()
    {
        dataHolder.NcountriesByPopInRegion(null,null);
    }
}