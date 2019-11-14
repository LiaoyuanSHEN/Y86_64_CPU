package y86_64;

public class CPU {

    private final long[] registers = new long[16];
    private final boolean[] flags = new boolean[3];
    private long pc = 0;
    private int status = 0;
    private final Memory memory;

    public CPU(Memory memory) {
        this.memory = memory;
    }

}
