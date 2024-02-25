package at.if22b208.mtc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import at.if22b208.mtc.exception.DatabaseTransactionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Database class for managing PostgreSQL database interactions.
 * Implements AutoCloseable to ensure proper resource management.
 */
@Slf4j
@Getter
public class Database implements AutoCloseable {
    private static Database INSTANCE;
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_SCHEMA = "mtc";
    private Connection connection;

    private Database() {
        // Private constructor to ensure singleton pattern.
    }

    /**
     * Connects to the PostgreSQL database using the specified credentials.
     */
    public void connect() {
        String url = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_SCHEMA + "?user=" + DB_USER +
                "&password=" + DB_PASSWORD;
        try {
            this.connection = DriverManager.getConnection(url);
            log.info("Connected to the PostgreSQL database successfully");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Checks if the database is currently connected.
     *
     * @return True if connected, false otherwise.
     */
    public boolean isConnected() {
        return this.connection != null;
    }

    /**
     * Executes an update SQL query.
     *
     * @param query  The SQL query to execute.
     * @param params Parameters to be used in the query.
     */
    public void executeUpdateQuery(String query, Object... params) throws DatabaseTransactionException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseTransactionException("");
        }
    }

    /**
     * Executes an insert SQL query.
     *
     * @param query  The SQL query to execute.
     * @param params Parameters to be used in the query.
     * @return The UUID of the inserted row.
     */
    public UUID executeInsertQuery(String query, Object... params) throws DatabaseTransactionException {
        try (PreparedStatement statement = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                log.error("Creating query failed, no rows affected.");
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (UUID) generatedKeys.getObject(1);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseTransactionException("");
        }
        return null;
    }

    /**
     * Executes a select SQL query and returns the result.
     *
     * @param query  The SQL query to execute.
     * @param params Parameters to be used in the query.
     * @return The result of the select query.
     */
    public Result executeSelectQuery(String query, Object... params) throws DatabaseTransactionException {
        Result result = Result.builder().build();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            final ResultSet resultSet = statement.executeQuery();
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            List<String> columns = new ArrayList<>();

            for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
                columns.add(resultSetMetaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Row row = new Row();
                for (String entry : columns) {
                    row.put(entry, resultSet.getObject(entry));
                }
                result.addRow(row);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DatabaseTransactionException("");
        }
        return result;
    }

    /**
     * Closes the database connection.
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Gets the singleton instance of the {@code Database}.
     *
     * @return The singleton instance of the {@code Database}.
     */
    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }
}
