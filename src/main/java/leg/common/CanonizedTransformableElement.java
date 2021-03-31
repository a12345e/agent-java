package leg.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanonizedTransformableElement implements Transformable {
    Map<String, Transformable> transformablesMap = new HashMap<>();
    Map<String,String> stringsMap = new HashMap<>();
    Map<String,Boolean> booleansMap = new HashMap();
    Map<String,Long> longsMap = new HashMap<>();
    Map<String, List<String>> stringListsMap = new HashMap();
    Map<String, List<Transformable>> transformableLists = new HashMap<>();

    @Override
    public  Map<String, Transformable> getTransformableMap(){
        return transformablesMap;
    }

    @Override
    public  Map<String,String> getStringMap(){
        return stringsMap;
    }

    @Override
    public  Map<String,Boolean> getBooleanMap(){
        return booleansMap;
    }
    public  Map<String,Long> getLongMap(){
        return longsMap;
    }
    public  Map<String, List<String>> getStringListMap(){
        return stringListsMap;

    }
    @Override
    public Map<String, List<Transformable>> getTransformableListMap(){
        return transformableLists;
    }


    @Override
    public  void deserialize(Transformable element) {
            transformablesMap = element.getTransformableMap();
            stringsMap = element.getStringMap();
            booleansMap = element.getBooleanMap();
            longsMap = element.getLongMap();
            stringListsMap = element.getStringListMap();
            transformableLists = element.getTransformableListMap();
    }


}
