package org.guyapooye.mtp.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BackgroundSlot extends Slot {
    public BackgroundSlot(Container iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }
    public ItemStack remove(int i) {
        return super.remove(i);
    }
    public boolean mayPlace(ItemStack itemstack) {
        return false;
    }
    public boolean mayPickup(Player p) {
        return false;
    }
    public void onTake(Player entityhuman, ItemStack itemstack) {
        super.onTake(entityhuman, itemstack);
    }

}
