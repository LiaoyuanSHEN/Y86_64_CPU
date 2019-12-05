

long sum(long *start, long count) {
    long sum = 0;
    while (count) {
        sum += *start;
        ++start;
        --count;
    }
    return sum;
}
