package y86_64.cpu;

import y86_64.CPU;
import y86_64.Memory;
import y86_64.exceptions.CpuException;
import y86_64.exceptions.MemoryException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static y86_64.cpu.Const.*;

public class TestCpu {
    public static void main(String[] args) throws CpuException {
//        CPU cpu = new CPUImpl(new MemoryImpl());
//        while (true) {
//            cpu.compute();
//        }
        Pattern movPattern = Pattern.compile("(\\d+)\\(([A-Z][A-Z0-1][A-Z0-9])\\)");
        Matcher matcher = movPattern.matcher("12(R08)");
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }

    private static class MemoryImpl implements Memory {

        private long[] memory = new long[] {
                // addq test
                irmoveq, 1, R08,
                irmoveq, 2, R09,
                addq, R08, R09,
                irmoveq, 0, R10,
                rmmoveq, R08, R10, 0,
                // subq test
                irmoveq, 1, R08,
                irmoveq, 2, R09,
                subq, R09, R08,
                irmoveq, 0, R10,
                rmmoveq, R09, R10, 0,

    };

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
