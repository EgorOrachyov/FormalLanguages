import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps each TM state q to the set of the states,
 * which has transitions from the state q.
 *
 * Example: map contains (q, [a,b,c]) if
 *          TM has the following transitions:
 *          q -> a
 *          q -> b
 *          q -> c
 */
public class TuringMachineDirect {
    private Map<String, Set<String>> next = new LinkedHashMap<>();

    public TuringMachineDirect(TuringMachine machine) {
        machine.getTransitions().forEach((context, transition) -> {

            String q = context.getName();
            String m = transition.getName();

            if (next.containsKey(q)) {
                next.get(q).add(m);
            }
            else {
                HashSet<String> states = new HashSet<>();
                states.add(m);
                next.put(q, states);
            }

        });

        machine.getStates().forEach(s -> {
            next.putIfAbsent(s, new HashSet<>());
        });
    }

    @Override
    public String toString() {
        return next.toString();
    }

    public Map<String, Set<String>> getNext() {
        return next;
    }

}
