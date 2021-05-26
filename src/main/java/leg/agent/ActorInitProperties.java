package leg.agent;

import java.util.Properties;

public class ActorInitProperties {
    static private PropertyChainBox actorInitProperties;
    static public void initialize(Properties properties){
        actorInitProperties = new PropertyChainBox(null);
        for(ActorProperty property: ActorProperty.values()){
            Object value = properties.get(property.name());
            if(value == null){
                value = property.getDefault();
            }
            actorInitProperties.set(property.name(),value);
        }
    }
    static public PropertyChainBox getRoot(){return actorInitProperties;};


}
