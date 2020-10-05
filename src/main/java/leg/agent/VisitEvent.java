package leg.agent;


public final class VisitEvent {
    private final long time;
    private final long step;
    private final long visit;
    private final byte data[];


    public long getVisit(){
        return visit;
    }
    public long getStep() { return step;}
    public long getTime() { return time;}
    public byte[] getData() { return data;}

    VisitEvent(final VisitEvent from,long step,byte data[]) {
        this.step = step;
        this.time = Time.get();
        if(from != null){
            this.visit = from.visit+1;
        }else {
            visit = 0;
        }
        if (data != null)
            this.data = data.clone();
        else this.data = null;
    }

}
