package ua.in.link.db;

/**
  * User: ivan.mushketyk at gmail.com
  */
public class NextURLUtils {

    public static char INITIAL_CHAR = 'a';

    public static String getNextUrl(String shortURL) {

        StringBuilder nextURL = new StringBuilder(shortURL);

        int i = shortURL.length() - 1;
        for (; i >= 0; i--) {
            char currChar = shortURL.charAt(i);
            char nextChar = getNextChar(currChar);

            nextURL.setCharAt(i, nextChar);

            if (nextChar != INITIAL_CHAR) {
                break;
            }
        }

        if (i < 0) {
            nextURL.insert(0, INITIAL_CHAR);
        }

        return nextURL.toString();
    }

    private static char getNextChar(char currChar) {
        if (currChar == 'z') {
            return 'A';
        } else if (currChar == 'Z') {
            return '0';
        } else if (currChar == '9') {
            return INITIAL_CHAR;
        } else {
            return (char) (currChar + 1);
        }
    }

}
