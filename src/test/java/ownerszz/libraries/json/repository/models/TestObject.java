package ownerszz.libraries.json.repository.models;

public class TestObject {
    private String name;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    private int age;
    public TestObject(){}
    public TestObject(String name, int age){
        this.name = name;
        this.age = age;
    }
}
