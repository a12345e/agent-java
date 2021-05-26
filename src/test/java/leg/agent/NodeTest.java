package leg.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

public class NodeTest {
    PropertyChainBox emtyPropertyBox = new PropertyChainBox(null);

    @Test
    public void testNodeEdges(){
        emtyPropertyBox.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT, 2);
        emtyPropertyBox.set(PropertyChainBox.Property.HISTORY_SUFFIX_LOG_LIMIT, 2);
        Node node1 = new Node("domain","operation1","className1","method1",1,emtyPropertyBox);
        Node node2 = new Node("domain","operation2","className2","method2",2,emtyPropertyBox);
        Node node3 = new Node("domain","operation3","className3","method3",3,emtyPropertyBox);
        Node node4 = new Node("domain","operation3","className4","method4",4,emtyPropertyBox);

        for(int i = 0; i < 10; i++){
            if(i % 2 == 0){
                node1.visit(node2.getKey(),i*10,10+2*i,null);
                node2.visit(node3.getKey(),i*10,10+2*i,null);
            }else {
                node1.visit(node3.getKey(),i*10,10+2*i,null);
            }
            node3.visit(node4.getKey(),i*10,i*10,null);
            node4.visit(node1.getKey(),i*10,i*10,null);
        }
        Assert.assertEquals(2,node1.maptTarget2History.values().size());
        Edge e[] = node1.maptTarget2History.values().toArray(new Edge[0]);
        Assert.assertEquals(node2.getKey(),e[0].target);
        Assert.assertEquals(node3.getKey(),e[1].target);
        Assert.assertEquals(1,node2.maptTarget2History.values().size());
        Assert.assertEquals(node3.getKey(),node2.maptTarget2History.values().stream().findFirst().get().target);
        Assert.assertEquals(1,node3.maptTarget2History.values().size());
        Assert.assertEquals(node4.getKey(),node3.maptTarget2History.values().stream().findFirst().get().target);
        Assert.assertEquals(1,node4.maptTarget2History.values().size());
        Assert.assertEquals(node1.getKey(),node4.maptTarget2History.values().stream().findFirst().get().target);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonOutput = gson.toJson(node1);
        System.out.print(jsonOutput);
    }
}
