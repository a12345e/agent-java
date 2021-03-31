package leg.agent;

import com.google.gson.*;
import leg.agent.util.CurrentResource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HistoryTest extends SetupTest {
    String inputSource;



    @Test
    public void historyFirstEvent() throws IOException {
        Gson gson = new Gson();
        PropertyChainBox pcb = new PropertyChainBox(null, new Properties());
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 4,false);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2,false);
        History history = new History(pcb);
        history.visit(10, 10,new byte[]{1, 2, 3, (byte) 255});

        JsonElement jsonElement = gson.toJsonTree(history.getFirstEvent());
        Assert.assertEquals(10, jsonElement.getAsJsonObject().get("time").getAsInt());
        Assert.assertEquals(10, jsonElement.getAsJsonObject().get("step").getAsInt());
        Assert.assertEquals(0, jsonElement.getAsJsonObject().get("visit").getAsInt());
        Assert.assertEquals(1, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsInt());
        Assert.assertEquals(2, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(1).getAsInt());
        Assert.assertEquals(3, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(2).getAsInt());
        Assert.assertEquals(-1, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(3).getAsInt());
    }

    @Test
    public void historyLastEvent() throws IOException {
        Gson gson = new Gson();
        PropertyChainBox pcb = new PropertyChainBox(null, new Properties());
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 4,false);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2,false);
        History history = new History(pcb);
        history.visit(10, 10,new byte[]{1, 2, 3, (byte) 255});
        history.visit(11, 11,new byte[]{1, 2, 4, (byte) 255});

        JsonElement jsonElement = gson.toJsonTree(history.getLastEvent());
        Assert.assertEquals(11, jsonElement.getAsJsonObject().get("time").getAsInt());
        Assert.assertEquals(11, jsonElement.getAsJsonObject().get("step").getAsInt());
        Assert.assertEquals(1, jsonElement.getAsJsonObject().get("visit").getAsInt());
        Assert.assertEquals(1, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0).getAsInt());
        Assert.assertEquals(2, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(1).getAsInt());
        Assert.assertEquals(4, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(2).getAsInt());
        Assert.assertEquals(-1, jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(3).getAsInt());

    }


    private void CheckVisit(JsonObject o, long time, long step, long visit, byte[] data)
    {
        Assert.assertEquals(time, o.get("time").getAsInt());
        Assert.assertEquals(step, o.get("step").getAsInt());
        Assert.assertEquals(visit, o.get("visit").getAsInt());
        int index = 0;
        for(byte b: data){
            Assert.assertEquals((int) b, o.get("data").getAsJsonArray().get(index++).getAsInt());
        }

    }
    @Test
    public void historyFull() throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        PropertyChainBox pcb = new PropertyChainBox(null, new Properties());
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 2);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2);
        History history = new History(pcb);
        history.visit(10, 1000,new byte[]{1, 2, 1, (byte) 255});
        history.visit(11, 1001,new byte[]{1, 2, 2, (byte) 255});
        history.visit(12, 1002,new byte[]{1, 2, 3, (byte) 255});
        history.visit(13, 1003,new byte[]{1, 2, 4, (byte) 255});
        history.visit(14, 1004,new byte[]{1, 2, 5, (byte) 255});
        history.visit(15, 1005,new byte[]{1, 2, 6, (byte) 255});

        JsonElement jsonElement = gson.toJsonTree(history);
        Assert.assertEquals(2, jsonElement.getAsJsonObject().get("firstEvents").getAsJsonArray().size());
        Assert.assertEquals(2, jsonElement.getAsJsonObject().get("lastEvents").getAsJsonArray().size());
        CheckVisit( jsonElement.getAsJsonObject().get("firstEvents").getAsJsonArray().get(0).getAsJsonObject(),1000,10,0,new byte[]{1, 2, 1, (byte) 255});
        CheckVisit( jsonElement.getAsJsonObject().get("firstEvents").getAsJsonArray().get(1).getAsJsonObject(),1001,11,1,new byte[]{1, 2, 2, (byte) 255});
        CheckVisit( jsonElement.getAsJsonObject().get("lastEvents").getAsJsonArray().get(0).getAsJsonObject(),1004,14,4,new byte[]{1, 2, 5, (byte) 255});
        CheckVisit( jsonElement.getAsJsonObject().get("lastEvents").getAsJsonArray().get(1).getAsJsonObject(),1005,15,5,new byte[]{1, 2, 6, (byte) 255});

        String jsonOutput = gson.toJson(history);
        System.out.print(jsonOutput);

    }
}
