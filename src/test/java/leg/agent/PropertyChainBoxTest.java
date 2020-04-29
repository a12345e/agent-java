package leg.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropertyChainBoxTest {

    @Test
    public void PropertiesSetTest() {
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HistoryPrefixLogLimit.name(),"4");
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        Assert.assertEquals(4,pcb.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
    }


    @Test
    public void PropertiesInheritanceTest() {
        PropertyChainBox pcb = new PropertyChainBox(null,new Properties());
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit,4);
        pcb = new PropertyChainBox(pcb);
        Assert.assertEquals(4,pcb.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
    }
    @Test
    public void PropertiesLocalPrecedenceOverInheritanceTest() {
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit,4);
        pcb = new PropertyChainBox(pcb);
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit,5);
        Assert.assertEquals(5,pcb.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
    }
    @Test
    public void PropertiesMultipleInheritanceTest() {
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit,4);
        PropertyChainBox middle = new PropertyChainBox(pcb);
        middle.set(PropertyChainBox.Property.HistoryPrefixLogLimit,5);
        PropertyChainBox top = new PropertyChainBox(middle);
        Assert.assertEquals(5,top.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
        top.set(PropertyChainBox.Property.HistoryPrefixLogLimit,6);
        Assert.assertEquals(6,top.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
    }
    @Test
    public void PropertiesRemoveTest() {
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HistoryPrefixLogLimit,4);
        PropertyChainBox middle = new PropertyChainBox(pcb);
        middle.set(PropertyChainBox.Property.HistoryPrefixLogLimit,5);
        PropertyChainBox top = new PropertyChainBox(middle);
        Assert.assertEquals(5,top.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
        top.set(PropertyChainBox.Property.HistoryPrefixLogLimit,6);
        Assert.assertEquals(6,top.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
        top.remove(PropertyChainBox.Property.HistoryPrefixLogLimit);
        Assert.assertEquals(5,top.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit));
        pcb.remove(PropertyChainBox.Property.HistoryPrefixLogLimit,true);
        Assert.assertEquals(top.getInt(PropertyChainBox.Property.HistoryPrefixLogLimit),0);
        Assert.assertNull(top.get(PropertyChainBox.Property.HistoryPrefixLogLimit));


    }
}
