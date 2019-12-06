# entry main function
    irmovq $1024, %rbp
    irmovq $1024, %rsp
    call main
    jmp end

# long sum(long *start, long count)
# start in %rdi, count in %rsi
sum:
    # saving previous stack info
    pushq %rbp
    rrmovq %rsp, %rbp

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
    # restoring previous stack info
    rrmovq %rbp, %rsp
    popq %rbp
    ret

# main
main:
    rrmovq %rsp, %r10
    irmovq $32, %r08
    subq %r08, rsp
    irmovq $24, %r08
    subq %r08, %r10
    irmovq $1, %r09
    rmmovq %r09, (%r10)
    irmovq $2, %r09
    rmmovq %r09, 8(%r10)
    irmovq $3, %r09
    rmmovq %r09, 16(%r10)
    irmovq $4, %r09
    rmmovq %r09, 24(%r10)
    rrmovq %r10, %rdi
    irmovq %4, %rsi
    call sum

end:
    nop