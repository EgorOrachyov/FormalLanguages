import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
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
