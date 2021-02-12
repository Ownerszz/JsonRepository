package ownerszz.libraries.json.repository.core.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Operators {
    private static final List<String> values = Arrays.asList("And", "Or", "IsNull","IsNotNull");
    public static List<String> getValues(){
        return Collections.unmodifiableList(values);
    }
    public static String getSpliterator(){
        return String.join("|",values);
    }
}
