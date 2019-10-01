import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        /*
        TuringMachine machine = new TuringMachineLoader().load(new FileInputStream("PrimeNumsEH.txt"));
        System.out.println(machine.getGamma());
        System.out.println(machine.getAccept());
        System.out.println(machine.getStates().size());
        System.out.println(machine.getTransitions().size());

        Set<String> genVars = new HashSet<>();
        genVars.add("[,_]");
        genVars.add("[1,1]");
        genVars.add("[1,#]");

        Set<String> sigma = new HashSet<>();
        sigma.add("");
        sigma.add("1");

        Grammar grammar = new TuringMachineToGrammar().convert(machine, sigma, genVars);
        System.out.println(grammar.getFirst().size());
        System.out.println(grammar.getMain().size());
        System.out.println(grammar.getLast().size());

        new GrammarSerialize().serialize(new FileOutputStream("PrimeNumsT0.txt"), grammar);
        */
        Grammar loaded = new GrammarLoader().load(new FileInputStream("PrimeNumsT0.txt"));
        DerivationBuilder derivation = new DerivationBuilder(loaded, "[,_]init[1,1][1,1][1,1][1,1][1,1][,_]");

        System.out.println("\n" + derivation);
        while (derivation.canApply()) {
            System.out.println(derivation.applyNext());
        }

        while (derivation.canApplyFinal()) {
            System.out.println(derivation.applyNextFinal());
        }

    }

}
