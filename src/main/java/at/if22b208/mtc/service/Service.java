package at.if22b208.mtc.service;

import at.if22b208.mtc.exception.InvalidPackageException;
import at.if22b208.mtc.exception.InvalidTradingDealException;

import java.util.List;

public interface Service<E, T> {
    E create(E e) throws InvalidPackageException, InvalidTradingDealException;
    List<E> getAll();
    E getById(T t);
}
