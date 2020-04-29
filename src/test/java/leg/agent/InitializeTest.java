package leg.agent;

import leg.agent.sample.basic.BasicRun;
import org.junit.Test;

import java.util.Properties;

public class InitializeTest {


    @Test
    public void testLegAgentInit(){
        BasicRun run = new BasicRun();
        Properties prop = new Properties();
        prop.setProperty(PropertyChainBox.Property.HistoryPrefixLogLimit.name(),"4");
        prop.setProperty(PropertyChainBox.Property.HistorySuffixLogLimit.name(),"3");
        StringBuilder buffer = new StringBuilder();
        run.run(prop,buffer);
    }
}
