package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTests {
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
        Continent continent = dataHolder.getContinents().get("Europe");
        assertNotNull(continent);
    }
}
