package leg.agent;


import java.util.LinkedList;
import java.util.List;

public class Edge {
    public final String target;
    private final History history;
    final transient PropertyChainBox properties;
    public Edge(String target, PropertyChainBox parentProperty) {
        this.target = target;
        this.properties = new PropertyChainBox(parentProperty);
        history = new History(properties);
    }

    public final History getHistory() {
        return history;
    }
}

