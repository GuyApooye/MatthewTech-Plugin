package org.guyapooye.mtp.utils;

import org.bukkit.Location;

public class MTUtils {
    public static String locationToString(Location v) {
        return v.getBlockX()+";"+v.getBlockY()+";"+v.getBlockZ();
    }
    public enum EnumValidationResult {
        VALID,
        INVALID,
        SKIP
    }
}
