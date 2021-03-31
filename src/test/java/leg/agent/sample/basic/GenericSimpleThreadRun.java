package leg.agent.sample.basic;

import leg.agent.LEgApi;

public class GenericSimpleThreadRun implements Runnable{

    int loops;
    int waitMilli;
    final String domain;
    byte emptyArray[] = new byte[0];
    public GenericSimpleThreadRun(int waitMillis,int mainloops, String domain){
        this.loops = mainloops;
        this.waitMilli = waitMillis;
        this.domain = domain;
    }
    private void D() throws InterruptedException {
        LEgApi.mark(domain,"D1", emptyArray);
        Thread.sleep(waitMilli);
        LEgApi.mark(domain,"D2", emptyArray);
        Thread.sleep(waitMilli);
        LEgApi.mark(domain,"D3", emptyArray);
        Thread.sleep(waitMilli);

    }
    private void C() throws InterruptedException {
        LEgApi.mark(domain,"C1", emptyArray);
        Thread.sleep(waitMilli);
        D();
        LEgApi.mark(domain,"C2", emptyArray);
        Thread.sleep(waitMilli);
        LEgApi.mark(domain,"C3", emptyArray);
        Thread.sleep(waitMilli);

    }
    private void B() throws InterruptedException {
        LEgApi.mark(domain,domain, emptyArray);
        Thread.sleep(waitMilli);
        LEgApi.mark(domain,"B2", emptyArray);
        for(int i = 0; i <3; i++)
            D();
        Thread.sleep(waitMilli);
        LEgApi.mark(domain,"B3", emptyArray);
        Thread.sleep(waitMilli);

    }
    private void A() throws InterruptedException {
        LEgApi.mark(domain,"A1", emptyArray);
        Thread.sleep(100);
        LEgApi.mark(domain,"A2", emptyArray);
        Thread.sleep(waitMilli);
        for(int i=0; i < 3; i++){
            B();
        }
        C();
        LEgApi.mark(domain,"A3" , emptyArray);
        Thread.sleep(waitMilli);
    }


    @Override
    public void run() {
        try {
            for(int i=0 ; i < loops; i++){
                A();
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
