package com.example.kata;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CsvParserTest {

    private static final String COLUMN = "SampleColumn";

    private static final String COLUMN_1 = "Name";
    private static final String COLUMN_2 = "Age";
    private static final String COLUMN_3 = "Gender";

    private static final String SEPARATOR = ",";

    private static final String EMPTY_INPUT = "";

    @Test
    public void readsSingleColumnName() {
        CsvParser.Table result = new CsvParser().parse(COLUMN);

        assertArrayEquals(new String[]{COLUMN}, result.getColumns());
        assertEquals(0, result.rows());
    }

    @Test
    public void readsMultipleColumns() {
        CsvParser.Table result = new CsvParser().parse(COLUMN_1 + SEPARATOR + COLUMN_2 + SEPARATOR + COLUMN_3);

        assertArrayEquals(new String[]{COLUMN_1, COLUMN_2, COLUMN_3}, result.getColumns());
        assertEquals(0, result.rows());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnEmptyInput() {
        new CsvParser().parse(EMPTY_INPUT);
    }

    @Test
    public void readsSingleDataLine() {
        CsvParser.Table result = new CsvParser().parse("SampleColumn\nInput");

        assertArrayEquals(new String[]{COLUMN}, result.getColumns());
        assertEquals(1, result.rows());
        assertEquals("Input", result.getValue("SampleColumn", 0));
    }

    @Test
    public void readsSingleDataLineWithMultipleColumns() {
        CsvParser.Table result = new CsvParser().parse("col1,col2,col3\nvalue1,value2,value3");

        assertArrayEquals(new String[]{"col1", "col2", "col3"}, result.getColumns());
        assertEquals(1, result.rows());
        assertEquals("value1", result.getValue("col1", 0));
        assertEquals("value2", result.getValue("col2", 0));
        assertEquals("value3", result.getValue("col3", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnUnknownColumn() {
        CsvParser.Table result = new CsvParser().parse("col1,col2,col3\nvalue1,value2,value3");

        result.getValue("UNKOWN", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnIllegalRow() {
        CsvParser.Table result = new CsvParser().parse("col1\nvalue1");

        result.getValue("col1", 1);
    }

    @Test
    public void readsMultipleDataLines() {
        CsvParser.Table result = new CsvParser().parse(
                "c1,c2,c3\n" +
                        "c1r0,c2r0,c3r0\n" +
                        "c1r1,c2r1,c3r1\n" +
                        "c1r2,c2r2,c3r2");

        assertEquals(3, result.rows());
        assertEquals("c1r0", result.getValue("c1", 0));
        assertEquals("c2r0", result.getValue("c2", 0));
        assertEquals("c3r0", result.getValue("c3", 0));
        assertEquals("c1r1", result.getValue("c1", 1));
        assertEquals("c2r1", result.getValue("c2", 1));
        assertEquals("c3r1", result.getValue("c3", 1));
        assertEquals("c1r2", result.getValue("c1", 2));
        assertEquals("c2r2", result.getValue("c2", 2));
        assertEquals("c3r2", result.getValue("c3", 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnParsingIfLineLengthDiffers() {
        new CsvParser().parse(
                "c1,c2,c3\n" +
                        "c1r0,c2r0\n" +
                        "c1r1,c2r1,c3r1\n" +
                        "c1r2,c2r2,c3r2");
    }
}
