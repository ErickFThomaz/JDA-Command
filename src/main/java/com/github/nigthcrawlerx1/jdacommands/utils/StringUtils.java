package com.github.nigthcrawlerx1.jdacommands.utils;

import java.util.*;
import java.util.regex.Pattern;

public class StringUtils {
    public static final Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

    public static String[] advancedSplitArgs(String args, int expectedArgs) {
        //Final result to work with.
        List<String> result = new ArrayList<>();

        //Whether a string is in a "quotation block".
        boolean inBlock = false;
        //Current "quotation block"
        StringBuilder currentBlock = new StringBuilder();

        for (int i = 0; i < args.length(); i++) {
            char currentChar = args.charAt(i);
            //Flip inBlock if current character is a " or a start/end smart quote character aka “ or ” (but only if it's not escaped)
            if ((currentChar == '"' || currentChar == '“' || currentChar == '”') && (i == 0 || args.charAt(i - 1) != '\\' || args.charAt(i - 2) == '\\'))
                inBlock = !inBlock;

            //If character is currently in a block (aka "this is one block"), append to the current block.
            if (inBlock)
                currentBlock.append(currentChar);
                //If current character is a space.
            else if (Character.isSpaceChar(currentChar)) {
                //Check if next or last character is a " or a start/end smart quote character aka “ or ” and remove them.
                if (currentBlock.length() != 0) {
                    if (((currentBlock.charAt(0) == '"' || currentBlock.charAt(0) == '“') &&
                            (currentBlock.charAt(currentBlock.length() - 1) == '"' || currentBlock.charAt(currentBlock.length() - 1) == '”'))
                    ) {
                        //Remove start quote.
                        currentBlock.deleteCharAt(0);
                        //Remove end quote.
                        currentBlock.deleteCharAt(currentBlock.length() - 1);
                    }

                    //Add the unboxed result to the current block.
                    result.add(advancedSplitArgsUnbox(currentBlock.toString()));
                    //Reset the current block: end of block, parse another argument (assume each block is one argument)
                    currentBlock = new StringBuilder();
                }
            } else {
                //Append to current block.
                currentBlock.append(currentChar);
            }
        }

        if (currentBlock.length() != 0) {
            //Check if next or last character is a " or a start/end smart quote character aka “ or ” and remove them.
            if ((currentBlock.charAt(0) == '"' || currentBlock.charAt(0) == '“') &&
                    (currentBlock.charAt(currentBlock.length() - 1) == '"' || currentBlock.charAt(currentBlock.length() - 1) == '”')
            ) {
                //Remove start quote.
                currentBlock.deleteCharAt(0);
                //Remove end quote.
                currentBlock.deleteCharAt(currentBlock.length() - 1);
            }

            //Remove escape characters.
            result.add(advancedSplitArgsUnbox(currentBlock.toString()));
        }

        //Convert result to an string array.
        String[] raw = result.toArray(new String[0]);

        //If the amount of arguments this detected is less than one, just return the string as a whole.
        if (expectedArgs < 1)
            return raw;

        //Normalize array to the amount of arguments expected (aka, if we expect 3 arguments but have 6, join the last 3 in one argument)
        return normalizeArray(raw, expectedArgs);
    }

    public static String limit(String value, int length) {
        StringBuilder buf = new StringBuilder(value);
        if (buf.length() > length) {
            buf.setLength(length - 3);
            buf.append("...");
        }

        return buf.toString();
    }

    /**
     * Normalize an {@link String} Array.
     *
     * @param raw          the String array to be normalized
     * @param expectedSize the final size of the Array.
     * @return {@link String}[] with the size of expectedArgs
     */
    public static String[] normalizeArray(String[] raw, int expectedSize) {
        String[] normalized = new String[expectedSize];

        Arrays.fill(normalized, "");
        for (int i = 0; i < normalized.length; i++)
            if (i < raw.length && raw[i] != null && !raw[i].isEmpty())
                normalized[i] = raw[i];

        return normalized;
    }

    public static Map<String, String> parse(String[] args) {
        Map<String, String> options = new HashMap<>();

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].charAt(0) == '-' || args[i].charAt(0) == '/') //This start with - or /
                {
                    args[i] = args[i].substring(1);
                    if (i + 1 >= args.length || args[i + 1].charAt(0) == '-' || args[i + 1].charAt(0) == '/') //Next start with - (or last arg)
                    {
                        options.put(args[i], "null");
                    } else {
                        options.put(args[i], args[i + 1]);
                        i++;
                    }
                } else {
                    options.put(null, args[i]);
                }
            }

            return options;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * Enhanced {@link String#split(String, int)} with SPLIT_PATTERN as the Pattern used.
     *
     * @param args         the {@link String} to be split.
     * @param expectedArgs the size of the returned array of Non-null {@link String}s
     * @return a {@link String}[] with the size of expectedArgs
     */
    public static String[] splitArgs(String args, int expectedArgs) {
        String[] raw = SPLIT_PATTERN.split(args, expectedArgs);
        if (expectedArgs < 1) return raw;
        return normalizeArray(raw, expectedArgs);
    }

    //Basically removes escape characters.
    private static String advancedSplitArgsUnbox(String s) {
        return s.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t").replace("\\\"", "\"").replace("\\\\", "\\");
    }
}