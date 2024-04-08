package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AppUnitTests
{
    static App app;
    static DataHolder dataHolder;

    // For testing methods that print to console.
    // src: https://stackoverflow.com/a/1119559
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeAll
    static void init()
    {
        app = new App();
        dataHolder = new DataHolder();
    }

    // For testing methods that print to console.
    // src: https://stackoverflow.com/a/1119559
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    // For testing methods that print to console.
    // src: https://stackoverflow.com/a/1119559
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testConnectIfValid(){
        app = new App();
        app.connect("localhost:33060", 30000);
        assertTrue(app.isConnected());
    }

    @Test
    void testConnectIfNull(){
        app = new App();
        app.connect(null, 30);
        assertFalse(app.isConnected());
    }

    @Test
    void testConnectIfNotValid(){
        app = new App();
        app.connect("qd3wd17", 30);
        assertFalse(app.isConnected());
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
    void testDisplayMenu(){
        String expected = "Type in:\n" +
                "'world' to get world's population\n" +
                "'continent x', where x is a name of the continent, to get specified continent population\n" +
                "'region x', where x is a name of the region, to get specified region population\n" +
                "'country x', where x is a name of country, to get specified country population\n" +
                "'district x', where x is a name of district, to get specified district population\n" +
                "'city x', where x is a name of city, to get specified city population\n" +
                "'e' to exit the program\r\n";
        app.displayMenu();
        assertEquals(expected.replaceAll("\\s+",""), outContent.toString().replaceAll("\\s+",""));
    }

    @Test
    void testWriteToFileIfFileIsCreated(){
        String data = "";
        String filename = "test-file.md";
        File testFile = app.writeToFile(data, filename);
        assertTrue(testFile.exists());
    }

    @Test
    void testWriteToFileIfFileIsWrittenTo(){
        String data = "Writing to file test.";
        String filename = "test-file.md";
        File testFile = app.writeToFile(data, filename);

        StringBuilder actualData = new StringBuilder();

        try {
            Scanner myReader = new Scanner(testFile);
            while (myReader.hasNextLine()) {
                actualData.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        assertEquals(data, actualData.toString());
    }

    @Test
    void testWriteToFileIfFilenameNull(){
        String data = "";
        File testFile = app.writeToFile(data, null);
        assertNull(testFile);
    }

    @Test
    void testWriteToFileIfFilenameInvalid(){
        String data = "";
        String filename = "test-file";
        File testFile = app.writeToFile(data, filename);
        assertNull(testFile);
    }

    @Test
    void testStringToTitleCaseIfValid(){
        String str = "teSTing sTrINg";
        String[] strArr = str.split("[\\s,;:/.]+");
        String actual = app.stringToTitleCase(strArr, 0);
        String expected = "Testing String";

        assertEquals(expected, actual);
    }

    @Test
    void testStringToTitleCaseIfArrNull(){
        assertNull(app.stringToTitleCase(null, 0));
    }

    @Test
    void testStringToTitleCaseIfArrEmpty(){
        String[] strArr = {};
        assertNull(app.stringToTitleCase(strArr, 0));
    }

    @Test
    void testStringToTitleCaseIfIdxOutRange(){
        String[] strArr = {"testing", "idxRange"};
        assertNull(app.stringToTitleCase(strArr, 2));
    }

    @Test
    void testStringToTitleCaseIfIdxInvalid(){
        String[] strArr = {"testing", "idxRange"};
        assertNull(app.stringToTitleCase(strArr, -1));
    }

}