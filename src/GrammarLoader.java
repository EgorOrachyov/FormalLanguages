import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GrammarLoader {

    public Grammar load(InputStream inputStream) {
        Scanner in = new Scanner(inputStream);

        List<Pair<String,String>> first = new ArrayList<>();
        List<Pair<String,String>> main = new ArrayList<>();
        List<Pair<String,String>> last = new ArrayList<>();

        String init = in.nextLine();
        in.nextLine();

        parse(first, in);
        parse(main, in);
        parse(last, in);

        return new Grammar(init, first, main, last);
    }

    private void parse(List<Pair<String,String>> list, Scanner in) {
        while (in.hasNextLine()) {
            String l = in.nextLine();

            if (l.equals("")) return;

            String[] product = l.split(" -> ");
            if (product.length == 2) {
                list.add(new Pair<>(product[0], product[1]));
            } else {
                list.add(new Pair<>(product[0], ""));
            }
        }
    }

}
