package at.if22b208.mtc.database;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class Database implements AutoCloseable {
    private static Database INSTANCE;
    // TODO: config file
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_SCHEMA = "mtc";
    private Connection connection;

    private Database() {
    }

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

    public boolean isConnected() {
        return this.connection != null;
    }

    public void executeUpdateQuery(String query, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public UUID executeCreateQuery(String query, Object... params) {
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
        }
        return null;
    }

    public Result executeSelectQuery(String query, Object... params) {
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
        }
        return result;
    }

    public static synchronized Database getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

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
}
