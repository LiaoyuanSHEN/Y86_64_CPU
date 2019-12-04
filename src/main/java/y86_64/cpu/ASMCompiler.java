package y86_64.cpu;

import y86_64.bus.TransportUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static y86_64.cpu.Const.*;

public class ASMCompiler {

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
                        TransportUtil.writeLongToOutputStream(toRegister(matcher.group(2)), os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(matcher.group(1)), os);
                        count += 4;
                        break;
                    }
                case "rmmoveq":
                    matcher = movPattern.matcher(codes[2]);
                    if (matcher.find()) {
                        TransportUtil.writeLongToOutputStream(rmmoveq, os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                        TransportUtil.writeLongToOutputStream(toRegister(matcher.group(2)), os);
                        TransportUtil.writeLongToOutputStream(Long.parseLong(matcher.group(1)), os);
                        count += 4;
                        break;
                    }
                case "addq":
                    TransportUtil.writeLongToOutputStream(addq, os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[1]), os);
                    TransportUtil.writeLongToOutputStream(Long.parseLong(codes[2]), os);
                    count += 3;
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

    private static long toRegister(String registerName) {
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
        }
        throw new IllegalArgumentException("Unsupported register: " + registerName);
    }

}
