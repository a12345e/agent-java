package leg.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import leg.agent.sample.basic.GenericSimpleThreadRun;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.Properties;

public class ThreadTest {


    @Test
    public void testMainThread(){
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HistoryPrefixLogLimit.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HistorySuffixLogLimit.name(),"3");
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
        prop.setProperty(PropertyChainBox.Property.HistoryPrefixLogLimit.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HistorySuffixLogLimit.name(),"3");
        StringBuilder buffer = new StringBuilder();
        LEgApi.init(prop,buffer);
        Thread t = new Thread(new GenericSimpleThreadRun(100,3));
        long id = t.getId();
        t.start();
        t.join();
        Actor actor = Actor.getMapThread2Actor().get(id);
        Assert.assertNotNull(actor);


        //actor.getMapKey2Node(Node.getKey("domainA","OperationD",)
        LEgApi.dispose(buffer);
    }

}
