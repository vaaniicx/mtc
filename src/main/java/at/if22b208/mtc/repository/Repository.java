package at.if22b208.mtc.repository;

import java.util.List;
import java.util.Optional;

import at.if22b208.mtc.exception.DatabaseTransactionException;

/**
 * The {@code Repository} interface represents a generic repository for database operations.
 *
 * <p>This interface provides common methods for finding, creating, and retrieving entities from the database.</p>
 *
 * @param <E> The type of the entity.
 * @param <T> The type of the entity identifier.
 */
public interface Repository<E, T> {
    /**
     * The default database schema for queries.
     */
    String SCHEMA = "public.";

    /**
     * The SQL sequence to generate a new UUID.
     */
    String GENERATE_UUID_SEQUENCE_STRING = "SELECT uuid_generate_v4()";

    /**
     * Retrieves all entities of type {@code E} from the database.
     *
     * @return A list of optional entities in the database.
     */
    List<Optional<E>> findAll()
            throws DatabaseTransactionException;

    /**
     * Finds an entity by its identifier in the database.
     *
     * @param t The identifier of the entity to find.
     * @return An Optional containing the found entity, or an empty Optional if not found.
     */
    Optional<E> findById(T t)
            throws DatabaseTransactionException;

    /**
     * Creates a new entity in the database.
     *
     * @param e The entity to be created.
     * @return The created entity.
     */
    E create(E e)
            throws DatabaseTransactionException;
}
