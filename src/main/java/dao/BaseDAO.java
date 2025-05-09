package dao;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> {
    Optional<T> get(String id);
    List<T> getAll();
    boolean save(T t);
    boolean update(T t);
    boolean delete(String id);
}