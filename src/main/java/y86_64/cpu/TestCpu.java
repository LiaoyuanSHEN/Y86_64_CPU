package y86_64.cpu;

import y86_64.CPU;
import y86_64.Memory;
import y86_64.exceptions.MemoryException;

import java.io.IOException;

public class TestCpu {
    public static void main(String[] args) throws Throwable {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        CPU cpu = new CPUImpl(new MemoryImpl("sum.asm"));
        while (true) {
            cpu.compute();
        }
//        Pattern movPattern = Pattern.compile("(\\d*)\\((%[A-Za-z][A-Za-z0-1][A-Za-z0-9])\\)");
//        Matcher matcher = movPattern.matcher("(%rbx)");
//        System.out.println("MOVES_123:".matches("^[_a-z0-9A-Z]*:$"));
//        if (matcher.find()) {
//            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
//        }
    }

    private static class MemoryImpl implements Memory {

        private byte[] memory = new byte[1024];

        MemoryImpl(String path) throws IOException {
            OutputToInputStream ios = new OutputToInputStream();
            ASMCompiler.compile(MemoryImpl.class.getClassLoader().getResourceAsStream(path), ios);
            ios.toInputStream().read(memory);
        }

        @Override
        public byte readByte(long address) throws MemoryException {
            return memory[(int) address];
        }

        @Override
        public void writeByte(long address, byte value) throws MemoryException {
            memory[(int) address] = value;
        }

        @Override
        public void stop() throws IOException {

        }
    }

}
