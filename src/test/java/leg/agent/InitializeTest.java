package leg.agent;

import leg.agent.sample.basic.GenericSimpleThreadRun;
import org.junit.Test;

import java.util.Properties;

public class InitializeTest {


    @Test
    public void testLegAgentInit(){
        GenericSimpleThreadRun t = new GenericSimpleThreadRun(100,1,"Adomain");
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HISTORY_PREFIX_LOG_LIMIT.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HISTORY_SUFFIX_LOG_LIMIT.name(),"3");
        StringBuilder buffer = new StringBuilder();
        t.run();
    }
}
