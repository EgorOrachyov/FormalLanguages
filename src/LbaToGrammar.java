import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.function.Consumer;

public class LbaToGrammar {
    private Tm machine;
    private Set<String> gamma;
    private Set<String> statesL;
    private Set<String> statesR;
    private String L;
    private String R;
    private List<Pair<String,String>> first = new ArrayList<>();
    private List<Pair<String,String>> main = new ArrayList<>();
    private List<Pair<String,String>> last = new ArrayList<>();

    public LbaToGrammar(Tm machine, Set<String> gamma, Set<String> statesL, Set<String> statesR) {
        this.machine = machine;
        this.gamma = gamma;
        this.statesL = statesL;
        this.statesR = statesR;
        this.L = "%";
        this.R = "$";
    }

    public LbaToGrammar(Tm machine, Set<String> gamma, Set<String> statesL, Set<String> statesR, String L, String R) {
        this.machine = machine;
        this.gamma = gamma;
        this.statesL = statesL;
        this.statesR = statesR;
        this.L = L;
        this.R = R;
    }

    public Grammar convert() {
        String init = machine.getInit();
        Set<String> accept = machine.getAccept();
        Map<Tm.Context, Tm.Transition> transitions = machine.getTransitions();

        addFirstRule("A1", createVar(init, L, "1", "1") + "A2");
        addFirstRule("A2", createVar("1", "1") + "A2");
        addFirstRule("A2", createVar("1", "1", R));

        transitions.forEach((context, transition) -> {
            String s = context.getSymbol();
            String q = context.getName();
            String p = transition.getName();

            Tm.Direction d = transition.getDirection();
            Set<Tm.Context> ctx = machine.getContext(p);

            if (d == Tm.Direction.Right) {
                if (s.equals(L)) {

                    /////////////////////////// 1 ///////////////////////////

                    forEachCtxGamma(ctx, c -> {
                        addRule(createVar(q, L, c, "1"),
                                createVar(L, p, c, "1"));
                    });

                } else {

                    forEachCtxGamma(ctx, c -> {

                        /////////////////////////// 1 ///////////////////////////

                        if (statesL.contains(q))
                            addRule(createVar(L, q, s, "1") + createVar(c, "1"),
                                    createVar(L, transition.getSymbol(), "1") + createVar(p, c, "1"));

                        if (statesL.contains(q) && statesR.contains(p))
                            addRule(createVar(L, q, s, "1") + createVar(c, "1", R),
                                    createVar(L, transition.getSymbol(), "1") + createVar(p, c, "1", R));

                        /////////////////////////// 2 ///////////////////////////

                        addRule(createVar(q, s, "1") +
                                        createVar(c, "1"),
                                createVar(transition.getSymbol(), "1") +
                                        createVar(p, c, "1"));


                        if (statesR.contains(p))
                            addRule(createVar(q, s, "1") +
                                        createVar(c, "1", R),
                                    createVar(transition.getSymbol(), "1") +
                                        createVar(p, c, "1", R));
                    });

                    /////////////////////////// 3 ///////////////////////////

                    if (statesR.contains(q))
                        addRule(createVar(q, s, "1", R),
                                createVar(transition.getSymbol(), "1", p, R));

                }
            } else if (d == Tm.Direction.Stay) {
                if (s.equals(L)) {

                    /////////////////////////// 1 ///////////////////////////

                    gamma.forEach(X -> {
                        addRule(createVar(q, L, X, "1"),
                                createVar(p, L, X, "1"));
                    });

                } else if (s.equals(R)) {

                    /////////////////////////// 3 ///////////////////////////

                    gamma.forEach(X -> {
                        addRule(createVar(X, "1", q, R),
                                createVar(X, "1", p, R));
                    });

                } else {

                    /////////////////////////// 1 ///////////////////////////

                    if (statesL.contains(q))
                        addRule(createVar(L, q, s, "1"),
                                createVar(L, transition.getSymbol(), "1"));

                    /////////////////////////// 2 ///////////////////////////

                    addRule(createVar(q, s, "1"),
                            createVar(p, transition.getSymbol(), "1"));

                    /////////////////////////// 3 ///////////////////////////

                    if (statesR.contains(q))
                        addRule(createVar(q, s, "1", R),
                                createVar(transition.getSymbol(), "1", R));

                }
            } else if (d == Tm.Direction.Left) {
                if (s.equals(R)) {

                    /////////////////////////// 3 ///////////////////////////

                    forEachCtxGamma(ctx, c -> {
                        addRule(createVar(c, "1", q, R),
                                createVar(p, c, "1", R));
                    });

                } else {

                    /////////////////////////// 1 ///////////////////////////

                    if (statesL.contains(q))
                        addRule(createVar(L, q, s, "1"),
                                createVar(p, L, transition.getSymbol(), "1"));

                    forEachCtxGamma(ctx, c -> {

                        /////////////////////////// 2 ///////////////////////////

                        addRule(createVar(c, "1") +
                                        createVar(q, s, "1"),
                                createVar(p, c, "1") +
                                        createVar(transition.getSymbol(), "1"));

                        if (statesL.contains(p))
                            addRule(createVar(L, c, "1") +
                                        createVar(q, s, "1"),
                                    createVar(L,p, c, "1") +
                                        createVar(transition.getSymbol(), "1"));

                        /////////////////////////// 3 ///////////////////////////

                        if (statesR.contains(q))
                            addRule(createVar(c, "1") +
                                        createVar(q, s, "1", R),
                                    createVar(p, c, "1") +
                                        createVar(transition.getSymbol(), "1", R));

                    });
                }

            }
        });

        accept.forEach(q -> {
            gamma.forEach(X -> {

                addLastRule(createVar(q, L, X, "1"), "1");
                addLastRule(createVar(L, q, X, "1"), "1");

                addLastRule(createVar(q, X, "1"), "1");

                addLastRule(createVar(q, X, "1", R), "1");
                addLastRule(createVar(X, "1", q, R), "1");

                addLastRule("1" + createVar(X, "1"), "11");
                addLastRule("1" + createVar(X, "1", R), "11");

                addLastRule(createVar(X, "1") + "1", "11");
                addLastRule(createVar(L, X, "1") + "1", "11");

            });
        });

        return new Grammar("A1", first, main, last);
    }

    private void forEachCtxGamma(Set<Tm.Context> ctx, Consumer<? super  String> action) {
        ctx.forEach(c -> {
            if (!c.getSymbol().equals(R) && !c.getSymbol().equals(L)) {
                action.accept(c.getSymbol());
            }
        });
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
        Tm machine = new TmLoader().load(new FileInputStream("PrimeNumsLBA.txt"));

        Set<String> gamma = new HashSet<>();
        gamma.add("1");
        gamma.add("#");

        Set<String> statesL = new HashSet<>();
        statesL.add("init");
        statesL.add("copy2");
        statesL.add("finishCopy1");
        statesL.add("moveLeft");
        statesL.add("markSt");

        Set<String> statesR = new HashSet<>();
        statesR.add("markDiv");
        statesR.add("finishCopy3");
        statesR.add("checkInit");
        statesR.add("copy5");
        statesR.add("copy6");
        statesR.add("incDiv3");
        statesR.add("moveLeft");
        statesR.add("copyDone1");
        statesR.add("copyDone2");
        statesR.add("copyDone3");

        Grammar grammar = new LbaToGrammar(machine, gamma, statesL, statesR).convert();
        new GrammarSerialize().serialize(new FileOutputStream("PrimeNumsT1.txt"), grammar);
    }

}