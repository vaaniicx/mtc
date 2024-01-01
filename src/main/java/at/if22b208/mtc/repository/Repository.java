package at.if22b208.mtc.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, T> {
    String SCHEMA = "public.";
    String GENERATE_UUID_SEQUENCE_STRING = "SELECT uuid_generate_v4()";
    List<E> findAll();
    Optional<E> findById(T t);
    E create(E e);
}
