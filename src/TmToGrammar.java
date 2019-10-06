import java.util.*;

public class TmToGrammar {
    private List<Pair<String,String>> first = new ArrayList<>();
    private List<Pair<String,String>> main = new ArrayList<>();
    private List<Pair<String,String>> last = new ArrayList<>();

    public Grammar convert(Tm machine, Set<String> sigma, Set<String> genVars) {
        String init = machine.getInit();
        Set<String> gamma = machine.getGamma();
        Set<String> accept = machine.getAccept();
        Map<Tm.Context, Tm.Transition> transitions = machine.getTransitions();

        addFirstRule("A1", "[,_]" + init + "A2");
        addFirstRule("A2", "[1,1]A2");
        addFirstRule("A2", "[,_]");

        transitions.forEach((c, t) -> {
            if (t.getDirection() == Tm.Direction.Stay) {

                sigma.forEach(a -> {
                    String before = "[" + a + "," + c.getSymbol() + "]";
                    String after = "[" + a + "," + t.getSymbol() + "]";

                    if (genVars.contains(before)) {
                        addRule(c.getName() + before, t.getName() + after);
                    }
                });

            } else if (t.getDirection() == Tm.Direction.Right) {

                sigma.forEach(a -> {
                    String before = "[" + a + "," + c.getSymbol() + "]";
                    String after = "[" + a + "," + t.getSymbol() + "]";

                    if (genVars.contains(before)) {
                        addRule(c.getName() + before, after + t.getName());
                    }
                });

            } else if (t.getDirection() == Tm.Direction.Left) {

                Set<Tm.Context> contexts = machine.getContext(t.getName());

                contexts.forEach(left -> {
                    String g = left.getSymbol();
                    sigma.forEach(l -> {
                        String context = "[" + l + "," + g + "]";
                        if (genVars.contains(context)) {
                            sigma.forEach(a -> {
                                String before = "[" + a + "," + c.getSymbol() + "]";
                                if (genVars.contains(before)) {
                                    String prod = t.getName() + context + "[" + a + "," + t.getSymbol() + "]";
                                    addRule(context + c.getName() + before, prod);
                                }
                            });
                        }
                    });

                });
            }
        });

        accept.forEach(s -> {
            gamma.forEach(g -> {
                sigma.forEach(a -> {
                    String v = "[" + a + "," + g +"]";
                    if (genVars.contains(v)) {
                        addLastRule(v + s, s + a + s);
                        addLastRule(s + v, s + a + s);
                    }
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
