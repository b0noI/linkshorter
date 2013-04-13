package ua.in.link.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomString {

    private static final List<Character> symbols = new ArrayList<>();

    static {
        int idx;
        for (idx = 0; idx < 10; ++idx)
            symbols.add((char) ('0' + idx));
        for (idx = 10; idx < 36; ++idx)
            symbols.add((char) ('a' + idx - 10));
        for (int i = (int)'а'; i < (int)'Я'; i++)
            symbols.add((char)i);
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
            buf[idx] = symbols.get(random.nextInt(symbols.size()));
        return new String(buf);
    }

}
