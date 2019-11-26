package y86_64.cpu;

import y86_64.CPU;
import y86_64.Memory;
import y86_64.exceptions.CpuException;

import java.io.IOException;

public class CPUImpl implements CPU {

    private final long[] registers = new long[16];
    private final boolean[] flags = new boolean[3];
    private long pc = 0;
    private int status = 0;
    private final Memory memory;

    public CPUImpl(Memory memory) {
        this.memory = memory;
    }

    @Override
    public void compute() throws CpuException {

    }

    @Override
    public void interrupt(long code) throws CpuException {

    }

    @Override
    public void stop() throws IOException {
        // do nothing
    }
}
