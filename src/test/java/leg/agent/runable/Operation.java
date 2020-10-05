package leg.agent.runable;

public class Operation{
    public final String name;
    public final byte data[];
    public final int executionTimeSleep;

    Operation(String name, byte data[], int sleepMillis) {
        this.name = name;
        this.data = data;
        this.executionTimeSleep = sleepMillis;
    }
}
