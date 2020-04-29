package leg.agent;

import org.junit.Assert;
import org.junit.Test;

public class

EdgeTest {

    final static String TARGET = "target";
    final static PropertyChainBox emtyPropertyBox = new PropertyChainBox(null);

    private void checkEmpty(Edge.Statistics s){
        Assert.assertEquals(s.lastThreadStep,0);
        Assert.assertEquals(s.firstThreadStep,0);
        Assert.assertEquals(s.first,0);
        Assert.assertEquals(s.last,0);
        Assert.assertNull(s.lastPropertyData);
        Assert.assertEquals(s.visits,0);
    }
    @Test
    public void testFillOneEvent()
    {
        Edge e = new Edge(TARGET,emtyPropertyBox);
        Edge.Statistics statistics =  e.getStatistics();
        checkEmpty(statistics);
    }


}
