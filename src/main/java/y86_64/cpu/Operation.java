package y86_64.cpu;

public abstract class Operation {

    public abstract long getId();

    public abstract String getName();

    public static long toId(String name) {
        return -1;
    }

    public static String toName(long id) {
        return null;
    }
}
