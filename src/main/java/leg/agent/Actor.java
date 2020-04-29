package leg.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Actor {
    private Node rootNode;
    private long threadStep;
    private Node lastNode;
    private Map<String,Node> mapKey2Node = new HashMap<String,Node>();
    private static Map<Long,Actor> mapThread2Actor = new ConcurrentHashMap<Long,Actor>();
    private PropertyChainBox propertyChainBox;
    private Actor(PropertyChainBox parentProperties){
        propertyChainBox = new PropertyChainBox(parentProperties);
        rootNode = lastNode = new Node("","","","",0,propertyChainBox);
        threadStep = 0;

    }
    static public Actor getCreateCurrentActor(PropertyChainBox parentProperties){
            long id = Thread.currentThread().getId();
            Actor actor = mapThread2Actor.get(id);
            if(actor  == null){
                actor = new Actor(parentProperties);
                mapThread2Actor.put(id,actor);
            }
            return actor;
    }
    void mark(String  domain, String operation, byte propertyData[],
              String classFullName, String method, int lineNumber){
            Node node = new Node(domain,operation,classFullName,method,lineNumber,propertyChainBox);
            String target = node.getKey();
            Node found =  mapKey2Node.get(target);
            if(found  == null){
                found = node;
                mapKey2Node.put(target,found);
            }
            lastNode.visit(target,threadStep++,propertyData);
            lastNode = found;
    }
}
