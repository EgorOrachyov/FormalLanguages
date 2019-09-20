public class Pair<A,B> {
    private A a;
    private B b;

    public Pair(A first, B second) {
        a = first;
        b = second;
    }

    public A first() {
        return a;
    }

    public B second() {
        return b;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + "," + b.toString() + ")";
    }
}
