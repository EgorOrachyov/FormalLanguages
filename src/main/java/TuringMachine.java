import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TuringMachine {

    private String name;
    private String description;
    private String init;
    private Set<String> accept;
    private Map<Context,Transition> transitions;
    private Set<String> states;
    private Set<String> gamma;

    public TuringMachine(String name, String description, String init, Set<String> accept, Map<Context,Transition> transitions) {
        this.name = name;
        this.description = description;
        this.init = init;
        this.accept = accept;
        this.transitions = transitions;
        computeGamma();
        computeStates();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getInit() {
        return init;
    }

    public Set<String> getAccept() {
        return accept;
    }

    public Map<Context, Transition> getTransitions() {
        return transitions;
    }

    public Set<String> getGamma() {
        return gamma;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<Context> getContext(String state) {
        Set<Context> res = new HashSet<>();
        transitions.forEach((context, transition) -> {
            if (context.getName().equals(state)) {
                res.add(context);
            }
        });
        return res;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("name: ").append(name).append('\n');
        builder.append("description: ").append(description).append('\n');
        builder.append("init: ").append(init).append('\n');

        builder.append("accept: ");

        accept.forEach(string -> {
            builder.append(string).append(",");
        });

        builder.deleteCharAt(builder.length() - 1);
        builder.append('\n');

        transitions.forEach((context, transition) -> {
            builder.append(context.name)
                    .append(',')
                    .append(context.symbol)
                    .append(',')
                    .append(transition.name)
                    .append(',')
                    .append(transition.symbol)
                    .append(',')
                    .append(transition.direction)
                    .append('\n');
        });

        return builder.toString();
    }

    private void computeStates() {
        states = new HashSet<>();
        transitions.forEach((context, transition) -> {
            states.add(context.name);
            states.add(transition.name);
        });
    }

    private void computeGamma() {
        gamma = new HashSet<>();
        transitions.forEach((context, transition) -> {
            gamma.add(context.symbol);
            gamma.add(transition.symbol);
        });
    }

    public static enum Direction {
        Left("<"),
        Right(">"),
        Stay("-");

        private String symbol;

        Direction(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public static Direction fromString(String in) {
            for (Direction d : Direction.values()) {
                if (d.symbol.equals(in))
                    return d;
            }

            throw new RuntimeException("Invalid input: " + in);
        }
    }

    public static class Context {
        private String name;
        private String symbol;

        public Context(String name, String symbol) {
            this.name = name;
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return name + "," + symbol;
        }

        @Override
        public int hashCode() {
            return (name.hashCode() + symbol.hashCode()) % Integer.MAX_VALUE;
        }

        @Override
        public boolean equals(Object obj) {
            Context c = (Context) obj;
            return c.getSymbol().equals(symbol) && c.getName().equals(name);
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public static class Transition {
        private String name;
        private String symbol;
        private Direction direction;

        public Transition(String name, String symbol, Direction direction) {
            this.name = name;
            this.symbol = symbol;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return name + "," + symbol + "," + direction;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        public Direction getDirection() {
            return direction;
        }
    }

}
