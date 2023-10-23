package org.guyapooye.mtp.utils;

import org.bukkit.ChatColor;

public class MTValues {
    public static final int ULV = 0;
    public static final int LV = 1;
    public static final int MV = 2;
    public static final int HV = 3;
    public static final int EV = 4;
    public static final int IV = 5;
    public static final int LuV = 6;
    public static final int ZPM = 7;
    public static final int UV = 8;
    public static final String[] VN = new String[]{"ULV","LV","MV","HV","EV","IV","LuV","ZPM","UV"};
    public static final String[] VNF = new String[]{
            ChatColor.DARK_GRAY + "ULV", ChatColor.GRAY + "LV", ChatColor.AQUA + "MV",
            ChatColor.GOLD + "HV", ChatColor.DARK_PURPLE + "EV", ChatColor.DARK_BLUE + "IV",
            ChatColor.LIGHT_PURPLE + "LuV", ChatColor.RED + "ZPM", ChatColor.DARK_AQUA + "UV"};
    public static final String[] VOLTAGE_NAMES = new String[]{"Ultra Low Voltage", "Low Voltage", "Medium Voltage", "High Voltage", "Extreme Voltage", "Insane Voltage", "Ludicrous Voltage", "ZPM Voltage", "Ultimate Voltage"};

}
