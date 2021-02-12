package ownerszz.libraries.json.repository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ownerszz.libraries.json.repository.core.JsonRepositoryFactory;
import ownerszz.libraries.json.repository.models.TestLocalJsonRepository;
import ownerszz.libraries.json.repository.models.TestObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class JsonRepositoryTests {
    private TestLocalJsonRepository localJsonRepository;
    private List<TestObject> testObjects;

    @Before
    public void setup() throws Exception {
        testObjects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testObjects.add(new TestObject("test" + i, i %2));
        }
        localJsonRepository = JsonRepositoryFactory.createProxyFor(TestLocalJsonRepository.class,"src/test/java/ownerszz/libraries/json/repository/test/files/testobjects.json");
    }
    @After
    public void tearDown() throws Exception{
        localJsonRepository.removeAllBy(null);
    }
    @Test
    public void testInvokeExistingMethods() throws Exception {
        for (TestObject testObject:testObjects) {
            localJsonRepository.save(testObject);
        }
        List<TestObject> fromRepo = new ArrayList<>(localJsonRepository.findAllBy(null));
        Assert.assertArrayEquals(testObjects.toArray(), fromRepo.toArray());
    }

    @Test
    public void testInvokeFindByName() throws Exception{
        for (TestObject testObject:testObjects) {
            localJsonRepository.save(testObject);
        }
       TestObject testObject = localJsonRepository.findByName("test0").orElseThrow();
    }
    @Test(expected = Exception.class)
    public void testInvokeFindByAge() throws Exception{
        for (TestObject testObject:testObjects) {
            localJsonRepository.save(testObject);
        }
        TestObject testObject = localJsonRepository.findByAge(0).orElseThrow();
    }
    @Test
    public void testInvokeFindByNameAndAge() throws Exception{
        for (TestObject testObject:testObjects) {
            localJsonRepository.save(testObject);
        }
        TestObject testObject = localJsonRepository.findByNameAndAge("test0",0).orElseThrow();
    }
    @Test
    public void testInvokeFindAllByAge() throws Exception{
        for (TestObject testObject:testObjects) {
            localJsonRepository.save(testObject);
        }
        List<TestObject> testObject = localJsonRepository.findAllByAge(0);
        Assert.assertTrue(testObject.stream().allMatch(e-> e.getAge() == 0));
    }
    @Test
    public void testInvokeFindAllByNameOrAge() throws Exception{
        for (TestObject testObject:testObjects) {
            localJsonRepository.save(testObject);
        }
        List<TestObject> testObject = localJsonRepository.findAllByNameOrAge("test1",0);
        Assert.assertTrue(testObject.stream().allMatch(e-> e.getAge() == 0 || e.getName().equals("test1")));
    }

}
