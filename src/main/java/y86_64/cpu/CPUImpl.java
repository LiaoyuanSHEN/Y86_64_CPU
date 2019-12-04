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
        long value;
        switch (operationCode) {
            case nop:
                break;
            case halt:
                status = HALT_CODE;
                break;
            case irmoveq:
                value = readNext();
                registerIndex1 = (int) readNext();
                registers[registerIndex1] = value;
                break;
            case rrmoveq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                registers[registerIndex2] = registers[registerIndex1];
                break;
            case mrmoveq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                memoryOffset = readNext();
                memoryAddress = registers[registerIndex1] + memoryOffset;
                registers[registerIndex2] = readFromMemory(memoryAddress);
                break;
            case rmmoveq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                memoryOffset = readNext();
                memoryAddress = registers[registerIndex2] + memoryOffset;
                writeToMemory(memoryAddress, registers[registerIndex1]);
                break;
            case addq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                value = registers[registerIndex1] + registers[registerIndex2];
                setAddOPFlags(registers[registerIndex1], registers[registerIndex2], value);
                registers[registerIndex1] = value;
                break;
            case subq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                value = registers[registerIndex1] - registers[registerIndex2];
                setAddOPFlags(registers[registerIndex1], -registers[registerIndex2], value);
                registers[registerIndex1] = value;
                break;
            case andq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                value = registers[registerIndex1] & registers[registerIndex2];
                flags[ZF] = value == 0;
                flags[SF] = value < 0;
                flags[OF] = false;
                registers[registerIndex1] = value;
                break;
            case xorq:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                value = registers[registerIndex1] ^ registers[registerIndex2];
                flags[ZF] = value == 0;
                flags[SF] = value < 0;
                flags[OF] = false;
                registers[registerIndex1] = value;
                break;
            case jmp:
                memoryAddress = readNext();
                pc = memoryAddress;
                break;
            case jle:
                memoryAddress = readNext();
                if (flags[ZF] || flags[SF] != flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case jl:
                memoryAddress = readNext();
                if (!flags[ZF] && flags[SF] != flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case je:
                memoryAddress = readNext();
                if (flags[ZF]) {
                    pc = memoryAddress;
                }
                break;
            case jne:
                memoryAddress = readNext();
                if (!flags[ZF]) {
                    pc = memoryAddress;
                }
                break;
            case jge:
                memoryAddress = readNext();
                if (flags[ZF] || flags[SF] == flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case jg:
                memoryAddress = readNext();
                if (!flags[ZF] || flags[SF] == flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case cmovle:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                if (flags[ZF] || flags[SF] != flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovl:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                if (!flags[ZF] && flags[SF] != flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmove:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                if (flags[ZF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovne:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                if (!flags[ZF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovge:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                if (flags[ZF] || flags[SF] == flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovg:
                registerIndex1 = (int) readNext();
                registerIndex2 = (int) readNext();
                if (!flags[ZF] || flags[SF] == flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case call:
                registerIndex1 = (int) readNext();
                writeToMemory(registers[RSP]++, pc);
                pc = registerIndex1;
                break;
            case ret:
                pc = readFromMemory(--registers[RSP]);
                break;
            case pushq:
                registerIndex1 = (int) readNext();
                writeToMemory(registers[RSP]++, registers[registerIndex1]);
                break;
            case popq:
                registerIndex1 = (int) readNext();
                registers[registerIndex1] = readFromMemory(--registers[RSP]);
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

    private void setAddOPFlags(long register1Value, long register2Value, long value) {
        flags[ZF] = value == 0;
        flags[SF] = value < 0;
        flags[OF] = (register1Value < 0 && register2Value < 0 && value > 0) || (register1Value > 0 && register2Value > 0 && value < 0);
    }

}
