

    nop
    halt

MOVES:
    irmovq $1, %rax
    rrmovq %rax, %rbx
    mrmovq (%rbx), %rcx
    rmmovq %rcx, 2(%rdx)

OPERATIONS:
    addq %rsp, %rbp
    subq %rsi, %rdi
    andq %r08, %r09
    addq %r10, %r11

JUMPS:
    jmp MOVES
    jle OPERATIONS
    jl JUMPS
    je CONDITION_MOVES
    jne STACK_OPTIONS
    jge MOVES
    jg OPERATIONS

CONDITION_MOVES:
    cmovle %r12, %r13
    cmovl %r13, %r14
    cmove %r14, %r15
    cmovne %r15, %r12
    cmovge %r12, %r13
    cmovg %r13, %r14

CALLS:
    call CALLS
    ret

STACK_OPTIONS:
    pushq %rax
    popq %rbx
