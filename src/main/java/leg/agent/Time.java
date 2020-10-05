package leg.agent;

public class Time {
    static boolean testMode = false;
    static void setTestMode(boolean value){
        testMode = true;
    }
    static long get() {
        if(testMode)
            return 0L;
        else
            return System.nanoTime();
    }
}
