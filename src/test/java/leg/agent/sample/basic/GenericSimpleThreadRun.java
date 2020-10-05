package leg.agent.sample.basic;

import leg.agent.LEgApi;

public class GenericSimpleThreadRun implements Runnable{

    int loops;
    int waitMilli;
    byte emptyArray[] = new byte[0];
    public GenericSimpleThreadRun(int waitMillis,int loops){
        this.loops = loops;
        this.waitMilli = waitMillis;
    }
    private void secondStep() throws InterruptedException {
        LEgApi.mark("domainA","OperationD", emptyArray);

    }
    private void firstStep() throws InterruptedException {
        LEgApi.mark("domainA","OperationA", emptyArray);
        Thread.sleep(100);
        LEgApi.mark("domainA","OperationB", emptyArray);
        Thread.sleep(waitMilli);
        for(int i=0; i < 10; i++){
            secondStep();
        }
        LEgApi.mark("domainA","OperationC" , emptyArray);
        Thread.sleep(waitMilli);
    }


    @Override
    public void run() {
        try {
            for(int i=0 ; i < loops; i++){
                firstStep();
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
