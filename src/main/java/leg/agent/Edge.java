package leg.agent;


import java.util.LinkedList;
import java.util.List;

public class Edge {
    public final String target;
    final Object historyLock;
    final PropertyChainBox properties;
    LinkedList<VisitEvent> historyPrefix;
    LinkedList<VisitEvent> historySuffix;
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
        public final Statistics statistics;
        public final VisitEvent historyPrefix[];
        public final VisitEvent historySuffix[];
        private Snapshot(Statistics st, List<VisitEvent> prefix, List<VisitEvent> suffix){
            this.statistics = new Statistics(st);
            this.historyPrefix = new VisitEvent[prefix.size()];
            this.historySuffix = new VisitEvent[suffix.size()];
            prefix.toArray (historyPrefix);
            suffix.toArray(historySuffix);
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
        public class Snapshot {
            long time;
            long step;
            byte[] data;
            long visit;
            public Snapshot(Snapshot other){
                this.time = other.time;
                this.step = other.step;
                this.data = (other.data == null) ? null : other.data.clone();
                this.visit = other.visit;
            }
            public Snapshot() {
                time = 0;
                step = 0;
                visit = 0;
                data = null;
            }
        }
        public Statistics.Snapshot first;
        public  Statistics.Snapshot last;
        public Statistics() {
            this.first = new Snapshot();
            this.last = new Snapshot();
        }
        public Statistics(final Statistics other) {
            this.first = new Snapshot(other.first);
            this.last = new Snapshot(other.last);
        }
    }


    public final class  VisitEvent{
        final long time;
        final long step;
        final long visit;
        final byte data[];

        public VisitEvent(long time, long step, long visit, byte data[]) {
            this.step = step;
            this.time = time;
            this.visit = visit;
            this.data = (data != null) ? data.clone(): null;
        }

        public VisitEvent(final VisitEvent other) {
            this.step = other.step;
            this.time = other.time;
            this.visit = other.visit;
            this.data = (other.data != null ) ? other.data.clone():null;
        }
    }


    public void visit(long threadStep, final byte data[])
    {
        synchronized (historyLock) {
            statistics.last.visit++;
            statistics.last.time = System.nanoTime();
            statistics.last.step =threadStep;
            statistics.last.data = (data != null) ? data.clone(): null;
            if(statistics.last.visit == 1){
                statistics.first.data = statistics.last.data;
                statistics.first.time = statistics.last.time;
                statistics.first.step = statistics.last.step;
                statistics.first.visit = statistics.last.visit;
            }
            int prefixLimit = properties.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit);
            int suffixLimit = properties.getInt(PropertyChainBox.Property.HistorySuffixLogLimit);
            if (historyPrefix.size() < prefixLimit) {
                historyPrefix.add(new VisitEvent(statistics.last.time, statistics.last.step,statistics.last.visit, data));
            } else {
                historySuffix.add(new VisitEvent(statistics.last.time, statistics.last.step, statistics.last.visit, statistics.last.data));
                if (historySuffix.size() > suffixLimit) {
                    historySuffix.removeFirst();
                }
            }
        }
    }

    public void resetHistory() {
        synchronized (historyLock) {
            this.historyPrefix = new LinkedList<>();
            this.historySuffix = new LinkedList<>();
        }
    }
    public void resetStatistics() {
            synchronized (historyLock) {
                statistics = new Statistics();
            }
    }
}

