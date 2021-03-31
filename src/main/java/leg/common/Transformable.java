package leg.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Transformable {
    public default boolean equalTo(Transformable other){
        if(!getStringMap().equals(other.getStringMap())
                || !getBooleanMap().equals(other.getBooleanMap())
                || !getLongMap().equals(other.getLongMap())
                || !getStringListMap().equals(other.getStringListMap())
                || !getTransformableMap().keySet().equals(other.getTransformableMap().keySet())
                || !getTransformableListMap().keySet().equals(other.getTransformableListMap().keySet()))
            return false;

        for(String key: getTransformableMap().keySet())
             if(!getTransformableMap().get(key).equalTo(other.getTransformableMap().get(key)))
                 return false;
        for(String key: getTransformableListMap().keySet()) {
            if (getTransformableListMap().get(key).size() != other.getTransformableListMap().get(key).size())
                return false;
            for (int i = 0; i < getTransformableListMap().get(key).size(); i++) {
                if (!getTransformableListMap().get(key).get(i).equalTo(other.getTransformableListMap().get(key).get(i)))
                    return false;
            }
        }
        return true;

    }
    public default Map<String, Transformable> getTransformableMap(){
        return new HashMap<>();
    }
    public default Map<String,String> getStringMap(){
        return new HashMap<>();
    }
    public default Map<String,Boolean> getBooleanMap(){
        return new HashMap<>();
    }
    public default Map<String,Long> getLongMap(){
        return new HashMap<>();
    }
    public default Map<String, List<String>> getStringListMap(){
        return new HashMap<>();
    }
    public default Map<String, List<Transformable>> getTransformableListMap(){
        return new HashMap<>();
    }
    public void deserialize(Transformable element);

}
