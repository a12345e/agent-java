package leg.agent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SubmissionPublisher;

public class HistoryMonitor {
    private SubmissionPublisher publisher = new SubmissionPublisher<>();
    transient private Node rootNode;
    private long step;
    transient private Node lastNode;
    private Map<String, Node> mapKey2Node = new HashMap<String, Node>();
    private Map<String, History> mapEdge2History = new HashMap<String, History>();

    final long threadId;
    private static final int KEEP_FIRST_EVENTS_NUMBER = 5;
    private static final int KEEP_LAST_EVENTS_NUMBER = 10;

    private String edgeKey(Node src,Node target) {
        StringBuilder builder = new StringBuilder();
        builder.append(src.getKey()).append(':').append(target.getKey());
        return builder.toString();
    }


    public Map<String, Node> getMapKey2Node() {
        return Collections.unmodifiableMap(mapKey2Node);
    }
    public HistoryMonitor(long threadId){
        this.threadId = threadId;
        step = 0;
        rootNode = lastNode = new Node("", "", "", "", 0);

    }
    public void visit(String domain, String operation,byte[] data,EventComputedDetails event) {
        String target = Node.getKey(domain, event.className, event.method, event.lineNumber,operation);
        Node found = mapKey2Node.get(target);
        if (found == null) {
            found = new Node(domain, operation, event.className, event.method, event.lineNumber);
            mapKey2Node.put(target, found);
        }
        String edgeKey = edgeKey(lastNode,found);
        History edgeHistory = mapEdge2History.get(edgeKey);
        if(edgeHistory == null){
            edgeHistory = new History();
            mapEdge2History.put(edgeKey,edgeHistory);
        }
        edgeHistory.visit(step++,event.time,data,  KEEP_FIRST_EVENTS_NUMBER, KEEP_LAST_EVENTS_NUMBER);
        lastNode = found;


    }


}
