package com.napier.sem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        // todo: test for invalid arguments
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

}