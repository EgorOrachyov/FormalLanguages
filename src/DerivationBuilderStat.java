import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;

public class DerivationBuilderStat extends DerivationBuilder {

    private Set<Pair<String,String>> usedRules = new LinkedHashSet<>();

    public DerivationBuilderStat(Grammar grammar) {
        super(grammar);
    }

    public DerivationBuilderStat(Grammar grammar, String sequence) {
        super(grammar, sequence);
    }

    public Set<Pair<String, String>> getUsedRules() {
        return usedRules;
    }

    @Override
    public String applyNext() {
        usedRules.add(nextRule);
        return super.applyNext();
    }

    @Override
    public String applyNextFinal() {
        usedRules.add(nextRuleFinal);
        return super.applyNextFinal();
    }

    public void serialize(OutputStream stream) {
        PrintWriter writer = new PrintWriter(stream);

        usedRules.forEach(p -> {
            writer.println(p.first() + " " + p.second());
        });

        writer.flush();
    }
}
