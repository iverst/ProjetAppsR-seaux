package servers;

public class Counter {
    private static final Counter instance = new Counter();
    private int value = 0;

    public static Counter getInstance() {
        return instance;
    }

    public synchronized int getAndIncrement() {
        int oldValue = value;
        value++;
        return oldValue;
    }
}
