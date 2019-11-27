package y86_64.cpu;

import y86_64.CPU;
import y86_64.Memory;
import y86_64.exceptions.CpuException;
import y86_64.exceptions.MemoryException;

import java.io.IOException;

import static y86_64.cpu.Const.*;

public class CPUImpl implements CPU {

    private final long[] registers = new long[16];
    private final boolean[] flags = new boolean[3];
    private long pc = 0;
    private int status = READY_CODE;
    private final Memory memory;

    public CPUImpl(Memory memory) {
        this.memory = memory;
    }

    @Override
    public void compute() throws CpuException {
        int operationCode = (int) readNext();
        int registerIndex1;
        int registerIndex2;
        long memoryOffset;
        long memoryAddress;
        switch (operationCode) {
            case nop:
                break;
            case halt:
                status = HALT_CODE;
                break;
            case irmoveq:
                registerIndex1 = (int) readNext();
                long value = readNext();
                registers[registerIndex1] = value;
                break;
            case rrmoveq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                registers[registerIndex2] = registerIndex1;
                break;
            case mrmoveq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                memoryOffset = readNext();
                memoryAddress = registers[registerIndex2] + memoryOffset;
                registers[registerIndex1] = readFromMemory(memoryAddress);
                break;
            case rmmoveq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                memoryOffset = readNext();
                memoryAddress = registers[registerIndex1] + memoryOffset;
                writeToMemory(memoryAddress, registers[registerIndex2]);
                break;
            default:
                throw new CpuException("Unknown operation code: " + operationCode);
        }
    }

    @Override
    public void interrupt(long code) throws CpuException {

    }

    @Override
    public void stop() throws IOException {
        // do nothing
    }

    private void writeToMemory(long address, long value) {
        try {
            memory.write(address, value);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

    private long readFromMemory(long address) {
        try {
            return memory.read(address);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

    private long readNext() {
        try {
            return memory.read(pc++);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

}
