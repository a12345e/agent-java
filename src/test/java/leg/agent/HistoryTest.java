package leg.agent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import leg.agent.util.CurrentResource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class HistoryTest extends SetupTest {
    String inputSource;

    private JsonElement computeExpectedJsonElement() throws IOException {
        return JsonParser.parseString(Files.readString(Paths.get(CurrentResource.getTestExpectedJsonFile(1))));
    }


    @Test
    public void historyFirstEvent() throws IOException {
        Gson gson = new Gson();
        PropertyChainBox pcb = new PropertyChainBox(null, new Properties());
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 4);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2);
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
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 4);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2);
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

    @Test
    public void historyFull() throws IOException {
        Gson gson = new Gson();
        PropertyChainBox pcb = new PropertyChainBox(null, new Properties());
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit, 2);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit, 2);
        History history = new History(pcb);
        history.visit(10, 10,new byte[]{1, 2, 1, (byte) 255});
        history.visit(11, 11,new byte[]{1, 2, 2, (byte) 255});
        history.visit(12, 12,new byte[]{1, 2, 3, (byte) 255});
        history.visit(13, 13,new byte[]{1, 2, 4, (byte) 255});
        history.visit(14, 14,new byte[]{1, 2, 5, (byte) 255});
        history.visit(15, 15,new byte[]{1, 2, 6, (byte) 255});

        JsonElement jsonElement = gson.toJsonTree(history);
        Assert.assertEquals(10, jsonElement.getAsJsonObject().get("first").getAsJsonObject().get("time").getAsInt());
    }
}
