package leg.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ActorTest {

    @Test
    public void Test(){
        PropertyChainBox  pcb =new PropertyChainBox(null);
        Actor actor = Actor.getCreateCurrentActor(pcb);
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 2);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2);

        Map<String,Node> mapKey2Node = actor.getMapKey2Node();
        Assert.assertEquals(0,mapKey2Node.size());

        for(int i=0; i < 10; i++ ) {
            actor.mark("domain1", "operation1",null,new EventComputedDetails(1,"com.software.BestClass1","method1",10));
            actor.mark("domain1", "operation2", null, new EventComputedDetails(2,"com.software.BestClass1","method2",11));
            actor.mark("domain1", "operation3", null, new EventComputedDetails(3,"com.software.BestClass1","method3",12));
            actor.mark("domain1", "operation4", null, new EventComputedDetails(4,"com.software.BestClass1","method4",13));

            actor.mark("domain2", "operation1",null,new EventComputedDetails(1,"com.software.BestClass2","method1",10));
            actor.mark("domain2", "operation2", null, new EventComputedDetails(2,"com.software.BestClass2","method2",11));
            actor.mark("domain2", "operation3", null, new EventComputedDetails(3,"com.software.BestClass2","method3",12));
            actor.mark("domain2", "operation4", null, new EventComputedDetails(4,"com.software.BestClass2","method4",13));
        }
        Assert.assertEquals(8,mapKey2Node.size());
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1)));
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain1", "operation2", "com.software.BestClass1", "method2", 2)));
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain1", "operation3", "com.software.BestClass1", "method3", 3)));
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain1", "operation4", "com.software.BestClass1", "method4", 4)));

        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain2", "operation1", "com.software.BestClass2", "method1", 1)));
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain2", "operation2", "com.software.BestClass2", "method2", 2)));
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain2", "operation3", "com.software.BestClass2", "method3", 3)));
        Assert.assertTrue(mapKey2Node.keySet().contains(Node.getKey("domain2", "operation4", "com.software.BestClass2", "method4", 4)));

        Assert.assertEquals(1,mapKey2Node.get(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1)).maptTarget2Edge.size());
        Assert.assertEquals(1,mapKey2Node.get(Node.getKey("domain1", "operation2", "com.software.BestClass1", "method2", 2)).maptTarget2Edge.size());
        Assert.assertEquals(1,mapKey2Node.get(Node.getKey("domain1", "operation3", "com.software.BestClass1", "method3", 3)).maptTarget2Edge.size());
        Assert.assertEquals(1,mapKey2Node.get(Node.getKey("domain1", "operation4", "com.software.BestClass1", "method4", 4)).maptTarget2Edge.size());

        mapKey2Node.get(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1)).maptTarget2Edge.values().iterator().next().target.equals(Node.getKey("domain1", "operation2", "com.software.BestClass1", "method2", 2));
        mapKey2Node.get(Node.getKey("domain1", "operation2", "com.software.BestClass1", "method2", 2)).maptTarget2Edge.values().iterator().next().target.equals(Node.getKey("domain1", "operation3", "com.software.BestClass1", "method3", 3));
        mapKey2Node.get(Node.getKey("domain1", "operation3", "com.software.BestClass1", "method3", 3)).maptTarget2Edge.values().iterator().next().target.equals(Node.getKey("domain1", "operation4", "com.software.BestClass1", "method4", 4));
        mapKey2Node.get(Node.getKey("domain1", "operation4", "com.software.BestClass1", "method4", 4)).maptTarget2Edge.values().iterator().next().target.equals(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1));


        actor.mark("domain1", "operation1", null, new EventComputedDetails(1,"com.software.BestClass1","method1",20));
        actor.mark("domain2", "operation1", null, new EventComputedDetails(1,"com.software.BestClass2","method1",21));

        Assert.assertEquals(2,mapKey2Node.get(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1)).maptTarget2Edge.size());
        Node node1 = mapKey2Node.get(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1));
        Edge e2node21 = node1.maptTarget2Edge.get(Node.getKey("domain2", "operation1", "com.software.BestClass2", "method1", 1));
        e2node21.target.equals((Node.getKey("domain2", "operation1", "com.software.BestClass2", "method1", 1)));

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonOutput = gson.toJson(actor);
        System.out.print(jsonOutput);


    }

}
