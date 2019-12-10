package y86_64.cpu;

import y86_64.CPU;
import y86_64.Memory;
import y86_64.exceptions.CpuException;
import y86_64.exceptions.MemoryException;
import y86_64.util.TranslateUtil;

import java.io.IOException;

import static y86_64.cpu.Const.*;

public class CPUImpl implements CPU {

    private final long[] registers = new long[16];
    private final boolean[] flags = new boolean[3];
    private final Memory memory;
    private long pc = 0;
    private int status = READY_CODE;

    public CPUImpl(Memory memory) {
        this.memory = memory;
    }

    @Override
    public void compute() throws CpuException {
        if (status == HALT_CODE) {
            throw new CpuException("CPU status is halt.");
        }
        int operationCode = readNextByte();
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
            case irmovq:
                value = readNextLong();
                registerIndex1 = readNextByte();
                registers[registerIndex1] = value;
                break;
            case rrmovq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                registers[registerIndex2] = registers[registerIndex1];
                break;
            case mrmovq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                memoryOffset = readNextLong();
                memoryAddress = registers[registerIndex1] + memoryOffset;
                registers[registerIndex2] = readLongFromMemory(memoryAddress);
                break;
            case rmmovq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                memoryOffset = readNextLong();
                memoryAddress = registers[registerIndex2] + memoryOffset;
                writeLongToMemory(memoryAddress, registers[registerIndex1]);
                break;
            case addq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                value = registers[registerIndex1] + registers[registerIndex2];
                flags[ZF] = value == 0;
                flags[SF] = value < 0;
                flags[OF] = (registers[registerIndex1] < 0 && registers[registerIndex2] < 0 && value > 0)
                        || (registers[registerIndex1] > 0 && registers[registerIndex2] > 0 && value < 0);
                registers[registerIndex2] = value;
                break;
            case subq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                value = registers[registerIndex2] - registers[registerIndex1];
                flags[ZF] = value == 0;
                flags[SF] = value < 0;
                flags[OF] = (registers[registerIndex1] < 0 && registers[registerIndex2] > 0 && value < 0)
                        || (registers[registerIndex1] > 0 && registers[registerIndex2] < 0 && value > 0);
                registers[registerIndex2] = value;
                break;
            case andq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                value = registers[registerIndex1] & registers[registerIndex2];
                flags[ZF] = value == 0;
                flags[SF] = value < 0;
                flags[OF] = false;
                registers[registerIndex2] = value;
                break;
            case xorq:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                value = registers[registerIndex1] ^ registers[registerIndex2];
                flags[ZF] = value == 0;
                flags[SF] = value < 0;
                flags[OF] = false;
                registers[registerIndex2] = value;
                break;
            case jmp:
                memoryAddress = readNextLong();
                pc = memoryAddress;
                break;
            case jle:
                memoryAddress = readNextLong();
                if (flags[ZF] || flags[SF] != flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case jl:
                memoryAddress = readNextLong();
                if (!flags[ZF] && flags[SF] != flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case je:
                memoryAddress = readNextLong();
                if (flags[ZF]) {
                    pc = memoryAddress;
                }
                break;
            case jne:
                memoryAddress = readNextLong();
                if (!flags[ZF]) {
                    pc = memoryAddress;
                }
                break;
            case jge:
                memoryAddress = readNextLong();
                if (flags[ZF] || flags[SF] == flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case jg:
                memoryAddress = readNextLong();
                if (!flags[ZF] || flags[SF] == flags[OF]) {
                    pc = memoryAddress;
                }
                break;
            case cmovle:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                if (flags[ZF] || flags[SF] != flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovl:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                if (!flags[ZF] && flags[SF] != flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmove:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                if (flags[ZF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovne:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                if (!flags[ZF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovge:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                if (flags[ZF] || flags[SF] == flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case cmovg:
                registerIndex1 = readNextByte();
                registerIndex2 = readNextByte();
                if (!flags[ZF] || flags[SF] == flags[OF]) {
                    registers[registerIndex2] = registers[registerIndex1];
                }
                break;
            case call:
                memoryAddress = readNextLong();
                writeLongToMemory(registers[RSP], pc);
                registers[RSP] -= 8;
                pc = memoryAddress;
                break;
            case ret:
                registers[RSP] += 8;
                pc = readLongFromMemory(registers[RSP]);
                break;
            case pushq:
                registerIndex1 = readNextByte();
                writeLongToMemory(registers[RSP], registers[registerIndex1]);
                registers[RSP] -= 8;
                break;
            case popq:
                registerIndex1 = readNextByte();
                registers[RSP] += 8;
                registers[registerIndex1] = readLongFromMemory(registers[RSP]);
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

    private void writeLongToMemory(long address, long value) {
        try {
            byte[] arr = TranslateUtil.toLongByteArray(value);
            memory.writeByte(address, arr[0]);
            memory.writeByte(address + 1, arr[1]);
            memory.writeByte(address + 2, arr[2]);
            memory.writeByte(address + 3, arr[3]);
            memory.writeByte(address + 4, arr[4]);
            memory.writeByte(address + 5, arr[5]);
            memory.writeByte(address + 6, arr[6]);
            memory.writeByte(address + 7, arr[7]);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte readByteFromMemory(long address) {
        try {
            return memory.readByte(address);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

    private long readLongFromMemory(long address) {
        try {
            byte[] arr = new byte[8];
            arr[0] = memory.readByte(address);
            arr[1] = memory.readByte(address + 1);
            arr[2] = memory.readByte(address + 2);
            arr[3] = memory.readByte(address + 3);
            arr[4] = memory.readByte(address + 4);
            arr[5] = memory.readByte(address + 5);
            arr[6] = memory.readByte(address + 6);
            arr[7] = memory.readByte(address + 7);
            return TranslateUtil.fromLongByteArray(arr);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte readNextByte() {
        try {
            return memory.readByte(pc++);
        } catch (MemoryException e) {
            throw new IllegalStateException(e);
        }
    }

    private long readNextLong() {
        long value = readLongFromMemory(pc);
        pc += 8;
        return value;
    }

}
