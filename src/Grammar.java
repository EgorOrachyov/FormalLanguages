import java.util.List;

public class Grammar {
    private String init;
    private List<Pair<String,String>> first;
    private List<Pair<String,String>> main;
    private List<Pair<String,String>> last;

    public Grammar(String init, List<Pair<String,String>> first, List<Pair<String,String>> main, List<Pair<String,String>> last) {
        this.init = init;
        this.first = first;
        this.main = main;
        this.last = last;
    }

    public String getInit() {
        return init;
    }

    public List<Pair<String, String>> getFirst() {
        return first;
    }

    public List<Pair<String, String>> getMain() {
        return main;
    }

    public List<Pair<String, String>> getLast() {
        return last;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Start").append(init).append("\n");
        listInfo("First", builder, first);
        listInfo("Main", builder, main);
        listInfo("Last", builder, last);

        return builder.toString();
    }

    private void listInfo(String info, StringBuilder builder, List<Pair<String, String>> list) {
        builder.append(info)
                .append("\n");
        list.forEach(s -> {
            builder.append(s.first())
                    .append(" -> ")
                    .append(s.second())
                    .append(" ;\n");
        });
    }
}
