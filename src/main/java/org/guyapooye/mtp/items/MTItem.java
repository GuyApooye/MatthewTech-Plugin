package org.guyapooye.mtp.items;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.guyapooye.mtp.blocks.MTBlock;

import java.util.Objects;

import static org.guyapooye.mtp.blocks.MTBlocks.registeredBlocks;
import static org.guyapooye.mtp.items.MTItems.registeredItems;

public class MTItem implements MTItemLike {
    @Getter
    private final Item baseItem;
    @Getter
    private final ItemStack modifiableItem;
    @Getter
    private CompoundTag tag;
    @Getter
    private final String name;
    private String id;
    public MTItem(String id , ItemStack item, String name) {
        org.bukkit.inventory.ItemStack itemStack = CraftItemStack.asBukkitCopy(item);
        ItemMeta m = itemStack.getItemMeta();
        m.setDisplayName(ChatColor.RESET+name);
        itemStack.setItemMeta(m);
        this.baseItem = item.getItem();
        this.id = id;
        this.modifiableItem = CraftItemStack.asNMSCopy(itemStack);
        this.name = name;
        this.tag = modifiableItem.getOrCreateTag();
        tag.put("MT", new CompoundTag());
        tag.getCompound("MT").putString("id",id);
        this.modifiableItem.setTag(tag);

    }
    public MTItem(MTItem gtItem) {
        this(gtItem.id, gtItem.modifiableItem, gtItem.name);
    }

    public static MTItem fromID(String id) {
        return registeredItems.get(id);
    }
    public boolean equalsItem(ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getTag().getCompound("MT")) == this.tag.getCompound("MT");
    }
    public static MTItem asMTCopy(ItemStack i) {
        return registeredItems.get(i.getOrCreateTag().getCompound("GT").getString("id"));
    }
    public MTBlock getBlock() {
        return registeredBlocks.get(id);
    }
    @Override
    public String getID() {
        return id;
    }
    public org.bukkit.inventory.ItemStack asBukkitCopy() {
        return CraftItemStack.asBukkitCopy(this.modifiableItem);
    }
    @Override
    public MTItem clone() {
        return new MTItem(id,modifiableItem,name);
    }
    public MTItem setCount(int c) {
        modifiableItem.setCount(c);
        tag = modifiableItem.getOrCreateTag();
        return this;
    }
}
