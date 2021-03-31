package leg.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class JsonTransfomer implements Transformer<ObjectNode> {


    public static final String STRINGS = "strings";
    public static final String BOOLEANS = "booleans";
    public static final String LONGS = "longs";
    public static final String TRANSFORMABLES = "transformables";
    public static final String STRINGLISTS = "stringlists";
    public static final String TRANSFORMABLELISTS = "transformablelists";

    @Override
    public Transformable deserialize(ObjectNode jsonObject) {
        CanonizedTransformableElement e = new CanonizedTransformableElement();
        deserializeStrings(jsonObject,e);
        deserializeStringLists(jsonObject,e);
        deserializeBooleans(jsonObject,e);
        deserializeLongs(jsonObject,e);
        deserializeTransformables(jsonObject,e);
        deserializeTransformableLists(jsonObject,e);
        return e;
    }





    @Override
    public ObjectNode serialize(Transformable e) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode o = mapper.createObjectNode();
        return serialize(e,o);

    }

    private void deserializeStrings(ObjectNode o, Transformable e){
        ObjectNode node = (ObjectNode) o.get(STRINGS);
        Iterator<String> fieldNames = node.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode value = node.get(fieldName);
            if(value.isTextual()){
                e.getStringMap().put(fieldName, value.asText());
            }else {
                throw new RuntimeException("Was expected string type but "+value.getNodeType());
            }
        }
    }
    private void deserializeBooleans(ObjectNode o,Transformable e){
        ObjectNode node = (ObjectNode) o.get(BOOLEANS);
        Iterator<String> fieldNames = node.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode value = node.get(fieldName);
            if(value.isBoolean()){
                e.getBooleanMap().put(fieldName, value.asBoolean());
            }else {
                throw new RuntimeException("Was expected boolean type but "+value.getNodeType());
            }
        }
    }
    private void deserializeLongs(ObjectNode o,Transformable e){
        ObjectNode node = (ObjectNode) o.get(LONGS);
        Iterator<String> fieldNames = node.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode value = node.get(fieldName);
            if(value.isIntegralNumber()){
                e.getLongMap().put(fieldName, value.asLong());
            }else {
                throw new RuntimeException("Was expected long type but "+value.getNodeType());
            }
        }
    }
    private void deserializeTransformables(ObjectNode o, Transformable e){
        ObjectNode node = (ObjectNode) o.get(TRANSFORMABLES);
        Iterator<String> fieldNames = node.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode value = node.get(fieldName);
            if(value.isObject()){
                e.getTransformableMap().put(fieldName,deserialize((ObjectNode)value));
            }else {
                throw new RuntimeException("Was expected Object type but "+value.getNodeType());
            }
        }
    }
    private void deserializeStringLists(ObjectNode o, Transformable e){
        ObjectNode node = (ObjectNode) o.get(STRINGLISTS);
        Iterator<String> fieldNames = node.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode value = node.get(fieldName);
            if(value.isArray()) {
                ArrayNode array = (ArrayNode) value;
                Optional<JsonNodeType>  type = getArrayType(array);
                if(type.isPresent()) {
                    if (type.get().equals(JsonNodeType.STRING)) {
                        e.getStringListMap().put(fieldName, get(array, x -> x.asText()));
                    }
                }else {
                    throw new RuntimeException("Was expected array of strings");
                }
            }else {
                throw new RuntimeException("Was expected Object type but "+value.getNodeType());
            }
        }
    }
    private void deserializeTransformableLists(ObjectNode o, Transformable e){
        ObjectNode node = (ObjectNode) o.get(TRANSFORMABLELISTS);
        Iterator<String> fieldNames = node.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode value = node.get(fieldName);
            if(value.isArray()) {
                ArrayNode array = (ArrayNode) value;
                Optional<JsonNodeType>  type = getArrayType(array);
                if(type.isPresent()) {
                    if (type.get().equals(JsonNodeType.OBJECT)) {
                        e.getTransformableListMap().put(fieldName, get(array, x -> deserialize((ObjectNode)x)));
                    }
                }else {
                    throw new RuntimeException("Was expected array of transformables");
                }
            }else {
                throw new RuntimeException("Was expected Object type but "+value.getNodeType());
            }
        }
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


    private <E> List<E>  get(ArrayNode array, Function<JsonNode,E> func) {
        List<E> set = new LinkedList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonNode node = array.get(i);
            set.add(func.apply(node));
        }
        return set;
    }
    private ObjectNode serialize(Transformable e, ObjectNode o) {
        ObjectNode booleans = o.putObject(BOOLEANS);
        e.getBooleanMap().entrySet().stream().forEach(entry -> booleans.put(entry.getKey(),entry.getValue()));
        ObjectNode strings = o.putObject(STRINGS);
        e.getStringMap().entrySet().stream().forEach(entry -> strings.put(entry.getKey(),entry.getValue()));
        ObjectNode longs = o.putObject(LONGS);
        e.getLongMap().entrySet().stream().forEach(entry -> longs.put(entry.getKey(),entry.getValue()));
        ObjectNode transformables = o.putObject(TRANSFORMABLES);
        e.getTransformableMap().entrySet().stream().forEach(entry -> {
            serialize(entry.getValue(),transformables.putObject(entry.getKey()));
        });
        ObjectNode stringlists = o.putObject(STRINGLISTS);
        e.getStringListMap().entrySet().stream().forEach(entry ->
        {
            ArrayNode arrayNode = stringlists.putArray(entry.getKey());
            for(String s:entry.getValue()){
                arrayNode.add(s);
            }
        });
        ObjectNode transformablelists = o.putObject(TRANSFORMABLELISTS);
        e.getTransformableListMap().entrySet().stream().forEach(entry ->
        {
            ArrayNode arrayNode = transformablelists.putArray(entry.getKey());
            for(Transformable t:entry.getValue()){
                arrayNode.add(serialize(t));
            }
        });
        return o;
    }
}
