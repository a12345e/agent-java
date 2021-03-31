package leg.agent;

import leg.common.Visit;

import java.util.LinkedList;

public class History implements Visit {
    
    transient private VisitEvent first;
    transient private VisitEvent last;
    private transient Object lock = new Object();
    private LinkedList<VisitEvent> firstEvents = new LinkedList<>();
    private LinkedList<VisitEvent> lastEvents = new LinkedList<>();
    transient PropertyChainBox properties;

    public History(PropertyChainBox properties){
        this.properties = properties;
    }
    public void clear(){
        synchronized (lock) {
            firstEvents = new LinkedList<>();
            lastEvents = new LinkedList<>();
        }
    }

    public VisitEvent getFirstEvent(){
        return first;
    }
    public VisitEvent getLastEvent(){
        return last;
    }
    @Override
    public void visit(long step, long time,byte[] data) {
        synchronized (lock) {
            last = new VisitEvent( last,step,time,data);
            if(last.getVisit() == 0){
                first = last;
            }
            if (firstEvents.size() < properties.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit)) {
                firstEvents.add(last);
            }
            lastEvents.add(last);
            if (lastEvents.size() > properties.getInt(PropertyChainBox.Property.HistorySuffixLogLimit)) {
                  lastEvents.removeFirst();
            }
        }

    }



}
