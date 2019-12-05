package y86_64.cpu;

public class Const {

    // RF
    public final static int RAX = 0;
    public final static int RBX = 1;
    public final static int RCX = 2;
    public final static int RDX = 3;
    public final static int RSP = 4;
    public final static int RBP = 5;
    public final static int RSI = 6;
    public final static int RDI = 7;
    public final static int R08 = 8;
    public final static int R09 = 9;
    public final static int R10 = 10;
    public final static int R11 = 11;
    public final static int R12 = 12;
    public final static int R13 = 13;
    public final static int R14 = 14;
    public final static int R15 = 15;

    // CC
    // zero flag
    public final static int ZF = 0;
    // negative flag (the highest bit of last operation result)
    public final static int SF = 1;
    // overflow flag
    public final static int OF = 2;

    // Status
    public final static int READY_CODE = 0;
    public final static int RUNNING_CODE = 1;
    public final static int HALT_CODE = 2;

    // Command
    /// $V stands for a number(immediate)
    /// %r* stands for a register
    /// D(%r*) stands for a value in memory with address equals (r* + D)
    /// L stands for a label of code
    public final static int nop = 0;
    public final static int halt = 1;
    // moves
    // irmovq $V %rA -> 10 V (index of rA)
    public final static int irmovq = 10;
    // rrmovq %rA %rB -> 11 (index of rA) (index of rB)
    public final static int rrmovq = 11;
    // mrmovq D(%rA) %rB -> 12 (index of rA) (index of rB) (value of D)
    public final static int mrmovq = 12;
    // rmmovq %rA D(%rB) -> 12 (index of rA) (index of rB) (value of D)
    public final static int rmmovq = 13;
    // operations
    // addq %rA %rB -> 20 (index of rA) (index of rB)
    public final static int addq = 20;
    // subq %rA %rB -> 21 (index of rA) (index of rB)
    public final static int subq = 21;
    // andq %rA %rB -> 22 (index of rA) (index of rB)
    public final static int andq = 22;
    // xorq %rA %rB -> 23 (index of rA) (index of rB)
    public final static int xorq = 23;
    // jumps
    // jm* L -> (code of jm*) (address of label)
    public final static int jmp = 30;
    public final static int jle = 31;
    public final static int jl = 32;
    public final static int je = 33;
    public final static int jne = 34;
    public final static int jge = 35;
    public final static int jg = 36;
    // condition moves
    // cmov* %rA %rB -> (code of cmov*) (index of rA) (index of rB)
    public final static int cmovle = 40;
    public final static int cmovl = 41;
    public final static int cmove = 42;
    public final static int cmovne = 43;
    public final static int cmovge = 44;
    public final static int cmovg = 45;
    // calls
    // call L -> 50 (address of label)
    public final static int call = 50;
    // ret -> 51
    public final static int ret = 51;
    // stack operations
    // pushq %rA -> 60 (index of rA)
    public final static int pushq = 60;
    // popq %rA -> 61 (index of rA)
    public final static int popq = 61;

}
