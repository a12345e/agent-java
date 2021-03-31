package leg.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class JsonTransfomer implements Transformer<ObjectNode>{



    @Override
    public Transformable deserialize(ObjectNode jsonObject) {
            CanonizedTransformableElement e = new CanonizedTransformableElement();
            Iterator<String> fieldNames = jsonObject.fieldNames();
            Map<String,String> mapKeyUsage = new HashMap<>();
            while(fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode value = jsonObject.get(fieldName);
                if (value.isTextual()) {
                    e.stringsMap.put(fieldName, value.asText());
                } else if (value.isLong()) {
                    e.longsMap.put(fieldName, value.asLong());
                } else if (value.isBoolean()) {
                    e.booleansMap.put(fieldName, value.asBoolean());
                }else if(value.isObject()){
                    e.transformablesMap.put(fieldName,deserialize((ObjectNode)value));
                }else if(value.isArray()) {
                    ArrayNode array = (ArrayNode) value;
                    Optional<JsonNodeType>  type = getArrayType(array);
                    if(type.isPresent()) {
                         if(type.get().equals(JsonNodeType.STRING)){
                             e.getStringListMap().put(fieldName, jsonArrayToList(array, x -> x.asText()));
                         }else if(type.get().equals(JsonNodeType.OBJECT)){
                             e.getTransformableListMap().put(fieldName, jsonArrayToList(array, x -> deserialize((ObjectNode)x)));
                         }
                    }
                }
            }
            return e;

    }





    @Override
    public ObjectNode serialize(Transformable e) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode o = mapper.createObjectNode();
        return serialize(e,o);

    }

    private Optional<JsonNodeType> getArrayType(ArrayNode array) {

        Set<JsonNodeType> types = new HashSet<>();

        for(int i = 0; i < array.size(); i++) {
            JsonNode node = array.get(i);
            types.add(node.getNodeType());
        }
        if(types.size() > 1){
            throw new RuntimeException("More than one type in json array: " +types.stream().map(x-> x.toString()).collect(Collectors.joining(",")));
        }
        return types.stream().findFirst();
    }


    private <E> List<E> jsonArrayToList(ArrayNode array, Function<JsonNode,E> func) {
        List<E> list = new LinkedList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonNode node = array.get(i);
            list.add(func.apply(node));
        }
        return list;
    }
    private void addNewKeysAndCheckUniq(Set<String> set, Set<String> newSet, final String className){
        for(String key: newSet){
            if(!set.add(key)){
                throw new RuntimeException("Key "+key+" is used for more than one member of the transformable "+className);
            }
        }
    }

    private void validateKeyUniqeness(Transformable e){
        Set<String> set = new HashSet<>();
        String className = e.getClass().getName();
        addNewKeysAndCheckUniq(set,e.getBooleanMap().keySet(),className);
        addNewKeysAndCheckUniq(set,e.getStringMap().keySet(),className);
        addNewKeysAndCheckUniq(set,e.getLongMap().keySet(),className);
        addNewKeysAndCheckUniq(set,e.getTransformableMap().keySet(),className);
        addNewKeysAndCheckUniq(set,e.getStringListMap().keySet(),className);
        addNewKeysAndCheckUniq(set,e.getTransformableListMap().keySet(),className);

    }

    private ObjectNode serialize(Transformable e, ObjectNode o) {
        validateKeyUniqeness(e);
        e.getBooleanMap().entrySet().stream().forEach(entry -> o.put(entry.getKey(),entry.getValue()));
        e.getStringMap().entrySet().stream().forEach(entry -> o.put(entry.getKey(),entry.getValue()));
        e.getLongMap().entrySet().stream().forEach(entry -> o.put(entry.getKey(),entry.getValue()));
        e.getTransformableMap().entrySet().stream().forEach(entry -> {
            serialize(entry.getValue(),o.putObject(entry.getKey()));
        });
        e.getStringListMap().entrySet().stream().forEach(entry ->
        {
            ArrayNode arrayNode = o.putArray(entry.getKey());
            for(String s:entry.getValue()){
                arrayNode.add(s);
            }
        });
        e.getTransformableListMap().entrySet().stream().forEach(entry ->
        {
            ArrayNode arrayNode = o.putArray(entry.getKey());
            for(Transformable t:entry.getValue()){
                arrayNode.add(serialize(t));
            }
        });
        return o;
    }
}
