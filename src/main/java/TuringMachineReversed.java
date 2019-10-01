import java.util.*;

/**
 * Maps each TM state q to the set of the states,
 * which has transitions to state q.
 *
 * Example: map contains (q, [a,b,c]) if
 *          TM has the following transitions:
 *          a -> q
 *          b -> q
 *          c -> q
 */
public class TuringMachineReversed {
    private Map<String, Set<String>> pred = new LinkedHashMap<>();

    public TuringMachineReversed(TuringMachine machine) {
        machine.getTransitions().forEach((context, transition) -> {

            String q = transition.getName();
            String m = context.getName();

            if (pred.containsKey(q)) {
                pred.get(q).add(m);
            }
            else {
                HashSet<String> states = new HashSet<>();
                states.add(m);
                pred.put(q, states);
            }

        });

        machine.getStates().forEach(s -> {
            pred.putIfAbsent(s, new HashSet<>());
        });
    }

    @Override
    public String toString() {
        return pred.toString();
    }

    public Map<String, Set<String>> getPred() {
        return pred;
    }

}

