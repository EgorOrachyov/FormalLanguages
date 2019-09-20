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
}
