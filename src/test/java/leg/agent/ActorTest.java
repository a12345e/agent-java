package leg.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ActorTest {

    @Test
    public void Test(){
        Actor actor = Actor.getCreateCurrentActor(new PropertyChainBox(null));
        Map<String,Node> mapKey2Node = actor.getMapKey2Node();
        Assert.assertEquals(0,mapKey2Node.size());

        for(int i=0; i < 10; i++ ) {
            actor.mark("domain1", "operation1", null, "com.software.BestClass1", "method1", 1);
            actor.mark("domain1", "operation2", null, "com.software.BestClass1", "method2", 2);
            actor.mark("domain1", "operation3", null, "com.software.BestClass1", "method3", 3);
            actor.mark("domain1", "operation4", null, "com.software.BestClass1", "method4", 4);

            actor.mark("domain2", "operation1", null, "com.software.BestClass2", "method1", 1);
            actor.mark("domain2", "operation2", null, "com.software.BestClass2", "method2", 2);
            actor.mark("domain2", "operation3", null, "com.software.BestClass2", "method3", 3);
            actor.mark("domain2", "operation4", null, "com.software.BestClass2", "method4", 4);
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


        actor.mark("domain1", "operation1", null, "com.software.BestClass1", "method1", 1);
        actor.mark("domain2", "operation1", null, "com.software.BestClass2", "method1", 1);

        Assert.assertEquals(2,mapKey2Node.get(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1)).maptTarget2Edge.size());
        Node node1 = mapKey2Node.get(Node.getKey("domain1", "operation1", "com.software.BestClass1", "method1", 1));
        Edge e2node21 = node1.maptTarget2Edge.get(Node.getKey("domain2", "operation1", "com.software.BestClass2", "method1", 1));
        e2node21.target.equals((Node.getKey("domain2", "operation1", "com.software.BestClass2", "method1", 1)));



    }
}
