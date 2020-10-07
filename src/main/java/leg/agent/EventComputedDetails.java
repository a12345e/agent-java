package leg.agent;

public final class EventComputedDetails {
    public final long time;
    public final int  lineNumber;
    public final String className;
    public final String method ;

    public EventComputedDetails(final int lineNumber,final String className, final String method,final long time){
        this.time = time;
        this.lineNumber = lineNumber;
        this.className = className;
        this.method = method;
    }
}
