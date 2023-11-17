package org.guyapooye.mtp.utils;

import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MTUtils {
    public static String locationToString(Location v) {
        return v.getBlockX()+";"+v.getBlockY()+";"+v.getBlockZ();
    }

    public static ItemStack GUI_BACKGROUND;
    static {
        org.bukkit.inventory.ItemStack pane = new org.bukkit.inventory.ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(" ");
        pane.setItemMeta(paneMeta);
        GUI_BACKGROUND = CraftItemStack.asNMSCopy(pane);
    }
    public enum EnumValidationResult {
        VALID,
        INVALID,
        SKIP
    }
}
