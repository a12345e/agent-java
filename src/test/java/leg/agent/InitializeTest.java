package leg.agent;

import leg.agent.sample.basic.GenericSimpleThreadRun;
import org.junit.Test;

import java.util.Properties;

public class InitializeTest {


    @Test
    public void testLegAgentInit(){
        GenericSimpleThreadRun t = new GenericSimpleThreadRun(100,1);
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HistoryPrefixLogLimit.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HistorySuffixLogLimit.name(),"3");
        StringBuilder buffer = new StringBuilder();
        t.run();
    }
}
