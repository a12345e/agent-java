package leg.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ActorTest {

    @Test
    public void Test(){
        Actor actor = Actor.getCreateCurrentActor();

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

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonOutput = gson.toJson(actor);
        System.out.print(jsonOutput);


    }

}
