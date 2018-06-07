package edu.wfu.jsonparser.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * 封装读入
 */

public class CharReader {
    private static final int BUFFER_SIZE = 1024;

    private Reader reader;
    private char[] buffer;
    private int pos;
    private int size;


    /**
     * 输入流
     * 每次读取1024 * 2个字节
     *
     * @param reader
     */
    public CharReader(Reader reader) {
        this.reader = reader;
        buffer = new char[BUFFER_SIZE];
    }

    // 弹出当前字符
    public char peek() {
        if (pos - 1 >= size) {
            return (char) -1;
        }
        return buffer[Math.max(0, pos - 1)];
    }

    // 回退一个
    public void back() {
        pos = Math.max(0, pos--);
    }

    public char next() throws IOException {
        if (!hasMore()) {
            return (char) -1;
        }
        return buffer[pos++];
    }


    // 判断是否还有
    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }
        fillBuffer();
        return pos < size;
    }

    // 输入读入
    private void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            return;
        }

        pos = 0;
        size = n;
    }


}
