package org.guyapooye.mtp.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static org.guyapooye.mtp.items.MTItems.registeredItems;

public class MTItemStack {
    public ItemStack itemStack;
    public boolean isMT() {
        return itemStack.getOrCreateTag().contains("MT");
    }
    public boolean isVannila() {
        return !itemStack.getOrCreateTag().contains("MT");
    }
    public String getIdentifier() {
        if(isMT()) return itemStack.getOrCreateTag().getCompound("MT").getString("id");
        return Item.getId(itemStack.getItem())+"";
    }
    public static boolean isMT(ItemStack itemStack) {
        return itemStack.getOrCreateTag().contains("MT");
    }
    public static boolean isVannila(ItemStack itemStack) {
        return !itemStack.getOrCreateTag().contains("MT");
    }
    public MTItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    public MTItemStack(MTItem itemStack) {
        this.itemStack = itemStack.getModifiableItem();
    }

    public MTItemStack copy() {
        return new MTItemStack(itemStack);
    }
    public MTItemStack(String identifier) {
        this(registeredItems.get(identifier) == null ? new ItemStack(Item.byId(Integer.parseInt(identifier))) : registeredItems.get(identifier).getModifiableItem());
    }
}
