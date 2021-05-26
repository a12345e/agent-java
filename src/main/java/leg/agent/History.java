package leg.agent;

import java.util.LinkedList;

public class History {
    
    private transient Object lock = new Object();
    private LinkedList<VisitEvent> firstEvents = new LinkedList<>();
    private LinkedList<VisitEvent> lastEvents = new LinkedList<>();
    private long countVisits;
    public History(){

    }
    public void clear(){
        synchronized (lock) {
            firstEvents = new LinkedList<>();
            lastEvents = new LinkedList<>();
        }
    }

    public void visit(long step, long time,byte[] data, int keepFirstEvents, int keepLastEvents) {
        synchronized (lock) {
            VisitEvent event = new VisitEvent( step,time,data);
            if (getFirstEvents().size() < keepFirstEvents){
                getFirstEvents().add(event);
            }
            getLastEvents().add(event);
            if (getLastEvents().size() > keepLastEvents) {
                  getLastEvents().removeFirst();
            }
            countVisits = getCountVisits() + 1;
        }

    }


    public LinkedList<VisitEvent> getFirstEvents() {
        return firstEvents;
    }

    public LinkedList<VisitEvent> getLastEvents() {
        return lastEvents;
    }

    public long getCountVisits() {
        return countVisits;
    }
}
