package org.guyapooye.mtp.items;

import lombok.Getter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@Getter
public class MTBlockItem extends MTItem {
    public MTBlockItem(String id, ItemStack mtItem, String name) {
        super(id, mtItem,name);
        if (!(mtItem.getItem() instanceof BlockItem)) throw new IllegalArgumentException("MTBlockItems can only be constructed with BlockItems");
    }
    public MTBlockItem(MTBlockItem gtItem) {
        super(gtItem);
    }
    public Block getBaseBlock() {
        return ((BlockItem)this.getBaseItem()).getBlock();
    }
}
