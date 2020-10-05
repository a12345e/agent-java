package leg.agent.runable;

import java.util.List;
import java.util.Optional;

public interface ProcedureElement {
    public Operation head();
    public List<ProcedureElement> next();
    public void append(ProcedureElement e);

}
