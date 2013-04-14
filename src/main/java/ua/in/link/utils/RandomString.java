package ua.in.link.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomString {

    private static final List<Character> SYMBOLS = new ArrayList<>();

    static {
        int idx;
        for (idx = 0; idx < 10; ++idx) {
            SYMBOLS.add((char) ('0' + idx));
        }
        for (idx = 10; idx < 36; ++idx) {
            SYMBOLS.add((char) ('a' + idx - 10));
        }
        for (int i = 'а'; i < 'Я'; i++) {
            SYMBOLS.add((char) i);
        }
    }

    private final Random random = new Random();

    private final char[] buf;

    public RandomString(int length)
    {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    public String nextString()
    {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = SYMBOLS.get(random.nextInt(SYMBOLS.size()));
        return new String(buf);
    }

}
