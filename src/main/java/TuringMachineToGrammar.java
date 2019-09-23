import java.util.*;

public class TuringMachineToGrammar {
    private List<Pair<String,String>> first = new ArrayList<>();
    private List<Pair<String,String>> main = new ArrayList<>();
    private List<Pair<String,String>> last = new ArrayList<>();

    public Grammar convert(TuringMachine machine) {
        Set<String> sigma = new HashSet<>();
        sigma.add("");
        sigma.add("1");

        String init = machine.getInit();
        Set<String> gamma = machine.getGamma();
        Set<String> accept = machine.getAccept();
        Map<TuringMachine.Context, TuringMachine.Transition> transitions = machine.getTransitions();

        addFirstRule("A1", "[,_]" + init + "A2");
        addFirstRule("A2", "[1,1]A2");
        addFirstRule("A2", "A3");
        addFirstRule("A3", "");
        addFirstRule("A3", "[,_]A3");

        transitions.forEach((c, t) -> {
            if (t.getDirection() == TuringMachine.Direction.Stay) {
                sigma.forEach(a -> {
                    addRule(c.getName() + "[" + a + "," + c.getSymbol() + "]", t.getName() + "[" + a + "," + t.getSymbol() + "]");
                });
            } else if (t.getDirection() == TuringMachine.Direction.Right) {
                sigma.forEach(a -> {
                    addRule(c.getName() + "[" + a + "," + c.getSymbol() + "]", "[" + a + "," + t.getSymbol() + "]" + t.getName());
                });
            } else if (t.getDirection() == TuringMachine.Direction.Left) {

                Set<TuringMachine.Context> contexts = machine.getContext(c.getName());
                contexts.forEach(left -> {
                    sigma.forEach(l -> {
                        sigma.forEach(r -> {
                            String g = left.getSymbol();
                            String context = "[" + l + "," + g + "]";
                            String state = c.getName() + "[" + r + "," + c.getSymbol() + "]";
                            String prod = t.getName() + context + "[" + r + "," + t.getSymbol() + "]";
                            addRule(context + state, prod);
                        });
                    });
                });

                // sigma.forEach(l -> {
                //     sigma.forEach(r -> {
                //         gamma.forEach(g -> {
                //             String context = "[" + l + "," + g + "]";
                //             String state = c.getName() + "[" + r + "," + c.getSymbol() + "]";
                //             String prod = t.getName() + context + "[" + r + "," + t.getSymbol() + "]";
                //             addRule(context + state, prod);
                //         });
                //     });
                // });
            }
        });

        accept.forEach(s -> {
            gamma.forEach(g -> {
                sigma.forEach(a -> {
                    addLastRule("[" + a + "," + g +"]" + s, s + a + s);
                    addLastRule(s + "[" + a + "," + g +"]", s + a + s);
                });
            });
            addLastRule(s, "");
        });

        return new Grammar("A1", first, main, last);
    }

    private void addRule(String left, String right) {
        main.add(new Pair<>(left, right));
    }

    private void addFirstRule(String left, String right) {
        first.add(new Pair<>(left, right));
    }

    private void addLastRule(String left, String right) {
        last.add(new Pair<>(left, right));
    }

}
