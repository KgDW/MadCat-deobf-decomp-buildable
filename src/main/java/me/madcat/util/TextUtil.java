package me.madcat.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Random;

public class TextUtil {
    public static final String AQUA;
    public static final String BLACK;
    public static final String LIGHT_PURPLE;
    public static final String DARK_GRAY;
    public static final String BLUE;
    public static final String GRAY;
    public static final String ITALIC;
    public static final String GOLD;
    public static final String DARK_AQUA;
    public static final String STRIKE;
    public static final String DARK_PURPLE;
    public static final String RED;
    public static final String WHITE;
    public static final String DARK_BLUE;
    public static final String UNDERLINE;
    public static final String DARK_GREEN;
    public static final String YELLOW;
    public static final String GREEN;
    private static final Random rand;
    public static final String DARK_RED;
    public static final String BOLD;
    public static final String OBFUSCATED;
    public static final String RESET;

    static {
        BLACK = String.valueOf(ChatFormatting.BLACK);
        DARK_BLUE = String.valueOf(ChatFormatting.DARK_BLUE);
        DARK_GREEN = String.valueOf(ChatFormatting.DARK_GREEN);
        DARK_AQUA = String.valueOf(ChatFormatting.DARK_AQUA);
        DARK_RED = String.valueOf(ChatFormatting.DARK_RED);
        DARK_PURPLE = String.valueOf(ChatFormatting.DARK_PURPLE);
        GOLD = String.valueOf(ChatFormatting.GOLD);
        GRAY = String.valueOf(ChatFormatting.GRAY);
        DARK_GRAY = String.valueOf(ChatFormatting.DARK_GRAY);
        BLUE = String.valueOf(ChatFormatting.BLUE);
        GREEN = String.valueOf(ChatFormatting.GREEN);
        AQUA = String.valueOf(ChatFormatting.AQUA);
        RED = String.valueOf(ChatFormatting.RED);
        LIGHT_PURPLE = String.valueOf(ChatFormatting.LIGHT_PURPLE);
        YELLOW = String.valueOf(ChatFormatting.YELLOW);
        WHITE = String.valueOf(ChatFormatting.WHITE);
        OBFUSCATED = String.valueOf(ChatFormatting.OBFUSCATED);
        BOLD = String.valueOf(ChatFormatting.BOLD);
        STRIKE = String.valueOf(ChatFormatting.STRIKETHROUGH);
        UNDERLINE = String.valueOf(ChatFormatting.UNDERLINE);
        ITALIC = String.valueOf(ChatFormatting.ITALIC);
        RESET = String.valueOf(ChatFormatting.RESET);
        rand = new Random();
    }

    public static String coloredString(final String s, final Color color) {
        String s2 = s;
        switch (color) {
            case AQUA:
                s2 = ChatFormatting.AQUA + s2;
                break;
            case WHITE:
                s2 = ChatFormatting.WHITE + s2;
                break;
            case BLACK:
                s2 = ChatFormatting.BLACK + s2;
                break;
            case DARK_BLUE:
                s2 = ChatFormatting.DARK_BLUE + s2;
                break;
            case DARK_GREEN:
                s2 = ChatFormatting.DARK_GREEN + s2;
                break;
            case DARK_AQUA:
                s2 = ChatFormatting.DARK_AQUA + s2;
                break;
            case DARK_RED:
                s2 = ChatFormatting.DARK_RED + s2;
                break;
            case DARK_PURPLE:
                s2 = ChatFormatting.DARK_PURPLE + s2;
                break;
            case GOLD:
                s2 = ChatFormatting.GOLD + s2;
                break;
            case DARK_GRAY:
                s2 = ChatFormatting.DARK_GRAY + s2;
                break;
            case GRAY:
                s2 = ChatFormatting.GRAY + s2;
                break;
            case BLUE:
                s2 = ChatFormatting.BLUE + s2;
                break;
            case RED:
                s2 = ChatFormatting.RED + s2;
                break;
            case GREEN:
                s2 = ChatFormatting.GREEN + s2;
                break;
            case LIGHT_PURPLE:
                s2 = ChatFormatting.LIGHT_PURPLE + s2;
                break;
            case YELLOW:
                s2 = ChatFormatting.YELLOW + s2;
                break;
        }
        return s2 + ChatFormatting.RESET;
    }


    public enum Color {
        NONE,
        WHITE,
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW

    }
}

