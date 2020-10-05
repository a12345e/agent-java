package leg.agent;

import leg.common.Visit;

import java.util.LinkedList;

public class History implements Visit {
    
    private VisitEvent first;
    private VisitEvent last;
    private transient Object lock = new Object();
    private LinkedList<VisitEvent> historyPrefix = new LinkedList<>();
    private LinkedList<VisitEvent> historySuffix = new LinkedList<>();
    PropertyChainBox properties;

    public History(PropertyChainBox properties){
        this.properties = properties;
    }
    public void clear(){
        synchronized (lock) {
            historyPrefix = new LinkedList<>();
            historySuffix = new LinkedList<>();
        }
    }

    public VisitEvent getFirstEvent(){
        return first;
    }
    public VisitEvent getLastEvent(){
        return last;
    }
    @Override
    public void visit(long step, byte[] data) {
        synchronized (lock) {
            last = new VisitEvent( last,step,data);
            if(last.getVisit() == 0){
                first = last;


            }
            if (historyPrefix.size() < properties.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit)) {
                historyPrefix.add(new VisitEvent(last,step,data));
            } else {
                historySuffix.add(new VisitEvent(last,step, data));
                if (historySuffix.size() > properties.getInt(PropertyChainBox.Property.HistorySuffixLogLimit)) {
                    historySuffix.removeFirst();
                }
            }
        }

    }

    
}
