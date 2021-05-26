package leg.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import leg.agent.sample.basic.GenericSimpleThreadRun;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {


    @Test
    public void testMainThread(){
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HISTORY_SUFFIX_LOG_LIMIT.name(),"3");
        StringBuilder buffer = new StringBuilder();
        LEgApi.init(prop,buffer);
        LEgApi.mark("domainA","OperationA",new byte[0]);
        Actor actor = Actor.getMapThread2Actor().get(Thread.currentThread().getId());
        Assert.assertNotNull(actor);
        LEgApi.dispose(buffer);
    }

    @Test
    public void test1ThreadNotMain() throws InterruptedException {
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HISTORY_SUFFIX_LOG_LIMIT.name(),"3");
        StringBuilder buffer = new StringBuilder();
        LEgApi.init(prop,buffer);
        Thread t = new Thread(new GenericSimpleThreadRun(100,3,"Adomain"));
        long id = t.getId();
        t.start();
        t.join();
        Actor actor = Actor.getMapThread2Actor().get(id);
        Assert.assertNotNull(actor);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonOutput = gson.toJson(actor);
        System.out.print(jsonOutput);
        //actor.getMapKey2Node(Node.getKey("domainA","OperationD",)
        LEgApi.dispose(buffer);
    }

    @Test
    public void testMultiThreadNotMain() throws InterruptedException {
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HISTORY_SUFFIX_LOG_LIMIT.name(),"3");
        StringBuilder buffer = new StringBuilder();
        LEgApi.init(prop,buffer);
        int runs = 5;
        CountDownLatch latch = new CountDownLatch(runs);
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < runs; i++) {
            service.submit(() -> {
                   new GenericSimpleThreadRun(100,3,"Adomain").run();
                  latch.countDown();
                  System.out.println("Done actor ");
            });
        }

// wait for the latch to be decremented by the two remaining threads
        System.out.println("Number of actors created "+Actor.getMapThread2Actor().keySet().size());
        latch.await();
        System.out.println("Number of actors created "+Actor.getMapThread2Actor().keySet().size());
        LEgApi.dispose(buffer);
        System.out.println("Number of actors created after dispose "+Actor.getMapThread2Actor().keySet().size());
    }

}
