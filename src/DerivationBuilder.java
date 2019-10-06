import java.util.Optional;

public class DerivationBuilder {
    private Grammar grammar;
    private String derivation;
    protected Pair<String,String> nextRule;
    protected Pair<String,String> nextRuleFinal;

    public DerivationBuilder(Grammar grammar) {
        this.grammar = grammar;
        this.derivation = grammar.getInit();
    }

    public DerivationBuilder(Grammar grammar, String sequence) {
        this.grammar = grammar;
        this.derivation = sequence;
    }

    @Override
    public String toString() {
        return derivation;
    }

    public String getDerivationSeq() {
        return derivation;
    }

    public boolean canApplyFinal() {
        for (Pair<String,String> p : grammar.getLast()) {
            if (derivation.contains(p.first())) {
                nextRuleFinal = p;
                return true;
            }
        }

        return false;
    }

    public boolean canApply() {
        nextRule = null;

        for (Pair<String,String> p : grammar.getMain()) {
            if (derivation.contains(p.first())) {
                nextRule = p;
                return true;
            }
        }

        return false;
    }

    public String applyNextFinal() {
        return Optional
                .ofNullable(nextRuleFinal)
                .map(p -> derivation = derivation.replace(p.first(), p.second()))
                .orElse(derivation);
    }

    public String applyNext() {
        return Optional
                .ofNullable(nextRule)
                .map(p -> derivation = derivation.replace(p.first(), p.second()))
                .orElse(derivation);
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
