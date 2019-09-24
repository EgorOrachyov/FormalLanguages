import java.util.Optional;

public class DerivationBuilder {
    private Grammar grammar;
    private String derivation;

    public DerivationBuilder(Grammar grammar) {
        this.grammar = grammar;
        this.derivation = grammar.getInit();
    }

    public String getDerivationSeq() {
        return derivation;
    }

    public String applyRule(String rule) {
        return Optional
                .ofNullable(getRule(rule))
                .map(p -> derivation = derivation.replace(p.first(), p.second()))
                .orElse(derivation);
    }

    private Pair<String,String> getRule(String rule) {
        for (Pair<String,String> p : grammar.getMain()) {
            if (p.first().equals(rule)) {
                return p;
            }
        }

        for (Pair<String,String> p : grammar.getLast()) {
            if (p.first().equals(rule)) {
                return p;
            }
        }

        for (Pair<String,String> p : grammar.getFirst()) {
            if (p.first().equals(rule)) {
                return p;
            }
        }

        return null;
    }

}
