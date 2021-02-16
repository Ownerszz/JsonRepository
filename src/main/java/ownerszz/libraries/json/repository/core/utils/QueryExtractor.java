package ownerszz.libraries.json.repository.core.utils;

import java.lang.reflect.Method;

public class QueryExtractor {
    public static String extractQueryFromMethod(Method method){
       return method.getName()
               .replaceFirst("findBy", "")
               .replaceFirst("findAllBy", "")
               .replaceAll("Async$","");
    }

}
