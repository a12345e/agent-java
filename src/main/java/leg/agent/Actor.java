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
    private static Map<Long, Actor> mapThread2Actor = new ConcurrentHashMap<Long, Actor>();
    private HistoryMonitor monitor;
    public static void reset(){
        mapThread2Actor.clear();
    }

    public static final Map<Long, Actor> getMapThread2Actor() {
        return Collections.unmodifiableMap(mapThread2Actor);
    }


    public Actor(long threadId) {
        HistoryMonitor monitor = new HistoryMonitor(threadId);
    }

    static public Actor getCreateCurrentActor() {
        long id = Thread.currentThread().getId();
        Actor actor = mapThread2Actor.get(id);
        if (actor == null) {
            actor = new Actor(id);
            // need enhancement to find when thread dies and not keep to much data of old threads
            // We can limit the number of active actors and remove olds ones by LRU
            // Can put thread local storage for making id unique against reuse of thread ids
            // dso we know that a thread is actually new if has not our thread local data
            mapThread2Actor.put(id, actor);
        }
        return actor;
    }

    public void mark(String domain, String operation, byte data[],
                     EventComputedDetails event) {
        monitor.visit(domain,operation,data,event);
    }

}
