package leg.agent;


import java.util.LinkedList;
import java.util.List;

public class Edge {
    public final String target;
    final Object historyLock;
    final PropertyChainBox properties;
    final LinkedList<VisitEvent> historyPrefix;
    final LinkedList<VisitEvent> historySuffix;
    Statistics statistics;
    int historyPrefixLimit;
    int historySuffixLimit;
    public Edge(String target, PropertyChainBox parentProperty) {
        historyLock = new Object();
        this.target = target;
        statistics = new Statistics();
        this.properties = new PropertyChainBox(parentProperty);
        historyPrefix = new LinkedList<>();
        historySuffix = new LinkedList<>();
        historyPrefixLimit = 0;
        historySuffixLimit = 0;
    }
    public final class Snapshot {
        private final Statistics statistics;
        private final VisitEvent historyPrefix[];
        private final VisitEvent historySuffix[];
        private Snapshot(Statistics st, List<VisitEvent> prefix, List<VisitEvent> suffix){
            this.statistics = new Statistics(st);
            this.historyPrefix = prefix.toArray(new VisitEvent[0]);
            this.historySuffix = suffix.toArray(new VisitEvent[0]);
        }
    }
    public final Statistics getStatistics() {
        synchronized (historyLock) {
            return new Statistics(this.statistics);
        }
    }

    public final Snapshot getSnapShot(){
        synchronized (historyLock) {
            return new Snapshot(statistics, historyPrefix, historySuffix);
        }
    }
    public class Statistics {
        public long first = 0;
        public long last = 0;
        public long visits = 0;
        public long lastThreadStep = 0;
        public long firstThreadStep = 0;
        public byte lastPropertyData[] = null;

        public Statistics() {
        }

        public Statistics(final Statistics other) {
            this.first = other.first;
            this.last = other.last;
            this.visits = other.visits;
            this.lastThreadStep = other.lastThreadStep;
            this.firstThreadStep = other.firstThreadStep;
            this.lastPropertyData = (other.lastPropertyData == null) ? null : other.lastPropertyData.clone();
        }
    }


    public final class  VisitEvent{
        final long time;
        final long threadStep;
        final byte propertyData[];

        public VisitEvent(long time, long threadStep, byte propertyData[]) {
            this.threadStep = threadStep;
            this.time = time;
            this.propertyData = propertyData;
        }

        public VisitEvent(final VisitEvent other) {
            this.threadStep = other.threadStep;
            this.time = other.time;
            this.propertyData = other.propertyData.clone();
        }
    }


    public void visit(long threadStep, final byte propertyData[])
    {
        synchronized (historyLock) {
            statistics.visits++;
            statistics.last = System.nanoTime();
            if(statistics.first == 0){
                statistics.first = statistics.last;
            }
            statistics.lastThreadStep = threadStep;
            if(statistics.firstThreadStep == 0){
                statistics.firstThreadStep = threadStep;
            }
            statistics.lastPropertyData = propertyData.clone();
            int prefixLimit = properties.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit);
            int suffixLimit = properties.getInt(PropertyChainBox.Property.HistorySuffixLogLimit);
            if (historyPrefix.size() < prefixLimit) {
                historyPrefix.add(new VisitEvent(statistics.last, statistics.lastThreadStep, propertyData));
            } else {
                historyPrefix.add(new VisitEvent(statistics.last, statistics.lastThreadStep, propertyData));
                if (historySuffix.size() > suffixLimit) {
                    historySuffix.removeFirst();
                }
            }
        }
    }
    public void resetHistoryAndStatistics() {
        synchronized (historyLock) {
            historyPrefix.clear();
            historySuffix.clear();
            this.statistics.lastPropertyData = null;
            this.statistics.last = 0;
            this.statistics.lastThreadStep=0;
            this.statistics.firstThreadStep=0;
            this.statistics.first = 0;
            this.statistics.last= 0;
        }

    }
    public void resetHistory() {
        synchronized (historyLock) {
            historyPrefix.clear();
            historySuffix.clear();
        }
    }
}

