import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Application {

    private PrintWriter out = new PrintWriter(System.out);

    private void parse(String[] args) {
        if (args.length == 0)
            return;

        final String cmd = args[0];

        try {
            if (cmd.equals("-help")) {
                help();
            } else if (cmd.equals("-t0")) {
                t0(args[1], args[2]);
            } else if (cmd.equals("-t1")) {
                t1(args[1], args[2]);
            } else if (cmd.equals("-p0")) {
                int num = Integer.valueOf(args[2]);
                p(args[1], num, num);
            } else if (cmd.equals("-p1")) {
                int num = Integer.valueOf(args[2]);
                p(args[1], num, num - 2);
            } else if (cmd.equals("-d0")) {
                int count = Integer.valueOf(args[2]);
                d(args[1], count);
            } else if (cmd.equals("-d1")) {
                int count = Integer.valueOf(args[2]);
                d(args[1], count);
            } else {
                throw new Exception("Invalid command arg: " + args[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.flush();
    }

    private void help() {
        String helpText =
                "List available commands for Grammar/Automata workflow\n" +
                "(NOTE: works only for MT and LBA, stored in repo because\n" +
                "of internal grammar generation optimizations):\n" +
                "-help\n" +
                "       Prints help text for the app\n" +
                "-t0 <fileTM> <fileT0>\n" +
                "       Generates t0 grammar from Turing Machine\n" +
                "       description, provided in file fileTM\n" +
                "       and saves result in file fileT0\n" +
                "-t1 <fileLBA> <fileT1>\n" +
                "       Generates t1 grammar from Linear Bounded Machine\n" +
                "       description, provided in file fileLBA\n" +
                "       and saves result in file fileT1\n" +
                "-d0 <fileT0> <count>\n" +
                "       Build derivation of the first count\n" +
                "       prime numbers in t0 grammar from file fileT0\n" +
                "-d1 <fileT1> <count>\n" +
                "       Build derivation of the first count\n" +
                "       prime numbers in t1 grammar from file fileT1\n" +
                "-p0 <fileT0> <num>\n" +
                "       Checks whether num is prime in grammar T0\n" +
                "-p1 <fileT1> <num>\n" +
                "       Checks whether num is prime in grammar T1\n";
        out.println(helpText);
    }

    private void t0(String inFile, String outFile) throws FileNotFoundException {
        Tm tm = new TmLoader().load(new FileInputStream(inFile));

        Set<String> genVars = new HashSet<>();
        genVars.add("[,_]");
        genVars.add("[1,1]");
        genVars.add("[1,#]");

        Set<String> sigma = new HashSet<>();
        sigma.add("");
        sigma.add("1");

        Grammar g = new TmToGrammar().convert(tm, sigma, genVars);
        new GrammarSerialize().serialize(new FileOutputStream(outFile), g);
    }

    private void t1(String inFile, String outFile) throws FileNotFoundException {
        Tm tm = new TmLoader().load(new FileInputStream(inFile));

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

        Grammar g = new LbaToGrammar(tm, gamma, statesL, statesR).convert();
        new GrammarSerialize().serialize(new FileOutputStream(outFile), g);
    }

    private void p(String inFile, int num, int ref) throws Exception {
        if (num < 0)
            throw new Exception("Invalid input:" + num);

        Grammar g = new GrammarLoader().load(new FileInputStream(inFile));
        DerivationBuilder builder = new DerivationBuilder(g);
        List<Pair<String, String>> all = builder.getAll("A2");

        builder.canApply();
        out.println(builder.applyNext());

        while (ref > 0) {
            out.println(builder.applyRule(all.get(0)));
            ref -= 1;
        }

        out.println(builder.applyRule(all.get(1)));

        while (builder.canApply()) {
            out.println(builder.applyNext());
        }

        while (builder.canApplyFinal()) {
            out.println(builder.applyNextFinal());
        }

        if (builder.getDerivationSeq().length() != num) {
            out.println("Is not a prime number");
        }
    }

    private void d(String inFile, int count) throws Exception {
        List<String> list = new LinkedList<>();

        int derived = 0;
        int currentNum = 0;

        Grammar g = new GrammarLoader().load(new FileInputStream(inFile));

        while (derived < count) {
            DerivationBuilder builder = new DerivationBuilder(g);
            List<Pair<String, String>> all = builder.getAll("A2");

            builder.canApply();
            list.add(builder.applyNext());

            for (int i = 0; i < currentNum; i++) {
                list.add(builder.applyRule(all.get(0)));
            }

            list.add(builder.applyRule(all.get(1)));

            while (builder.canApply()) {
                list.add(builder.applyNext());
            }

            while (builder.canApplyFinal()) {
                list.add(builder.applyNextFinal());
            }

            if (containsOnly(builder.getDerivationSeq(), '1')) {
                derived += 1;
                out.println(builder + " is prime");
                out.println("Derivation:");
                list.forEach(s -> out.println(s));
                out.println();
            }

            currentNum += 1;
            list.clear();
        }
    }

    private boolean containsOnly(String s, char c) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.parse(args);
    }

}
