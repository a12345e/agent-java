package leg.agent;

public enum ActorProperty {
    HISTORY_PREFIX_LOG_LIMIT(5),
    HISTORY_SUFFIX_LOG_LIMIT(5);
    ActorProperty(int v){

        value = Integer.toString(v);
    }
    public String getDefault(){ return value;}
    private String value;

}
