package y86_64.cpu;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class InputOutputStream extends OutputStream {

    private final List<Integer> list = new LinkedList<>();

    @Override
    public void write(int b) throws IOException {
        list.add(b);
    }

    public InputStream toInputStream() {
        return new InputStream() {

            private Iterator<Integer> iterator = list.iterator();
            private int length = list.size();
            private int index = 1;

            @Override
            public int read() throws IOException {
                if (iterator.hasNext()) {
                    ++index;
                    return iterator.next();
                }
                return -1;
            }

            @Override
            public int available() throws IOException {
                return length - index;
            }

            @Override
            public synchronized void reset() throws IOException {
                index = 1;
            }
        };
    }

}

