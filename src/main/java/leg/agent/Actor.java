package leg.agent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SubmissionPublisher;

public class Actor  {
    private SubmissionPublisher publisher = new SubmissionPublisher<>();
    private static final int KEEP_FIRST_EVENTS_NUMBER = 5;
    private static final int KEEP_LAST_EVENTS_NUMBER = 10;
    transient private Node rootNode;
    private long threadStep;
    transient private Node lastNode;
    transient private HistoryMonitor monitor;
    private Map<String, Node> mapKey2Node = new HashMap<String, Node>();
    private Map<String, History> mapEdge2History = new HashMap<String, History>();


    private String edgeKey(Node src,Node target){
        StringBuilder builder = new StringBuilder();
        builder.append(src.getKey()).append(':').append(target.getKey());
        return builder.toString();
    }
    public static void reset(){
        mapThread2Actor.clear();
    }
    private static Map<Long, Actor> mapThread2Actor = new ConcurrentHashMap<Long, Actor>();

    public Map<String, Node> getMapKey2Node() {
        return Collections.unmodifiableMap(mapKey2Node);
    }
    public static final Map<Long, Actor> getMapThread2Actor() {
        return Collections.unmodifiableMap(mapThread2Actor);
    }


    public Actor() {
        rootNode = lastNode = new Node("", "", "", "", 0);
        threadStep = 0;
        HistoryMonitor monitor = new HistoryMonitor();
    }

    static public Actor getCreateCurrentActor() {
        long id = Thread.currentThread().getId();
        Actor actor = mapThread2Actor.get(id);
        if (actor == null) {
            actor = new Actor();
            // need enhancement to find when thread dies and not keep to much data of old threads
            // We can limit the number of active actors and remove olds ones by LRU
            // Can put thread local storage for making id unique against reuse of thread ids
            // dso we know that a thread is actually new if has not our thread local data
            mapThread2Actor.put(id, actor);
        }
        return actor;
    }

    public void mark(String domain, String operation, byte data[],
                     EventComputedDetails eventComputedDetails) {
        String target = Node.getKey(domain, eventComputedDetails.className, eventComputedDetails.method, eventComputedDetails.lineNumber,operation);
        Node found = mapKey2Node.get(target);
        if (found == null) {
            found = new Node(domain, operation, eventComputedDetails.className, eventComputedDetails.method, eventComputedDetails.lineNumber);
            mapKey2Node.put(target, found);
        }
        String edgeKey = edgeKey(lastNode,found);
        History edgeHistory = mapEdge2History.get(edgeKey);
        if(edgeHistory == null){
            edgeHistory = new History();
            mapEdge2History.put(edgeKey,edgeHistory);
        }
        edgeHistory.visit(threadStep++,eventComputedDetails.time,data, KEEP_FIRST_EVENTS_NUMBER, KEEP_LAST_EVENTS_NUMBER);
        lastNode = found;

    }

}
