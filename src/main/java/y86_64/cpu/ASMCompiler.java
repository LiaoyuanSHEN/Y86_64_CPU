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
        OutputToInputStream outputStream1 = new OutputToInputStream();
        OutputToInputStream outputStream2 = new OutputToInputStream();
        compile(ASMCompiler.class.getClassLoader().getResourceAsStream("test.asm"), outputStream1);
        decompile(outputStream1.toInputStream(), outputStream2);
        outputStream1.reset();
        compile(outputStream2.toInputStream(), outputStream1);
        decompile(outputStream1.toInputStream(), System.out);
    }

    public static void compile(InputStream is, OutputStream os) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Long> codeArray = new ArrayList<>();
        Map<String, List<Integer>> labelCallTable = new HashMap<>();
        Map<String, Integer> labelDefTable = new HashMap<>();
        Pattern movPattern = Pattern.compile("(\\d*)\\((%[A-Za-z][A-Za-z0-1][A-Za-z0-9])\\)");
        Matcher matcher;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] codes = Arrays.stream(line.split("[\t, ]")).filter(s -> !s.isEmpty()).toArray(String[]::new);
            switch (codes[0].toLowerCase()) {
                case "nop":
                    codeArray.add((long) nop);
                    count++;
                    break;
                case "halt":
                    codeArray.add((long) halt);
                    count++;
                    break;
                case "irmovq":
                    codeArray.add((long) irmovq);
                    codeArray.add(Long.parseLong(codes[1].replace("$", "")));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "rrmovq":
                    codeArray.add((long) rrmovq);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "mrmovq":
                    matcher = movPattern.matcher(codes[1]);
                    if (matcher.find()) {
                        codeArray.add((long) mrmovq);
                        codeArray.add(toRegisterId(matcher.group(2)));
                        codeArray.add(toRegisterId(codes[2]));
                        if (matcher.group(1).isEmpty()) {
                            codeArray.add(0L);
                        } else {
                            codeArray.add(Long.parseLong(matcher.group(1)));
                        }
                        count += 4;
                        break;
                    }
                    throw new IllegalArgumentException("Unrecognized pattern: " + codes[1]);
                case "rmmovq":
                    matcher = movPattern.matcher(codes[2]);
                    if (matcher.find()) {
                        codeArray.add((long) rmmovq);
                        codeArray.add(toRegisterId(codes[1]));
                        codeArray.add(toRegisterId(matcher.group(2)));
                        if (matcher.group(1).isEmpty()) {
                            codeArray.add(0L);
                        } else {
                            codeArray.add(Long.parseLong(matcher.group(1)));
                        }
                        count += 4;
                        break;
                    }
                    throw new IllegalArgumentException("Unrecognized pattern: " + codes[2]);
                case "addq":
                    codeArray.add((long) addq);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "subq":
                    codeArray.add((long) subq);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "andq":
                    codeArray.add((long) andq);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "xorq":
                    codeArray.add((long) xorq);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "jmp":
                    codeArray.add((long) jmp);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "jle":
                    codeArray.add((long) jle);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "jl":
                    codeArray.add((long) jl);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "je":
                    codeArray.add((long) je);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "jne":
                    codeArray.add((long) jne);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "jge":
                    codeArray.add((long) jge);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "jg":
                    codeArray.add((long) jg);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "cmovle":
                    codeArray.add((long) cmovle);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "cmovl":
                    codeArray.add((long) cmovl);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "cmove":
                    codeArray.add((long) cmove);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "cmovne":
                    codeArray.add((long) cmovne);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "cmovge":
                    codeArray.add((long) cmovge);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "cmovg":
                    codeArray.add((long) cmovg);
                    codeArray.add(toRegisterId(codes[1]));
                    codeArray.add(toRegisterId(codes[2]));
                    count += 3;
                    break;
                case "call":
                    codeArray.add((long) call);
                    codeArray.add(0L);
                    labelCallTable.computeIfAbsent(codes[1], k -> new LinkedList<>());
                    labelCallTable.get(codes[1]).add(count + 1);
                    count += 2;
                    break;
                case "ret":
                    codeArray.add((long) ret);
                    ++count;
                    break;
                case "pushq":
                    codeArray.add((long) pushq);
                    codeArray.add(toRegisterId(codes[1]));
                    count += 2;
                    break;
                case "popq":
                    codeArray.add((long) popq);
                    codeArray.add(toRegisterId(codes[1]));
                    count += 2;
                    break;
                default:
                    if (codes.length == 1 && codes[0].matches("^[_a-z0-9A-Z]*:$")) {
                        labelDefTable.put(codes[0].replace(":", ""), count);
                    } else {
                        throw new IllegalArgumentException(line);
                    }
            }
        }

        labelDefTable.forEach((label, codeIndex) -> {
            if (labelCallTable.containsKey(label)) {
                labelCallTable
                        .get(label)
                        .forEach(index ->
                                codeArray.set(index, (long) codeIndex));
            }
        });

        codeArray.forEach(code -> {
            try {
                TransportUtil.writeLongToOutputStream(code, os);
            } catch (IOException e) {
                SneakyThrow.sneakyThrow(e);
            }
        });
    }

    public static void decompile(InputStream is, OutputStream os) throws IOException {
        int value;
        StringBuilder sb;
        long register1Id;
        long register2Id;
        long memoryAddress;
        long memoryOffset;
        Map<Long, StringBuilder> lines = new LinkedHashMap<>();
        Set<Long> labels = new HashSet<>();
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
                case irmovq:
                    sb = new StringBuilder("    ");
                    sb.append("irmovq $");
                    sb.append(TransportUtil.readLongFromInputStream(is));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case rrmovq:
                    sb = new StringBuilder("    ");
                    sb.append("rrmovq ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append(", ");
                    sb.append(toRegisterName(TransportUtil.readLongFromInputStream(is)));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 3;
                    break;
                case mrmovq:
                    register1Id = TransportUtil.readLongFromInputStream(is);
                    register2Id = TransportUtil.readLongFromInputStream(is);
                    memoryOffset = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("mrmovq ");
                    if (memoryOffset != 0) {
                        sb.append(memoryOffset);
                    }
                    sb.append("(");
                    sb.append(toRegisterName(register1Id));
                    sb.append("), ");
                    sb.append(toRegisterName(register2Id));
                    sb.append("\n");
                    lines.put(count, sb);
                    count += 4;
                    break;
                case rmmovq:
                    register1Id = TransportUtil.readLongFromInputStream(is);
                    register2Id = TransportUtil.readLongFromInputStream(is);
                    memoryOffset = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("rmmovq ");
                    sb.append(toRegisterName(register1Id));
                    sb.append(", ");
                    if (memoryOffset != 0) {
                        sb.append(memoryOffset);
                    }
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
                    sb = new StringBuilder("    ");
                    sb.append("jmp L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
                    count += 2;
                    break;
                case jle:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("jle L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
                    count += 2;
                    break;
                case jl:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("jl L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
                    count += 2;
                    break;
                case je:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("je L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
                    count += 2;
                    break;
                case jne:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("jne L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
                    count += 2;
                    break;
                case jge:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("jge L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
                    count += 2;
                    break;
                case jg:
                    memoryAddress = TransportUtil.readLongFromInputStream(is);
                    sb = new StringBuilder("    ");
                    sb.append("jg L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
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
                    sb = new StringBuilder("    ");
                    sb.append("call L");
                    sb.append(memoryAddress);
                    sb.append("\n");
                    lines.put(count, sb);
                    labels.add(memoryAddress);
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
        labelAppender(0L, lines.get(0L));
        labels.forEach(label -> labelAppender(label, lines.get(label)));
        lines.forEach((pos, stringBuilder) -> {
            try {
                os.write(stringBuilder.toString().getBytes());
            } catch (IOException e) {
                SneakyThrow.sneakyThrow(e);
            }
        });
        os.write("\n".getBytes());
    }

    private static StringBuilder labelAppender(Long pos, StringBuilder sb) {
        if (sb == null) {
            throw new IllegalArgumentException("Cannot find code with index: " + pos);
        }
        String label = "\nL" + pos + ":\n";
        if (!sb.toString().startsWith(label)) {
            sb.insert(0, label);
        }
        return sb;
    }

    private static long toRegisterId(String registerName) {
        switch (registerName.toLowerCase()) {
            case "%rax":
                return RAX;
            case "%rbx":
                return RBX;
            case "%rcx":
                return RCX;
            case "%rdx":
                return RDX;
            case "%rsp":
                return RSP;
            case "%rbp":
                return RBP;
            case "%rsi":
                return RSI;
            case "%rdi":
                return RDI;
            case "%r08":
                return R08;
            case "%r09":
                return R09;
            case "%r10":
                return R10;
            case "%r11":
                return R11;
            case "%r12":
                return R12;
            case "%r13":
                return R13;
            case "%r14":
                return R14;
            case "%r15":
                return R15;
            default:
                throw new IllegalArgumentException("Unsupported registerName: " + registerName);
        }
    }

    private static String toRegisterName(long registerId) {
        switch ((int) registerId) {
            case RAX:
                return "%rax";
            case RBX:
                return "%rbx";
            case RCX:
                return "%rcx";
            case RDX:
                return "%rdx";
            case RSP:
                return "%rsp";
            case RBP:
                return "%rbp";
            case RSI:
                return "%rsi";
            case RDI:
                return "%rdi";
            case R08:
                return "%r08";
            case R09:
                return "%r09";
            case R10:
                return "%r10";
            case R11:
                return "%r11";
            case R12:
                return "%r12";
            case R13:
                return "%r13";
            case R14:
                return "%r14";
            case R15:
                return "%r15";
            default:
                throw new IllegalArgumentException("Unsupported registerId: " + registerId);
        }
    }

}