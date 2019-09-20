import java.io.InputStream;
import java.util.*;

public class TuringMachineLoader {

    public TuringMachine load(InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        String name = "";
        String description = "";
        String init = "";
        Set<String> accept = new HashSet<String>();
        Map<TuringMachine.Context, TuringMachine.Transition> transitions = new HashMap<TuringMachine.Context, TuringMachine.Transition>();

        while (in.hasNextLine()) {

            String line = in.nextLine();

            if (line.contains("//")) {
                continue;
            } else if (line.startsWith("name:")) {
                name = line
                        .replaceFirst("name: ", "");
            } else if (line.startsWith("init:")) {
                init = line
                        .replaceFirst("init: ", "");
            } else if (line.startsWith("accept:")) {
                String[] split = line
                        .replaceFirst("accept: ", "")
                        .split(" ,");

                Collections.addAll(accept, split);
            } else if (!line.startsWith(" ") && (line.length() != 0)) {
                String[] l1 = line.split(",");
                String[] l2 = in.nextLine().split(",");

                transitions.put(new TuringMachine.Context(l1[0], l1[1]),
                        new TuringMachine.Transition(l2[0], l2[1], TuringMachine.Direction.fromString(l2[2])));
            }
        }

        return new TuringMachine(name, "", init, accept, transitions);
    }

}
