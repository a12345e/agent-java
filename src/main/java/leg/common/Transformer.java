package leg.common;

public interface Transformer<F> {
    public Transformable deserialize(F v);
    public F serialize(Transformable e);
}
