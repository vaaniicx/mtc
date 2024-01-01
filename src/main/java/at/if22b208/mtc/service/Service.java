package at.if22b208.mtc.service;

import java.util.List;

public interface Service<E, T> {
    E create(E e);
    List<E> getAll();
    E getById(T t);
}
