package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
}
