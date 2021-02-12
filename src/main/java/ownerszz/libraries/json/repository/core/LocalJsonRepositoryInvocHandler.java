package ownerszz.libraries.json.repository.core;

import ownerszz.libraries.json.repository.core.utils.ExpressionGenerator;
import ownerszz.libraries.json.repository.core.utils.QueryExtractor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class LocalJsonRepositoryInvocHandler<T> implements InvocationHandler {
    private LocalJsonRepository<T> repository;
    private Class<T> type;


    public LocalJsonRepositoryInvocHandler(Class<T> typeForRepo, String destination) throws Exception {
        this.type = typeForRepo;
        this.repository = new LocalJsonRepository(typeForRepo,destination);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean existingMethod = Arrays.stream(repository.getClass().getDeclaredMethods())
                .anyMatch(m ->
                        m.getReturnType().equals(method.getReturnType())
                        && Arrays.equals(m.getParameterTypes(), method.getParameterTypes())
                        && m.getName().equals(method.getName()));

        Object toReturn = null;
        if(existingMethod){
           toReturn = method.invoke(repository, args);
        }else {
            String query = QueryExtractor.extractQueryFromMethod(method);
            Predicate<T> predicate = ExpressionGenerator.expressionAsPredicate(type, query, args);
            if (Collection.class.isAssignableFrom(method.getReturnType())) {
                toReturn = repository.findAllBy(predicate);
            } else {
                toReturn = repository.findBy(predicate);
            }
        }
        return toReturn;
    }

}
