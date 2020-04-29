package leg.agent;

import org.apache.commons.text.CaseUtils;

import java.util.*;

public class PropertyChainBox {
    public enum Property {
        HistoryPrefixLogLimit,
        HistorySuffixLogLimit;
    }
    private final PropertyChainBox parent;
    private final Set<PropertyChainBox> children;
    private final Map<Property,Object> properties;
    private final void setInt(Property property,Properties externalProperties){
        String value = (String) externalProperties.getOrDefault(property.name(),"0");
        set(property, Integer.valueOf(value));
    }
    PropertyChainBox(PropertyChainBox parent,Properties properties){
        this(parent);
        setInt(Property.HistoryPrefixLogLimit,properties);
        setInt(Property.HistorySuffixLogLimit,properties);
    }
    PropertyChainBox(PropertyChainBox parent){
        this.parent = parent;
        this.properties = new HashMap<Property,Object>();
        this.children = new HashSet<PropertyChainBox>();
        if(parent != null){
            parent.addChild(this);
        }
    }
    private void  addChild(PropertyChainBox p){
        children.add(p);
    }
    private void  removeChild(PropertyChainBox p){
        children.remove(p);
    }
    public int

    getInt(Property name){
        Object  o = get(name);
        if(o == null){
            return 0;
        }else {
            return (Integer)o;
        }
    }
    public String getString(Property name){
        Object  o = get(name);
        if(o == null){
            return "";
        }else {
            return (String)o;
        }
    }
    Object get(Property name){
        Object value = properties.get(name);
        if(value == null && parent != null){
            value = parent.get(name);
        }
        return value;
    }
    public void set(Property name, Object value){
        properties.put(name,value);
    }
    public void remove(Property name, boolean recursive){
        properties.remove(name);
        if(recursive){
            children.stream().forEach(x -> x.remove(name,recursive));
        }
    }


    public void remove(Property name){
        remove(name,false);
    }

}
