package org.guyapooye.mtp.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.guyapooye.mtp.blocks.MTBlock;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.gui.TileEntityMenu;
import org.guyapooye.mtp.utils.MTUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MTTileEntity extends MTBlock implements Container, MenuProvider{
    protected Location loc;
    public NonNullList<ItemStack> items = NonNullList.withSize(getSize(),ItemStack.EMPTY);

    public MTTileEntity(MTBlockItem blockItem) {
        super(blockItem);
        if(!(blockItem.getBaseBlock() instanceof BaseEntityBlock)) throw new IllegalArgumentException();
        backgroundCreator(items);
    }
    public void backgroundCreator(@Nullable NonNullList<ItemStack> list) {
        for (int i = 0; i < 27; i++) {
            if (getInputSlots().contains(i) || getOutputSlots().contains(i)) continue;
            list.set(i,MTUtils.GUI_BACKGROUND.copy());
        }
    }
    public void openGUI(org.bukkit.entity.Player p, Location l) {
        loc = l;
        NonNullList<ItemStack> items = NonNullList.withSize(getSize(),ItemStack.EMPTY);
        CompoundTag ct = ((CraftWorld)l.getWorld()).getHandle().getBlockEntity(new BlockPos(l.getBlockX(),l.getBlockY(),l.getBlockZ())).persistentDataContainer.toTagCompound();
        ContainerHelper.loadAllItems(ct.getCompound("MT"),items);
        backgroundCreator(items);
        this.items = items;
        ServerPlayer sp = ((CraftPlayer) p).getHandle();
        sp.openMenu(this);
    }

//    protected void initializeInventory(Location l) {
//        CraftWorld w = (CraftWorld) l.getWorld();
//        BlockEntity b = w.getHandle().getBlockEntity(new BlockPos(l.getBlockX(),l.getBlockY(),l.getBlockZ()));
//        ListTag t = b.persistentDataContainer.toTagCompound().getCompound("PublicBukkitValue").getCompound("MT").getList("Inventory",10);
//        for (int j = 0; j < getSize(); j++) {
//            if(getInputSlots().contains(j) | getOutputSlots().contains(j)) {
//                dummyInventory.setItem(j, CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of((CompoundTag) t.get(j)
//            )));}
//            dummyInventory.setItem(j,new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        }
//    }
//    public void injectInventory(Location l) {
//        CraftWorld w = (CraftWorld) l.getWorld();
//        BlockEntity b = w.getHandle().getBlockEntity(new BlockPos(l.getBlockX(),l.getBlockY(),l.getBlockZ()));
//        if(b == null) throw new IllegalArgumentException();
//        CompoundTag t = new CompoundTag();
//        t.getCompound("PublicBukkitValues").getCompound("MT").put("Inventory", new ListTag());
//    }

    public void rightClick(Player p, Location l) {
        p.sendMessage("you have clicked a tile entity!");
        openGUI(p,l);
    }


    public List<HumanEntity> transaction = new ArrayList<>();
    public int getSize() {
        return 27;
    }
    public List<Integer> getInputSlots() {
        List<Integer> input = new ArrayList<>();
        input.add(12);
        return input;
    }
    public List<Integer> getOutputSlots() {
        List<Integer> output = new ArrayList<>();
        output.add(14);
        return output;
    }
    @Override
    public int getContainerSize() {
        return getSize();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public net.minecraft.world.item.ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public net.minecraft.world.item.ItemStack removeItem(int i, int i1) {
        Bukkit.broadcastMessage("removeItem");
        return ContainerHelper.removeItem(items,i,i1);
    }

    @Override
    public net.minecraft.world.item.ItemStack removeItemNoUpdate(int i) {
        Bukkit.broadcastMessage("removeItemNoUpdate");
        return ContainerHelper.takeItem(items,i);
    }

    @Override
    public void setItem(int i, net.minecraft.world.item.ItemStack itemStack) {
        Bukkit.broadcastMessage("setItem");
        ItemStack itemstack1 = this.items.get(i);
        boolean flag = !itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemstack1, itemStack) && !canPlaceItem(i,itemStack) || !canTakeItem(this,i,itemStack);
        this.items.set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }
        if (getInputSlots().contains(i) && !flag) {
            this.setChanged();
        }
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    private CompoundTag saveAllItems(CompoundTag var0, NonNullList<ItemStack> var1) {
        ListTag var3 = new ListTag();

        for(int var4 = 0; var4 < var1.size(); ++var4) {
            if(!(getOutputSlots().contains(var4) || getInputSlots().contains(var4))) continue;
            ItemStack var5 = var1.get(var4);
            if (!var5.isEmpty()) {
                CompoundTag var6 = new CompoundTag();
                var6.putByte("Slot", (byte)var4);
                var5.save(var6);
                var3.add(var6);
            }
        }
        var0.put("Items", var3);
        return var0;
    }

    @Override
    public void setChanged() {
        Bukkit.broadcastMessage("setChanged");
        getViewers().forEach(e -> {
            ((CraftHumanEntity) e).getHandle().containerMenu.broadcastChanges();
        });
        CompoundTag ct = ((CraftWorld) loc.getWorld()).getHandle().getBlockEntity(new BlockPos(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())).persistentDataContainer.toTagCompound();
        this.saveAllItems(ct.getCompound("MT"),items);
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return Container.stillValidBlockEntity(((CraftWorld) loc.getWorld()).getHandle().getBlockEntity(new BlockPos(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())), player);
    }

    @Override
    public List<net.minecraft.world.item.ItemStack> getContents() {
        return null;
    }

    @Override
    public void onOpen(CraftHumanEntity craftHumanEntity) {
        transaction.add(craftHumanEntity);
        Bukkit.broadcastMessage("onOpen");
    }

    @Override
    public void onClose(CraftHumanEntity craftHumanEntity) {
        transaction.remove(craftHumanEntity);
        Bukkit.broadcastMessage("onClose");
    }

    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public InventoryHolder getOwner() {
        return null;
    }

    @Override
    public void setMaxStackSize(int i) {}

    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public void clearContent() {
        Bukkit.broadcastMessage("clearContent");
        getInputSlots().forEach(i -> {
            items.remove(i);
        });
        getOutputSlots().forEach(i -> {
            items.remove(i);
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(getBlockItem().getName());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, net.minecraft.world.entity.player.Player player) {
        return TileEntityMenu.threeRows(i,inventory,this);
    }
}

