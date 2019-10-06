import java.io.FileInputStream;
import java.util.*;

public class SetGenerator {

    private Set<String> statesLeft = new LinkedHashSet<>();
    private Set<String> statesRight = new LinkedHashSet<>();
    private Tm machine;
    private Set<String> gamma;

    public SetGenerator(Tm machine, Set<String> gamma) {
        this.machine = machine;
        this.gamma = gamma;
    }

    public void run() {
        statesLeft.addAll(machine.getStates());
        statesRight.addAll(machine.getStates());

        runOnSet(statesLeft);
        runOnSet(statesRight);
    }

    private void runOnSet(Set<String> states) {

        ArrayList<String> attempt = new ArrayList<>(states);

        attempt.forEach(s -> {

            String res;
            states.remove(s);
            Grammar g = new LbaToGrammar(machine, gamma, statesLeft, statesRight).convert();

            res = runDerivation(new DerivationBuilder(g, "[init,%,1,1][1,1,$]"));
            if (!res.equals("11")) {
                states.add(s);
                return;
            }

            res = runDerivation(new DerivationBuilder(g, "[init,%,1,1][1,1][1,1,$]"));
            if (!res.equals("111")) {
                states.add(s);
                return;
            }

            res = runDerivation(new DerivationBuilder(g, "[init,%,1,1][1,1][1,1][1,1][1,1,$]"));
            if (!res.equals("11111")) {
                states.add(s);
                return;
            }

            res = runDerivation(new DerivationBuilder(g, "[init,%,1,1][1,1][1,1][1,1][1,1][1,1][1,1,$]"));
            if (!res.equals("1111111")) {
                states.add(s);
                return;
            }

            res = runDerivation(new DerivationBuilder(g, "[init,%,1,1][1,1][1,1][1,1][1,1][1,1][1,1][1,1][1,1,$]"));
            if (!res.contains("notPrime")) {
                states.add(s);
                return;
            }

            res = runDerivation(new DerivationBuilder(g, "[init,%,1,1][1,1][1,1][1,1][1,1][1,1][1,1][1,1][1,1][1,1][1,1,$]"));
            if (!res.equals("11111111111")) {
                states.add(s);
                return;
            }

        });

    }

    private String runDerivation(DerivationBuilder d) {
        while (d.canApply())
            d.applyNext();

        while (d.canApplyFinal())
            d.applyNextFinal();

        return d.toString();
    }

    public Set<String> getStatesLeft() {
        return statesLeft;
    }

    public Set<String> getStatesRight() {
        return statesRight;
    }

    public static void main(String[] args) throws Exception {
        Tm machine = new TmLoader().load(new FileInputStream("PrimeNumsLBA.txt"));

        Set<String> gamma = new HashSet<>();
        gamma.add("1");
        gamma.add("#");

        SetGenerator generator = new SetGenerator(machine, gamma);
        generator.run();

        System.out.println(generator.getStatesLeft());
        System.out.println(generator.getStatesRight());
    }

}
