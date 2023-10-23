package org.guyapooye.mtp.blocks.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.guyapooye.mtp.blocks.TieredTileEntity;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.recipes.RecipeMap;
import org.guyapooye.mtp.utils.MTValues;

import java.util.ArrayList;
import java.util.List;

public class SimpleMachine extends TieredTileEntity {
    public RecipeMap<?> map;
    Inventory i = Bukkit.createInventory(null,getSize());

    public SimpleMachine(MTBlockItem blockItem, RecipeMap<?> map, int v) {
        super(blockItem,v);
        this.map = map;
    }
    protected void initializeInventory(Location l) {
        CraftWorld w = (CraftWorld) l.getWorld();
        BlockEntity b = w.getHandle().getBlockEntity(new BlockPos(l.getBlockX(),l.getBlockY(),l.getBlockZ()));
        ListTag t = b.persistentDataContainer.toTagCompound().getCompound("PublicBukkitValue").getCompound("MT").getList("Inventory",10);
        for (int j = 0; j < getSize(); j++) {
            if(getInputSlots().contains(j) | getOutputSlots().contains(j)) {i.setItem(j,CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of((CompoundTag) t.get(j)
            )));}
            i.setItem(j,new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
    }
    public void injectInventory(Location l) {
        CraftWorld w = (CraftWorld) l.getWorld();
        BlockEntity b = w.getHandle().getBlockEntity(new BlockPos(l.getBlockX(),l.getBlockY(),l.getBlockZ()));
        CompoundTag t = new CompoundTag();
        t.getCompound("PublicBukkitValues").getCompound("MT").put("Inventory", new ListTag());
    }

    public int getSize() {
        return 27;
    }

    public List<Integer> getInputSlots() {
        List<Integer> input = new ArrayList<>();
        input.add(13);
        return input;
    }

    public List<Integer> getOutputSlots() {
        List<Integer> output = new ArrayList<>();
        output.add(13);
        return output;
    }
}
