package y86_64.cpu;

public abstract class Register {

    public static final int REGISTER_COUNT = 16;
    // RF
    public static final int RAX = 0;
    public static final int RBX = 1;
    public static final int RCX = 2;
    public static final int RDX = 3;
    public static final int RSP = 4;
    public static final int RBP = 5;
    public static final int RSI = 6;
    public static final int RDI = 7;
    public static final int R08 = 8;
    public static final int R09 = 9;
    public static final int R10 = 10;
    public static final int R11 = 11;
    public static final int R12 = 12;
    public static final int R13 = 13;
    public static final int R14 = 14;
    public static final int R15 = 15;

    public abstract long getId();

    public abstract String getName();

    public static long toId(String name) {
        switch (name.toLowerCase()) {
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
                throw new IllegalArgumentException("Unsupported registerName: " + name);
        }
    }

    public static String toName(long id) {
        switch ((int) id) {
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
                throw new IllegalArgumentException("Unsupported registerId: " + id);
        }
    }

}
