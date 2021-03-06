package com.github.dr.rwserver.util.log;

import com.github.dr.rwserver.data.global.Data;
import com.github.dr.rwserver.struct.ObjectMap;

/**
 * @author Dr
 */
public class ColorCodes {
    private static final String FLUSH = "\033[H\033[2J";
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String ITALIC = "\u001B[3m";
    private static final String UNDERLINED = "\u001B[4m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String LIGHT_RED = "\u001B[91m";
    private static final String LIGHT_GREEN = "\u001B[92m";
    private static final String LIGHT_YELLOW = "\u001B[93m";
    private static final String LIGHT_BLUE = "\u001B[94m";
    private static final String LIGHT_MAGENTA = "\u001B[95m";
    private static final String LIGHT_CYAN = "\u001B[96m";
    private static final String WHITE = "\u001B[37m";

    private static final String BACK_DEFAULT = "\u001B[49m";
    private static final String BACK_RED = "\u001B[41m";
    private static final String BACK_GREEN = "\u001B[42m";
    private static final String BACK_YELLOW = "\u001B[43m";
    private static final String BACK_BLUE = "\u001B[44m";

    public static final String[] CODES;
    public static final String[] VALUES;

    static{
        ObjectMap<String, String> map;
        //WIN :(

        if(Data.core.isWindows()){
            map = ObjectMap.of(
                    "ff", "",
                    "fr", "",
                    "fb", "",
                    "fi", "",
                    "fu", "",
                    "bk", "",
                    "r", "",
                    "g", "",
                    "y", "",
                    "b", "",
                    "p", "",
                    "c", "",
                    "lr", "",
                    "lg", "",
                    "ly", "",
                    "lm", "",
                    "lb", "",
                    "lc", "",
                    "w", "",

                    "bd", "",
                    "br", "",
                    "bg", "",
                    "by", "",
                    "bb", ""
            );
        } else {
            map = ObjectMap.of(
                    "ff", FLUSH,
                    "fr", RESET,
                    "fb", BOLD,
                    "fi", ITALIC,
                    "fu", UNDERLINED,
                    "bk", BLACK,
                    "r", RED,
                    "g", GREEN,
                    "y", YELLOW,
                    "b", BLUE,
                    "p", PURPLE,
                    "c", CYAN,
                    "lr", LIGHT_RED,
                    "lg", LIGHT_GREEN,
                    "ly", LIGHT_YELLOW,
                    "lm", LIGHT_MAGENTA,
                    "lb", LIGHT_BLUE,
                    "lc", LIGHT_CYAN,
                    "w", WHITE,

                    "bd", BACK_DEFAULT,
                    "br", BACK_RED,
                    "bg", BACK_GREEN,
                    "by", BACK_YELLOW,
                    "bb", BACK_BLUE
            );
        }

        CODES = map.keys().toSeq().toArray(String.class);
        VALUES = map.values().toSeq().toArray(String.class);
    }

}
