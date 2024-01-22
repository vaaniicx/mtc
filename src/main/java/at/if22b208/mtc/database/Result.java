package at.if22b208.mtc.database;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Result class representing the outcome of a database query.
 * It contains a list of rows retrieved from the query result.
 */
@Getter
@Builder
public class Result {
    /**
     * List of rows retrieved from the query result.
     */
    private final List<Row> rows = new ArrayList<>();

    /**
     * Adds a row to the list of rows in the result.
     *
     * @param row The row to be added to the result.
     */
    public void addRow(Row row) {
        getRows().add(row);
    }
}
