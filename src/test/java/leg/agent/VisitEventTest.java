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

public class VisitEventTest extends SetupTest{

    String inputSource;

    private JsonElement computeExpectedJsonElement() throws IOException{
        JsonElement e = JsonParser.parseString(Files.readString(Paths.get(CurrentResource.getTestExpectedJsonFile(1))));
        return e;
    }

    @Test
    public void firstCreatedVisitTest() throws IOException {
        Gson gson = new Gson();
        VisitEvent ev = new VisitEvent(null,10,  0,new byte []{1,2,3,(byte)255});
        JsonElement aa = gson.toJsonTree(ev);
        JsonElement e = computeExpectedJsonElement();
        Assert.assertEquals(aa, e);
    }
    @Test
    public void succeedingVisitTest() throws IOException {
        Gson gson = new Gson();
        VisitEvent ev = new VisitEvent(null, 10,  0,new byte []{1,2,3,(byte)255});
        VisitEvent succeedingVisit = new VisitEvent(ev, 10, 0, new byte []{1,2,3,(byte)255});
        Assert.assertEquals(computeExpectedJsonElement(), gson.toJsonTree(new VisitEvent(ev, 10,  0,new byte []{1,2,3,(byte)255})));
        Assert.assertNotEquals(computeExpectedJsonElement(), gson.toJsonTree(new VisitEvent(ev, 10, 0, new byte []{1,2,3})));
        Assert.assertNotEquals(computeExpectedJsonElement(), gson.toJsonTree(new VisitEvent(ev, 10,  0,new byte []{2,2,3,(byte)255})));
        Assert.assertEquals(computeExpectedJsonElement(), gson.toJsonTree(new VisitEvent(ev, 10,  0,new byte []{1,2,3,(byte)255})));
        Assert.assertNotEquals(computeExpectedJsonElement(), gson.toJsonTree(new VisitEvent(ev, 11,  0,new byte []{1,2,3,(byte)255})));
    }

}
