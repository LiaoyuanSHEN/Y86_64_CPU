package y86_64.cpu;

public class Const {

    // RF
    public static final byte RAX = 0;
    public static final byte RBX = 1;
    public static final byte RCX = 2;
    public static final byte RDX = 3;
    public static final byte RSP = 4;
    public static final byte RBP = 5;
    public static final byte RSI = 6;
    public static final byte RDI = 7;
    public static final byte R08 = 8;
    public static final byte R09 = 9;
    public static final byte R10 = 10;
    public static final byte R11 = 11;
    public static final byte R12 = 12;
    public static final byte R13 = 13;
    public static final byte R14 = 14;
    public static final byte R15 = 15;

    // CC
    // zero flag
    public static final byte ZF = 0;
    // negative flag (the highest bit of last operation result)
    public static final byte SF = 1;
    // overflow flag
    public static final byte OF = 2;

    // Status
    public static final byte READY_CODE = 0;
    public static final byte RUNNING_CODE = 1;
    public static final byte HALT_CODE = 2;

    // Command
    /// $V stands for a number(immediate)
    /// %r* stands for a register
    /// D(%r*) stands for a value in memory with address equals (r* + D)
    /// L stands for a label of code
    public static final byte nop = 0;
    public static final byte halt = 1;
    // moves
    // irmovq $V %rA -> 10 V (index of rA)
    public static final byte irmovq = 10;
    // rrmovq %rA %rB -> 11 (index of rA) (index of rB)
    public static final byte rrmovq = 11;
    // mrmovq D(%rA) %rB -> 12 (index of rA) (index of rB) (value of D)
    public static final byte mrmovq = 12;
    // rmmovq %rA D(%rB) -> 12 (index of rA) (index of rB) (value of D)
    public static final byte rmmovq = 13;
    // operations
    // addq %rA %rB -> 20 (index of rA) (index of rB)
    public static final byte addq = 20;
    // subq %rA %rB -> 21 (index of rA) (index of rB)
    public static final byte subq = 21;
    // andq %rA %rB -> 22 (index of rA) (index of rB)
    public static final byte andq = 22;
    // xorq %rA %rB -> 23 (index of rA) (index of rB)
    public static final byte xorq = 23;
    // jumps
    // jm* L -> (code of jm*) (address of label)
    public static final byte jmp = 30;
    public static final byte jle = 31;
    public static final byte jl = 32;
    public static final byte je = 33;
    public static final byte jne = 34;
    public static final byte jge = 35;
    public static final byte jg = 36;
    // condition moves
    // cmov* %rA %rB -> (code of cmov*) (index of rA) (index of rB)
    public static final byte cmovle = 40;
    public static final byte cmovl = 41;
    public static final byte cmove = 42;
    public static final byte cmovne = 43;
    public static final byte cmovge = 44;
    public static final byte cmovg = 45;
    // calls
    // call L -> 50 (address of label)
    public static final byte call = 50;
    // ret -> 51
    public static final byte ret = 51;
    // stack operations
    // pushq %rA -> 60 (index of rA)
    public static final byte pushq = 60;
    // popq %rA -> 61 (index of rA)
    public static final byte popq = 61;

}
