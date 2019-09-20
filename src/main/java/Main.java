import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        TuringMachine machine = new TuringMachineLoader().load(new FileInputStream("../PrimeNumbersUnary.txt"));
        System.out.println(machine);
        System.out.println("Total states: " + machine.getStates().size());
        System.out.println("Total transitions: " + machine.getTransitions().size());
    }
}
