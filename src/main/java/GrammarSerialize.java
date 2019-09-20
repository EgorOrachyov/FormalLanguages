import java.io.OutputStream;
import java.io.PrintWriter;

public class GrammarSerialize {

    public void serialize(OutputStream outputStream, Grammar grammar) {
        PrintWriter writer = new PrintWriter(outputStream);

        writer.println(grammar.getInit());
        writer.println();

        grammar.getFirst().forEach(p -> {
            writer.println(p.first() + " " + p.second());
        });
        writer.println();

        grammar.getMain().forEach(p -> {
            writer.println(p.first() + " " + p.second());
        });
        writer.println("");

        grammar.getLast().forEach(p -> {
            writer.println(p.first() + " " + p.second());
        });

        writer.flush();
    }

}
