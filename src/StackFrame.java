import java.util.HashMap;
import java.util.Map;

public class StackFrame {
    private final Map<String, String> variables;

    private StackFrame(Map<String, String> variables) {
        this.variables = variables;
    }

    public StackFrame() {
        this.variables = new HashMap<>();
    }

    public StackFrame closeApon(Iterable<String> variables) {
        final var varMap = new HashMap<String, String>();

        for (String variable : variables) {
            varMap.put(variable, this.variables.get(variable));
        }
        return new StackFrame(varMap);
    }
}
