package com.example.kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
    public Table parse(String csv) {
        requireNonEmptyInput(csv);

        return toTable(lines(csv));
    }

    private Table toTable(String[] lines) {
        String[] columns = columns(lines);

        return new Table(columns, rows(lines, columns.length));
    }

    private String[][] rows(String[] lines, int columns) {
        List<String[]> result = parseRows(data(lines), columns);
        return result.toArray(new String[][]{});
    }

    private String[] data(String[] lines) {
        return Arrays.copyOfRange(lines, 1, lines.length);
    }

    private List<String[]> parseRows(String[] values, int columns) {
        List<String[]> result = new ArrayList<>();
        for (String s : values) {
            String[] row = s.split(",");
            assertNumberOfColumns(row, columns);
            result.add(row);
        }
        return result;
    }

    private static void assertNumberOfColumns(String[] row, int columns) {
        if(row.length != columns) {
            throw new IllegalArgumentException("Wrong length of columns");
        }
    }

    private String[] lines(String csv) {
        return csv.split("\n");
    }

    private void requireNonEmptyInput(String csv) {
        if (csv.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private String[] columns(String[] csv) {
        return csv[0].split(",");
    }

    public class Table {
        String[] columns;
        String[][] rows;

        private Table(String[] columns, String[][] rows) {
            this.columns = columns;
            this.rows = rows;
        }

        public String[] getColumns() {
            return columns;
        }

        public int rows() {
            return rows.length;
        }

        public String getValue(String column, int row) {
            assertValidRow(row);
            return rows[row][indexFor(column)];
        }

        private void assertValidRow(int row) {
            if (row >= rows.length) {
                throw new IllegalArgumentException("illgeal row index: " + row);
            }
        }

        private int indexFor(String column) {
            int index = Arrays.asList(columns).indexOf(column);
            assertColumnExists(column, index);
            return index;
        }

        private void assertColumnExists(String column, int index) {
            if (index == -1) {
                throw new IllegalArgumentException("Unknown column: " + column);
            }
        }
    }
}
