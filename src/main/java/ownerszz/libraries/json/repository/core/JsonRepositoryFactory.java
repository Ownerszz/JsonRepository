package ownerszz.libraries.json.repository.core;

import ownerszz.libraries.json.repository.core.implementations.LocalJsonRepositoryInvocHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class JsonRepositoryFactory {
    public static <T extends JsonRepository<?>> T createLocalJsonRepository(Class<T> interfaze, String target) throws Exception {
        if (JsonRepository.class.isAssignableFrom(interfaze) && interfaze.isInterface()){
            Class<?> typeParam = (Class<?>) findJsonRepository(interfaze).getActualTypeArguments()[0];
            LocalJsonRepositoryInvocHandler invocHandler = new LocalJsonRepositoryInvocHandler(typeParam, target);
            return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{interfaze}, invocHandler);
        }
        throw new Exception("Interface must extend LocalJsonRepository<T>");
    }
    private static ParameterizedType findJsonRepository(Class<?> interfaze){
        return (ParameterizedType) Arrays.stream(interfaze.getGenericInterfaces())
                .filter(e-> JsonRepository.class.isAssignableFrom(interfaze))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Could not find LocalJsonRepository in interface: " + interfaze.getName()));
    }
}
