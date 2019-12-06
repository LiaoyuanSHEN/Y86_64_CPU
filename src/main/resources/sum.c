
long sum(long *start, long count) {
    long sum = 0;
    while (count) {
        sum += *start;
        ++start;
        --count;
    }
    return sum;
}

int main() {
    long arr[4];
    arr[0] = 1;
    arr[1] = 2;
    arr[2] = 3;
    arr[3] = 4;
    sum(arr, 4);
}
