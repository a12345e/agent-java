package leg.agent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Actor {
    private Node rootNode;
    private long threadStep;
    private Node lastNode;
    private Map<String,Node> mapKey2Node = new HashMap<String,Node>();
    private static Map<Long,Actor> mapThread2Actor = new ConcurrentHashMap<Long,Actor>();
    public Map<String, Node> getMapKey2Node(){
        return  Collections.unmodifiableMap(mapKey2Node);
    }
    static final public Map<Long, Actor> getMapThread2Actor(){
        return  Collections.unmodifiableMap(mapThread2Actor);
    }
    private transient PropertyChainBox propertyChainBox;
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
                // need enhancement to find when thread dies and not keep to much data of old threads
                // We can limit the number of active actors and remove olds ones by LRU
                // Can put thread local storage for making id unique against reuse of thread ids
                // dso we know that a thread is actually new if has not our thread local data
                mapThread2Actor.put(id,actor);
            }
            return actor;
    }

    public void mark(String  domain, String event, byte data[],
              EventComputedDetails eventComputedDetails){
            String target = Node.getKey(domain,event,eventComputedDetails.className,eventComputedDetails.method,eventComputedDetails.lineNumber);
            Node found =  mapKey2Node.get(target);
            if(found  == null){
                found =  new Node(domain,event,eventComputedDetails.className,eventComputedDetails.method,eventComputedDetails.lineNumber,propertyChainBox);
                mapKey2Node.put(target,found);
            }
            lastNode.visit(target,threadStep++,eventComputedDetails.time,data);
            lastNode = found;
    }
}
