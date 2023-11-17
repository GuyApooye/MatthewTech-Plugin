package org.guyapooye.mtp.gui;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryView;

public class TileEntityMenu extends AbstractContainerMenu {

    private final Inventory player;
    private final Container container;
    private CraftInventoryView bukkitEntity = null;
    public static TileEntityMenu threeRows(int i, Inventory playerinventory, Container iinventory) {
        return new TileEntityMenu(MenuType.GENERIC_9x3, i, playerinventory, iinventory);
    }

    public TileEntityMenu(MenuType<?> containers, int i, Inventory playerinventory, Container iinventory) {
        super(containers, i);
        player = playerinventory;
        container = iinventory;
        checkContainerSize(iinventory, 3 * 9);
        iinventory.startOpen(playerinventory.player);
        int k = -18;

        int l;
        int i1;
        for(l = 0; l < 3; ++l) {
            for(i1 = 0; i1 < 9; ++i1) {
                if(9*l+i1 == 12) {this.addSlot(new Slot(iinventory, i1 + l * 9, 8 + i1 * 18, 18 + l * 18)); continue;}
                if(9*l+i1 == 14) {this.addSlot(new OutputSlot(iinventory, i1 + l * 9, 8 + i1 * 18, 18 + l * 18)); continue;}
                this.addSlot(new BackgroundSlot(iinventory, i1 + l * 9, 8 + i1 * 18, 18 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 103 + l * 18 + k));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerinventory, l, 8 + l * 18, 161 + k));
        }

    }
    @Override
    public CraftInventoryView getBukkitView() {
        if (this.bukkitEntity != null) {
            return this.bukkitEntity;
        } else {
            Object inventory;
            if (this.container instanceof Inventory) {
                inventory = new CraftInventoryPlayer((Inventory)this.container);
            } else if (this.container instanceof CompoundContainer) {
                inventory = new CraftInventoryDoubleChest((CompoundContainer)this.container);
            } else {
                inventory = new CraftInventory(this.container);
            }

            this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), (org.bukkit.inventory.Inventory)inventory, this);
            return this.bukkitEntity;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (i < 3 * 9) {
                if (!this.moveItemStackTo(itemstack1, 3 * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 3 * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
    public void removed(Player entityhuman) {
        super.removed(entityhuman);
    }
}
