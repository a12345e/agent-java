package leg.agent.sample.basic;

import leg.agent.LEgApi;
import org.junit.Test;

import java.util.Properties;

public class BasicRun {

    private void secondStep(){
        LEgApi.mark("domainA","OperationC",new byte[0]);

    }
    private void firstStep(){
        LEgApi.mark("domainA","OperationA",new byte[0]);
        LEgApi.mark("domainA","OperationB",new byte[0]);
        secondStep();
        LEgApi.mark("domainA","Operationc" +
                "" +
                "" +
                "",new byte[0]);
    }

    public int run(Properties prop, StringBuilder buf){
        LEgApi.init(prop,buf);
        firstStep();
        LEgApi.dispose(buf);
        return 0;
    }
}
