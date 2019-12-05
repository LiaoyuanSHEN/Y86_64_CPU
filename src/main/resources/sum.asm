
# long sum(long *start, long count)
# start in %rdi, count in %rsi
sum:
    irmovq $8, %r08
    irmovq $1, %r09
    xorq %rax, %rax
    andq %rsi, %rsi
    jmp test
loop:
    mrmovq (%rdi), %r10
    addq %r10, %rax
    addq %r08, %rdi
    sub %r09, %rsi
test:
    jne loop
    ret

# main
main:
