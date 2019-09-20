import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        TuringMachine machine = new TuringMachineLoader().load(new FileInputStream("PrimeNumsMT.txt"));
        System.out.println(machine.getGamma());
        System.out.println(machine.getAccept());
        System.out.println(machine.getStates().size());
        System.out.println(machine.getTransitions().size());

        Grammar grammar = new TuringMachineToGrammar().convert(machine);
        System.out.println(grammar.getFirst().size());
        System.out.println(grammar.getMain().size());
        System.out.println(grammar.getLast().size());

        new GrammarSerialize().serialize(new FileOutputStream("PrimeNumsT0.txt"), grammar);

        Grammar loaded = new GrammarLoader().load(new FileInputStream("PrimeNumsT0.txt"));
        System.out.println(loaded.getFirst().size());
        System.out.println(loaded.getMain().size());
        System.out.println(loaded.getLast().size());
    }
}
