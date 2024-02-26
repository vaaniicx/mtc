package at.if22b208.mtc.database;

import java.sql.Connection;
import java.sql.SQLException;

import at.if22b208.mtc.exception.DatabaseTransactionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Transaction {

    private final Connection connection;

    public Transaction() {
        this.connection = Database.getInstance().getConnection();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Failed to disable auto-commit mode: {}", e.getMessage());
        }
    }

    public void commit()
            throws DatabaseTransactionException {
        if (this.connection != null) {
            try {
                this.connection.commit();
            } catch (SQLException e) {
                throw new DatabaseTransactionException("Failed to commit transaction.");
            }
        }

    }

    public void rollback()
            throws DatabaseTransactionException {
        if (this.connection != null) {
            try {
                this.connection.rollback();
            } catch (SQLException e) {
                throw new DatabaseTransactionException("Failed to rollback transaction.");
            }
        }
    }
}
