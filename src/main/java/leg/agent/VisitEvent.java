package leg.agent;


public final class VisitEvent{
    private final long time;
    private final long step;
    private final byte data[];


    public long getStep() { return step;}
    public long getTime() { return time;}
    public byte[] getData() { return data;}

    VisitEvent(long step,long time,byte data[]) {
        this.step = step;
        this.time = time;
        if (data != null)
            this.data = data.clone();
        else this.data = null;
    }


}
