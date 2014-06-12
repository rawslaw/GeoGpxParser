package zeroone3010.geogpxparser.outputformatters;

import zeroone3010.geogpxparser.tabular.TableData;

/**
 * This class defines methods for formatting tabular data.
 */
public abstract class AbstractTabularDataFormatter {
    private final TableData table;
    public AbstractTabularDataFormatter(TableData data) {
        this.table = data;
    }

    protected TableData getTable() {
        return this.table;
    }

    @Override
    public abstract String toString();

    public abstract String getFileExtension();

    public String getFileName() {
        return this.table.getIdentifier() + "." + getFileExtension();
    }
}