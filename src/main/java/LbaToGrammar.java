import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class LbaToGrammar {
    private List<Pair<String,String>> first = new ArrayList<>();
    private List<Pair<String,String>> main = new ArrayList<>();
    private List<Pair<String,String>> last = new ArrayList<>();

    public Grammar convert(TuringMachine machine, Set<String> sigma, Set<String> posPairs) {
        String init = machine.getInit();
        Set<String> gamma = machine.getGamma();
        Set<String> accept = machine.getAccept();
        Map<TuringMachine.Context, TuringMachine.Transition> transitions = machine.getTransitions();

        String L = "%";
        String R = "$";

        addFirstRule("A1", "[,_]" + "[" + init + ",1,1]" + "A2");
        addFirstRule("A2", "[1,1]A2");
        addFirstRule("A2", "[,_]");

        transitions.forEach((context, transition) -> {
            String s = context.getSymbol();
            TuringMachine.Direction d = transition.getDirection();

            if (d == TuringMachine.Direction.Right) {
                if (s.equals("%")) {
                    gamma.forEach(X -> {
                         addRule(createVar("[" + context.getName() + "," + L, X, "1"),
                                 createVar("[" + L + "," + transition.getName(), X, "1"));
                    });
                } else {
                    gamma.forEach(Z -> {
                        addRule(createVar(L + "," + context.getName(), context.getSymbol(), "1") + createVar(Z, "1"),
                                createVar(L, transition.getSymbol(), "1") + createVar(transition.getName(), Z, "1"));
                    });
                }
            } else if (d == TuringMachine.Direction.Stay) {
                if (s.equals("%")) {
                    gamma.forEach(X -> {
                        addRule(createVar(context.getName(), L, X, "1"),
                                createVar(transition.getName(), L, X, "1"));
                    });
                } else {
                    addRule(createVar(L, context.getName(), context.getSymbol(), "1"),
                            createVar(L, transition.getSymbol(), "1"));
                }
            } else if (d == TuringMachine.Direction.Left) {
                addRule(createVar(L, context.getName(), context.getSymbol(), "1"),
                        createVar(transition.getName(), L, transition.getSymbol(), "1"));
            }
        });

        transitions.forEach((context, transition) -> {
            String s = context.getSymbol();
            TuringMachine.Direction d = transition.getDirection();

            if (d == TuringMachine.Direction.Right) {
                gamma.forEach(Z -> {
                    addRule(createVar(context.getName(), context.getSymbol(), "1") +
                                    createVar(Z, "1"),
                            createVar(transition.getSymbol(), "1") +
                                    createVar(transition.getName(), Z, "1"));

                    addRule(createVar(context.getName(), context.getSymbol(), "1") +
                                    createVar(Z, "1", R),
                            createVar(transition.getSymbol(), "1") +
                                    createVar(transition.getName(), Z, "1", R));
                });
            } else if (d == TuringMachine.Direction.Stay) {
                addRule(createVar(context.getName(), context.getSymbol(), "1"),
                        createVar(transition.getName(), transition.getSymbol(), "1"));
            } else if (d == TuringMachine.Direction.Left) {
                gamma.forEach(Z -> {
                    addRule(createVar(Z, "1") +
                                    createVar(context.getName(), context.getSymbol(), "1"),
                            createVar(transition.getName(), Z, "1") +
                                    createVar(transition.getSymbol(), "1"));

                    addRule(createVar(L, Z, "1") +
                                    createVar(context.getName(), context.getSymbol(), "1"),
                            createVar(L,transition.getName(), Z, "1") +
                                    createVar(transition.getSymbol(), "1"));
                });
            }
        });

        transitions.forEach((context, transition) -> {

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

    private String createVar(String b, String X, String a, String r) {
        return "[" + b + "," + X + "," + a + "," + r + "]";
    }

    private String createVar(String b, String X, String a) {
        return "[" + b + "," + X + "," + a + "]";
    }

    private String createVar(String X, String a) {
        return "[" + X + "," + a + "]";
    }

    public static void main(String[] args) throws Exception {
        TuringMachine machine = new TuringMachineLoader().load(new FileInputStream("PrimeNumsLBA.txt"));
        Grammar grammar = new LbaToGrammar().convert(machine, null, null);
        new GrammarSerialize().serialize(new FileOutputStream("PrimeNumsT1.txt"), grammar);
    }

}
