package ownerszz.libraries.json.repository.core.implementations;

import ownerszz.libraries.json.repository.core.utils.ExpressionGenerator;
import ownerszz.libraries.json.repository.core.utils.QueryExtractor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Future;
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
            Class returnType = method.getReturnType();
            if(Future.class.isAssignableFrom(returnType)){
                Type futureType = method.getGenericReturnType();
                Type[] typeParams = ((ParameterizedType) futureType).getActualTypeArguments();
                Type typeArgument = ((ParameterizedType) typeParams[0]).getRawType();
                Class classOfTypeArgument = (Class) typeArgument;
                if (Collection.class.isAssignableFrom(classOfTypeArgument)){
                    toReturn = repository.findAllByAsync(predicate);
                }else {
                    toReturn = repository.findByAsync(predicate);
                }
            }else {
                if (Collection.class.isAssignableFrom(method.getReturnType())) {
                    toReturn = repository.findAllBy(predicate);
                } else {
                    toReturn = repository.findBy(predicate);
                    if (Boolean.class.isAssignableFrom(returnType)){
                     toReturn = ((Optional<?>) toReturn).isPresent();
                    }
                }
            }
        }
        return toReturn;
    }


}
