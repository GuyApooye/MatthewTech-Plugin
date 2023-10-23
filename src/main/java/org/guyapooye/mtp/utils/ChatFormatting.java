package org.guyapooye.mtp.utils;

import org.bukkit.ChatColor;

public class ChatFormatting {
    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('$',s);
    }
    public static String reset(String s) {
        return ChatColor.RESET+s;
    }
}
