package ownerszz.libraries.json.repository.core.utils;

import ownerszz.libraries.json.repository.core.utils.Operators;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExpressionGenerator {
    public static <T> Predicate<T> expressionAsPredicate(Class<T> target, String query, Object[] methodArgs){
        return new Predicate<T>() {
            @Override
            public boolean test(T o) {
                String expressionAsString = "";

                try {
                    String[] fieldNames = query.split(Operators.getSpliterator());
                    List<Field> fields = getFields(target,fieldNames);
                    List<String> operators = getOperatorsFromQuery(query);
                    for (int i = 0; i < methodArgs.length; i++) {
                        try {
                            if(operators.get(i).equals("IsNull")){
                                if(fields.get(i).get(o) == null){
                                    expressionAsString += "t";
                                }else {
                                    expressionAsString += "f";
                                }
                            }else if (operators.get(i).equals("IsNotNull")){
                                if(fields.get(i).get(o) != null){
                                    expressionAsString += "t";
                                }else {
                                    expressionAsString += "f";
                                }
                            }else {
                                Field field = fields.get(i);
                                field.setAccessible(true);
                                if(field.get(o).equals(methodArgs[i])){
                                    expressionAsString += "t";
                                }else {
                                    expressionAsString += "f";
                                }
                                field.setAccessible(false);
                            }
                        }catch (Exception e){
                            Field field = fields.get(i);
                            field.setAccessible(true);
                            if(field.get(o).equals(methodArgs[i])){
                                expressionAsString += "t";
                            }else {
                                expressionAsString += "f";
                            }
                            field.setAccessible(false);
                        }

                        try{
                            if (operators.get(i).equals("And")){
                                expressionAsString += "&";
                            }else if(operators.get(i).equals("Or")){
                                expressionAsString += "|";
                            }

                        }catch (Exception e){

                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
                //ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

                try {
                    //boolean result = Boolean.parseBoolean(scriptEngine.eval(expressionAsString).toString());
                    //return result;
                    return BooleanExpressionSolver.evaluate(expressionAsString,0);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    private static List<Field> getFields(Class<?> type,String[] fieldNames) throws NoSuchFieldException {
        List<Field> fields = new ArrayList<>();
        for (String name:fieldNames) {
            String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
            fields.add(type.getDeclaredField(fieldName));
        }
        return fields;
    }
    private static List<String> getOperatorsFromQuery(String query){
        return Operators.getValues().stream().filter(query::contains).collect(Collectors.toList());
    }
}
