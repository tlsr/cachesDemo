package org.example.cache.utils;

import java.util.ArrayList;
import java.util.List;

public class PrintUtils {

    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";
    private static final int MAX_LINE_LENGTH = 128;
    private static final String LEFT_GREEN_VERTICAL_LINE = GREEN + "| " + RESET;
    private static final String RIGHT_GREEN_VERTICAL_LINE = GREEN + " |" + RESET;

    public static void print(String msg) {
        String[] lines = splitMessage(msg, MAX_LINE_LENGTH);
        int borderLength = Math.min(msg.length(), MAX_LINE_LENGTH);
        System.out.println(GREEN + "+=" + "=".repeat(borderLength) + "=+" + RESET);
        for (String line : lines) {
            System.out.println(LEFT_GREEN_VERTICAL_LINE + line + RIGHT_GREEN_VERTICAL_LINE);
        }
        System.out.println(GREEN + "+=" + "=".repeat(borderLength) + "=+" + RESET);
    }

    private static String[] splitMessage(String msg, int maxLineLength) {
        String[] words = msg.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new ArrayList<>();

        for (String word : words) {
            if (line.length() + word.length() + 1 > maxLineLength) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            if (!line.isEmpty()) {
                line.append(" ");
            }
            line.append(word);
        }
        lines.add(line.toString());

        return lines.toArray(new String[0]);
    }

}
