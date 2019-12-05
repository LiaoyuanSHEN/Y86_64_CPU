package y86_64.cpu;

import y86_64.util.SneakyThrow;
import y86_64.util.TransportUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static y86_64.cpu.Const.*;

public class ASMCompiler {

    private ASMCompiler() {
    }

    public static void main(String[] args) throws Throwable {
        // compile
        InputOutputStream outputStream = new InputOutputStream();
        compile(ASMCompiler.class.getClassLoader().getResourceAsStream("test.asm"), outputStream);
        InputStream is = outputStream.toInputStream();
        while (is.available() > 0) {
            System.out.println(TransportUtil.readLongFromInputStream(is));
        }
        // decompile
    }

    public static void compile(InputStream is, OutputStream os) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Map<String, Long> table = new HashMap<>();
        Pattern movPattern = Pattern.compile("(\\d+)\\(([A-Z][A-Z0-1][A-Z0-9])\\)");
        Matcher matcher;
        long count = 0;
        while ((line = reader.readLine()) != null) {
            String[] codes = line.trim().split("[,; ]");
            switch (codes[0].toLowerCase()) {
                case "nop":
                    TransportUtil.writeLongToOutputStream(nop, os);
                    count++;
                    break;
                case "halt":
                    TransportUtil.writeLongToOutputStream(halt, os);
                    count++;
                    break;
                case "irmoveq":
                    TransportUtil.writeLongToOutputStream(irmoveq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
                    break;
                case "rrmoveq":
                    TransportUtil.writeLongToOutputStream(rrmoveq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
                    break;
                case "mrmoveq":
                    matcher = movPattern.matcher(codes[1]);
                    if (matcher.find()) {
                        TransportUtil.writeLongToOutputStream(mrmoveq, os);
                        TransportUtil.writeLongToOutputStream(toRegisterId(matcher.group(2)), os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(matcher.group(1)), os);
                        count += 4;
                        break;
                    }
                    throw new IllegalArgumentException("Unrecognized pattern: " + codes[1]);
                case "rmmoveq":
                    matcher = movPattern.matcher(codes[2]);
                    if (matcher.find()) {
                        TransportUtil.writeLongToOutputStream(rmmoveq, os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                        TransportUtil.writeLongToOutputStream(toRegisterId(matcher.group(2)), os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(matcher.group(1)), os);
                        count += 4;
                        break;
                    }
                    throw new IllegalArgumentException("Unrecognized pattern: " + codes[2]);
                case "addq":
                    TransportUtil.writeLongToOutputStream(addq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
                    break;
                case "subq":
                    TransportUtil.writeLongToOutputStream(subq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
                    break;
                case "andq":
                    TransportUtil.writeLongToOutputStream(andq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
                    break;
                case "xorq":
                    TransportUtil.writeLongToOutputStream(xorq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
                    break;
                case "jmp":
                    TransportUtil.writeLongToOutputStream(jmp, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "jle":
                    TransportUtil.writeLongToOutputStream(jle, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "jl":
                    TransportUtil.writeLongToOutputStream(jl, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "je":
                    TransportUtil.writeLongToOutputStream(je, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "jne":
                    TransportUtil.writeLongToOutputStream(jne, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "jge":
                    TransportUtil.writeLongToOutputStream(jge, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "jg":
                    TransportUtil.writeLongToOutputStream(jg, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "cmovle":
                    TransportUtil.writeLongToOutputStream(cmovle, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[2]), os);
                    count += 3;
                    break;
                case "cmovl":
                    TransportUtil.writeLongToOutputStream(cmovl, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[2]), os);
                    count += 3;
                    break;
                case "cmove":
                    TransportUtil.writeLongToOutputStream(cmove, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[2]), os);
                    count += 3;
                    break;
                case "cmovne":
                    TransportUtil.writeLongToOutputStream(cmovne, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[2]), os);
                    count += 3;
                    break;
                case "cmovge":
                    TransportUtil.writeLongToOutputStream(cmovge, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[2]), os);
                    count += 3;
                    break;
                case "cmovg":
                    TransportUtil.writeLongToOutputStream(cmovg, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[2]), os);
                    count += 3;
                    break;
                case "call":
                    TransportUtil.writeLongToOutputStream(call, os);
                    TransportUtil.writeLongToOutputStream(table.get(codes[1]), os);
                    count += 2;
                    break;
                case "ret":
                    TransportUtil.writeLongToOutputStream(ret, os);
                    ++count;
                    break;
                case "pushq":
                    TransportUtil.writeLongToOutputStream(pushq, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    count += 2;
                    break;
                case "popq":
                    TransportUtil.writeLongToOutputStream(popq, os);
                    TransportUtil.writeLongToOutputStream(toRegisterId(codes[1]), os);
                    count += 2;
                    break;
                default:
                    if (codes.length == 1 && codes[0].matches("^[a-z0-9A-Z*]:$") && codes[0].endsWith(":")) {
                        table.put(codes[0].replace(":", ""), count);
                    } else {
                        throw new IllegalArgumentException(line);
                    }
            }
        }
    }

    private static long toRegisterId(String registerName) {
        switch (registerName.toUpperCase()) {
            case "RAX":
                return RAX;
            case "RBX":
                return RBX;
            case "RCX":
                return RCX;
            case "RDX":
                return RDX;
            case "RSP":
                return RSP;
            case "RBP":
                return RBP;
            case "RSI":
                return RSI;
            case "RDI":
                return RDI;
            case "R08":
                return R08;
            case "R09":
                return R09;
            case "R10":
                return R10;
            case "R11":
                return R11;
            case "R12":
                return R12;
            case "R13":
                return R13;
            case "R14":
                return R14;
            case "R15":
                return R15;
            default:
                throw new IllegalArgumentException("Unsupported registerName: " + registerName);
        }
    }

    private static String toRegisterName(long registerId) {
        switch ((int) registerId) {
            case RAX:
                return "RAX";
            case RBX:
                return "RBX";
            case RCX:
                return "RCX";
            case RDX:
                return "RDX";
            case RSP:
                return "RSP";
            case RBP:
                return "RBP";
            case RSI:
                return "RSI";
            case RDI:
                return "RDI";
            case R08:
                return "R08";
            case R09:
                return "R09";
            case R10:
                return "R10";
            case R11:
                return "R11";
            case R12:
                return "R12";
            case R13:
                return "R13";
            case R14:
                return "R14";
            case R15:
                return "R15";
            default:
                throw new IllegalArgumentException("Unsupported registerId: " + registerId);
        }
    }

    public static void decompile(InputStream is, OutputStream os) throws IOException {
        int value;
        StringBuilder sb;
        long register1Id;
        long register2Id;
        long memoryAddress;
        long memoryOffset;
        Map<Long, StringBuilder> lines = new LinkedHashMap<>();
        long count = 0;
        while ((value = (int) TransportUtil.readLongFromInputStream(is)) != -1) {
            switch (value) {
                case nop:
                    sb = new StringBuilder("    ");
                    sb.append("nop");
                    sb.append("\n");
                    lines.put(count, sb);
                    count++;
                    break;
                case halt:
                    sb = new StringBuilder("    ");
                    sb.append("halt");
                    sb.append("\n");
                    lines.put(count, sb);
                    count++;
                    break;
                case irmoveq:
                    sb = new StringBuilder("    ");
                    sb.append("irmoveq ");
                    sb.append(TransportUtil.readLongFromInputStream(is));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case rrmoveq:
                    sb = new StringBuilder("    ");
                    sb.append("rrmoveq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case mrmoveq:
                    register1Id = TransportUtil.readLongFromInputStream(is);
                    register2Id = TransportUtil.readLongFromInputStream(is);
                    memoryOffset = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("mrmoveq ");
                    sb.append(memoryOffset);
                    sb.append("(");
                    sb.append(toRegisterName(register1Id));
                    sb.append("), ");
                    sb.append(toRegisterName(register2Id));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 4;
                    break;
                case rmmoveq:
                    register1Id = TransportUtil.readLongFromInputStream(is);
                    register2Id = TransportUtil.readLongFromInputStream(is);
                    memoryOffset = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("rmmoveq ");
                    sb.append(toRegisterName(register1Id));
                    sb.append(", ");
                    sb.append(memoryOffset);
                    sb.append("(");
                    sb.append(toRegisterName(register2Id));
                    sb.append(")");
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 4;
                    break;
                case addq:
                    sb = new StringBuilder("    ");
                    sb.append("addq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case subq:
                    sb = new StringBuilder("    ");
                    sb.append("subq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case andq:
                    sb = new StringBuilder("    ");
                    sb.append("andq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case xorq:
                    sb = new StringBuilder("    ");
                    sb.append("xorq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case jmp:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("jmp L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case jle:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("jle L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case jl:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("jl L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case je:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("je L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case jne:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("jne L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case jge:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("jge L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case jg:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("jg L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case cmovle:
                    sb = new StringBuilder("    ");
                    sb.append("cmovle ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case cmovl:
                    sb = new StringBuilder("    ");
                    sb.append("cmovl ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case cmove:
                    sb = new StringBuilder("    ");
                    sb.append("cmove ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case cmovne:
                    sb = new StringBuilder("    ");
                    sb.append("cmovne ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case cmovge:
                    sb = new StringBuilder("    ");
                    sb.append("cmovge ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case cmovg:
                    sb = new StringBuilder("    ");
                    sb.append("cmovg ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case call:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    lines.compute(memoryAddress, ASMCompiler::labelAppender);
                    sb = new StringBuilder("    ");
                    sb.append("call L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case ret:
                    sb = new StringBuilder("    ");
                    sb.append("ret");
                    sb.append("\n");
                    lines.put(count, sb);
                    count++;
                    break;
                case pushq:
                    sb = new StringBuilder("    ");
                    sb.append("pushq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
                case popq:
                    sb = new StringBuilder("    ");
                    sb.append("popq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 2;
                    break;
            }
        }
        os.write("L0:\n".getBytes());
        lines.forEach((pos, stringBuilder) -> {
            try {
                os.write(stringBuilder.toString().getBytes());
            } catch (IOException e) {
                SneakyThrow.sneakyThrow(e);
            }
        });
    }

    private static StringBuilder labelAppender(Long pos, StringBuilder sb) {
        if (sb == null) {
            throw new IllegalArgumentException("Cannot find code with index: " + pos);
        }
        String label = "L" + pos + ":\n";
        if (!sb.toString().startsWith(label)) {
            sb.insert(0, label);
        }
        return sb;
    }

}

class InputOutputStream extends OutputStream {

    private final List<Integer> list = new LinkedList<>();

    @Override
    public void write(int b) throws IOException {
        list.add(b);
    }

    public InputStream toInputStream() {
        return new InputStream() {

            private Iterator<Integer> iterator = list.iterator();
            private int length = list.size();
            private int index = 1;

            @Override
            public int read() throws IOException {
                ++index;
                return iterator.next();
            }

            @Override
            public int available() throws IOException {
                return length - index;
            }
        };
    }

}
