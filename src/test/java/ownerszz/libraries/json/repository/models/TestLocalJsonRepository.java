package ownerszz.libraries.json.repository.models;

import ownerszz.libraries.json.repository.core.JsonRepository;
import ownerszz.libraries.json.repository.core.LocalJsonRepository;

import java.util.List;
import java.util.Optional;

public interface TestLocalJsonRepository extends JsonRepository<TestObject> {
    Optional<TestObject> findByName(String name);
    Optional<TestObject> findByAge(int age);
    Optional<TestObject> findByNameAndAge(String name, int age);
    List<TestObject> findAllByAge(int age);
    List<TestObject> findAllByNameOrAge(String name, int age);
}
