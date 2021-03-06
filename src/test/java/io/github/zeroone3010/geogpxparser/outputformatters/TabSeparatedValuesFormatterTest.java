package io.github.zeroone3010.geogpxparser.outputformatters;

import io.github.zeroone3010.geogpxparser.tabular.CellData;
import io.github.zeroone3010.geogpxparser.tabular.TableData;
import io.github.zeroone3010.geogpxparser.tabular.TableRow;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the TabSeparatedValuesFormatter.
 */
public class TabSeparatedValuesFormatterTest {

    private final TableData table;

    public TabSeparatedValuesFormatterTest() {
        table = new TableData("testTable");
        final TableRow row1 = new TableRow(true);
        final TableRow row2 = new TableRow(false);
        final TableRow row3 = new TableRow(false);
        row1.addCell(new CellData("one"));
        row1.addCell(new CellData("two"));
        row1.addCell(new CellData("three"));

        row2.addCell(new CellData("four"));
        row2.addCell(new CellData("fi\tve"));
        row2.addCell(new CellData("six"));

        row3.addCell(new CellData("seven"));
        row3.addCell(new CellData("eight"));
        row3.addCell(new CellData("nine"));
        table.addRow(row1);
        table.addRow(row2);
        table.addRow(row3);
    }

    @Test
    public void test_formatting_a_table() {
        assertNotNull(table);

        final String result = new TabSeparatedValuesFormatter(table).toString();
        assertEquals("one\ttwo\tthree\nfour\tfi ve\tsix\nseven\teight\tnine", result);
    }
}
