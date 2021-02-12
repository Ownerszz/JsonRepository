package ownerszz.libraries.json.repository.core;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public interface JsonRepository<T> {
    T save(T obj) throws Exception;

    Future<T> saveAsync(T obj) throws Exception;

    List<T> findAllBy(Predicate<T> filter);

    Future<List<T>> findAllByAsync(Predicate<T> filter);

    Optional<T> findBy(Predicate<T> filter);

    Future<Optional<T>> findByAsync(Predicate<T> filter);

    boolean remove(T obj) throws Exception;

    Future<Boolean> removeAsync(T obj);

    void saveAll() throws Exception;

    Future<Void> saveAllAsync();

    boolean removeAll(List<T> objects) throws Exception;

    Future<Boolean> removeAllAsync(List<T> objects);

    boolean removeAllBy(Predicate<T> filter) throws Exception;

    Future<Boolean> removeAllByAsync(Predicate<T> filter);
}
