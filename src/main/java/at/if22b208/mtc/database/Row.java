package at.if22b208.mtc.database;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

/**
 * Row class representing a single row of data retrieved from a database query.
 * It contains a map of column names to their corresponding values in the row.
 */
@Getter
public class Row {
    /**
     * Map storing column names as keys and their corresponding values in the row.
     */
    private final Map<String, Object> content = new HashMap<>();

    /**
     * Adds a key-value pair to the content map representing a column and its value.
     *
     * @param column  The column name.
     * @param content The value corresponding to the column.
     */
    public void put(String column, Object content) {
        getContent().put(column, content);
    }

    /**
     * Retrieves the value of the specified column as an Integer.
     *
     * @param column The column name.
     * @return The Integer value of the specified column.
     */
    public Integer getInt(String column) {
        return (Integer) getContent().get(column);
    }

    /**
     * Retrieves the value of the specified column as a Long.
     *
     * @param column The column name.
     * @return The Long value of the specified column.
     */
    public Long getLong(String column) {
        return (Long) getContent().get(column);
    }

    /**
     * Retrieves the value of the specified column as a UUID.
     *
     * @param column The column name.
     * @return The UUID value of the specified column.
     */
    public UUID getUuid(String column) {
        return (UUID) getContent().get(column);
    }

    /**
     * Retrieves the value of the specified column as a String.
     *
     * @param column The column name.
     * @return The String value of the specified column.
     */
    public String getString(String column) {
        return (String) getContent().get(column);
    }

    /**
     * Retrieves the value of the specified column as a Double.
     *
     * @param column The column name.
     * @return The Double value of the specified column.
     */
    public Double getDouble(String column) {
        return (Double) getContent().get(column);
    }
}
