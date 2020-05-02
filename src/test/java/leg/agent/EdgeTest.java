package leg.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

public class

EdgeTest {

    final static String TARGET = "target";
    final static PropertyChainBox emtyPropertyBox = new PropertyChainBox(null);
    byte[] data = new byte[10];
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
        Assert.assertEquals(3,s.last.visit);
        e.resetStatistics();
        s =  e.getStatistics();
        Assert.assertEquals(0,s.last.step);
        Assert.assertNull(s.last.data);
        Assert.assertEquals(0,s.last.visit);

    }

    private byte[] visit(Edge e, long step, int fill, int dataSize){
        Arrays.fill(data,(byte)fill);
        e.visit(step,data);
        return data;
    }
    @Test
    public void testHistory()
    {
        PropertyChainBox emtyPropertyBox = new PropertyChainBox(null);
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit,2);
        pcb.set(PropertyChainBox.Property.HistorySuffixLogLimit,2);
        long beforeTime = System.nanoTime();
        Edge e = new Edge(TARGET,pcb);
        byte[] byte10 = Arrays.copyOf(visit(e,10,10,data.length),data.length);
        byte[] byte20 = Arrays.copyOf(visit(e,20,10,data.length),data.length);
        byte[] byte30 = Arrays.copyOf(visit(e,30,10,data.length),data.length);
        byte[] byte40 = Arrays.copyOf(visit(e,40,10,data.length),data.length);
        byte[] byte50 = Arrays.copyOf(visit(e,50,10,data.length),data.length);
        byte[] byte60 = Arrays.copyOf(visit(e,60,10,data.length),data.length);

        Edge.Snapshot snapShot = e.getSnapShot();
        Assert.assertEquals(2,snapShot.historyPrefix.length);
        Assert.assertEquals(2,snapShot.historySuffix.length);
        Edge.Snapshot snapshot = e.getSnapShot();
        Assert.assertArrayEquals(byte10,snapShot.historyPrefix[0].data);
        Assert.assertArrayEquals(byte20,snapShot.historyPrefix[1].data);
        Assert.assertArrayEquals(byte50,snapShot.historySuffix[0].data);
        Assert.assertArrayEquals(byte60,snapShot.historySuffix[1].data);

        Assert.assertEquals(snapShot.historyPrefix[0].step,10);
        Assert.assertEquals(snapShot.historyPrefix[1].step,20);
        Assert.assertEquals(snapShot.historyPrefix[0].visit,1);
        Assert.assertEquals(snapShot.historyPrefix[1].visit,2);

        Assert.assertEquals(snapShot.historySuffix[0].step,50);
        Assert.assertEquals(snapShot.historySuffix[1].step,60);
        Assert.assertEquals(snapShot.historySuffix[0].visit,5);
        Assert.assertEquals(snapShot.historySuffix[1].visit,6);

        e.resetHistory();
        snapShot = e.getSnapShot();
        Assert.assertEquals(0,snapShot.historyPrefix.length);
        Assert.assertEquals(0,snapShot.historySuffix.length);




    }

}
