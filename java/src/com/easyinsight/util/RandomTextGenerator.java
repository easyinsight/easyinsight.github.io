package com.easyinsight.util;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 7:20:07 PM
 */
public class RandomTextGenerator {
    public static String generateText(int length) {
        char[] activationBuf = new char[length];
        for (int i = 0; i < length; i++) {
            char c;
            int randVal = (int) (Math.random() * 26);
            if (randVal < 26) {
                c = (char) ('a' + randVal);
            } else {
                c = (char) (randVal - 26);
            }
            if ((int) (Math.random() * 2) == 1) {
                c = Character.toUpperCase(c);
            }
            activationBuf[i] = c;
        }
        return new String(activationBuf);
    }
}
