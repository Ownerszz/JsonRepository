package ownerszz.libraries.json.repository.core.utils;

import java.util.ArrayList;
import java.util.List;

public class BooleanExpressionSolver {
    public static boolean evaluate(String expression, int index){
        char op = expression.charAt(index++);
        List<Boolean> bools = new ArrayList();

        while (index < expression.length()) {
            char c = expression.charAt(index++);

            if (c == 't' || c == 'f')
                bools.add(c == 't');
            else if (c == '|' || c == '&' || c == '!') {
                index--;
                bools.add(evaluate(expression,index));
                break;
            }
            else if (c == ')')
                break;
        }
        if (op == '!')
            return !bools.get(0);

        boolean result = op == 't';
        for (boolean bool : bools)
            result = (op == 't') ? (result || bool) : (result && bool);
        return result;
    }


}
