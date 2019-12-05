package y86_64.cpu;

import y86_64.CPU;
import y86_64.Memory;
import y86_64.exceptions.CpuException;
import y86_64.exceptions.MemoryException;
import y86_64.util.TransportUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static y86_64.cpu.Const.*;

public class TestCpu {
    public static void main(String[] args) throws Throwable {
        CPU cpu = new CPUImpl(new MemoryImpl("test.asm"));
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

        private long[] memory = new long[1024 * 1024 * 64];

        MemoryImpl(String path) throws IOException {
            InputOutputStream ios = new InputOutputStream();
            ASMCompiler.compile(MemoryImpl.class.getClassLoader().getResourceAsStream(path), ios);
            long value;
            int count = 0;
            while ((value = TransportUtil.readLongFromInputStream(ios.toInputStream())) != -1) {
                memory[count++] = value;
            }
        }

        @Override
        public long read(long address) throws MemoryException {
            return memory[(int) address];
        }

        @Override
        public void write(long address, long value) throws MemoryException {
            memory[(int) address] = value;
        }

        @Override
        public void stop() throws IOException {

        }
    }

}
