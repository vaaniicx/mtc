package at.if22b208.mtc.service;

import java.util.List;

import at.if22b208.mtc.exception.DatabaseTransactionException;
import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.exception.InvalidTradingDealException;

/**
 * The {@code Service} interface provides a generic contract for common operations related to entities in the application.
 *
 * @param <E> The type of the entity.
 * @param <T> The type of the entity's identifier (e.g., UUID, Long).
 */
public interface Service<E, T> {
    /**
     * Creates a new entity in the system.
     *
     * @param e The entity to be created.
     * @return The created entity.
     * @throws InvalidPackageException     If there is an issue with the entity creation (specific to the application context).
     * @throws InvalidTradingDealException If there is an issue with the entity creation (specific to the application context).
     */
    E create(E e)
            throws InvalidPackageException, InvalidTradingDealException, DatabaseTransactionException;

    /**
     * Retrieves a list of all entities in the system.
     *
     * @return A list of all entities in the system.
     */
    List<E> getAll()
            throws DatabaseTransactionException;

    /**
     * Retrieves an entity by its identifier from the system.
     *
     * @param t The identifier of the entity to retrieve.
     * @return The entity with the specified identifier, or {@code null} if not found.
     */
    E getById(T t)
            throws DatabaseTransactionException;
}
