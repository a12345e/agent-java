package leg.agent;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropertyChainBoxTest {

    @Test
    public void PropertiesSetTest() {
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT.name(),"4");
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        Assert.assertEquals(4,pcb.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
    }


    @Test
    public void PropertiesInheritanceTest() {
        PropertyChainBox pcb = new PropertyChainBox(null,new Properties());
        pcb.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,4,false);
        pcb = new PropertyChainBox(pcb);
        Assert.assertEquals(4,pcb.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
    }
    @Test
    public void PropertiesLocalPrecedenceOverInheritanceTest() {
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,4,false);
        pcb = new PropertyChainBox(pcb);
        pcb.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,5,false);
        Assert.assertEquals(5,pcb.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
    }
    @Test
    public void PropertiesMultipleInheritanceTest() {
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,4,false);
        PropertyChainBox middle = new PropertyChainBox(pcb);
        middle.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,5,false);
        PropertyChainBox top = new PropertyChainBox(middle);
        Assert.assertEquals(5,top.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
        top.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,6,false);
        Assert.assertEquals(6,top.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
    }
    @Test
    public void PropertiesRemoveTest() {
        Properties prop = new Properties();
        PropertyChainBox pcb = new PropertyChainBox(null,prop);
        pcb.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,4,false);
        PropertyChainBox middle = new PropertyChainBox(pcb);
        middle.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,5,false);
        PropertyChainBox top = new PropertyChainBox(middle);
        Assert.assertEquals(5,top.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
        top.set(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,6,false);
        Assert.assertEquals(6,top.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
        top.remove(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT);
        Assert.assertEquals(5,top.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));
        pcb.remove(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT,true);
        Assert.assertEquals(top.getInt(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT),0);
        Assert.assertNull(top.get(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT));


    }
}
