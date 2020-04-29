package leg.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class

EdgeTest {

    final static String TARGET = "target";
    final static PropertyChainBox emtyPropertyBox = new PropertyChainBox(null);

    private void checkEmpty(Edge.Statistics s){
        Assert.assertEquals(s.last.step,0);
        Assert.assertEquals(s.first.time,0);
        Assert.assertEquals(s.first.visit,0);
        Assert.assertNull(s.first.data);

        Assert.assertEquals(s.last.step,0);
        Assert.assertEquals(s.last.time,0);
        Assert.assertEquals(s.last.visit,0);
        Assert.assertNull(s.last.data);

    }
    @Test
    public void testFillNoEvent()
    {
        Edge e = new Edge(TARGET,emtyPropertyBox);
        Edge.Statistics statistics =  e.getStatistics();
        checkEmpty(statistics);
    }

    @Test
    public void testFillStatisticsEvent()
    {
        long beforeTime = System.nanoTime();
        Edge e = new Edge(TARGET,emtyPropertyBox);
        byte[] bytes = new byte[4];
        Arrays.fill(bytes,(byte)5);
        byte[] data5 = bytes.clone();
        e.visit(1,bytes);
        Arrays.fill(bytes,(byte)6);
        e.visit(2,bytes);
        Arrays.fill(bytes,(byte)7);
        e.visit(3,bytes);
        Edge.Statistics s =  e.getStatistics();
        Assert.assertEquals(s.first.step, 1L);
        Assert.assertArrayEquals(s.first.data,data5);
        Assert.assertEquals(s.first.visit,1);

        Assert.assertTrue(s.first.time <= s.last.time);
        Assert.assertTrue(s.first.time >= beforeTime );

        Assert.assertEquals(s.last.step, 3L);
        Assert.assertArrayEquals(s.last.data,bytes);
        Assert.assertEquals(s.last.visit,3);
    }


}
