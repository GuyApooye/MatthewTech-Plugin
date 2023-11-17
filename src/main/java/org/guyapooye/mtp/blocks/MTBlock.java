package org.guyapooye.mtp.blocks;

import lombok.Getter;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.items.MTItemLike;


public class MTBlock implements MTItemLike {
    @Getter
    private final MTBlockItem blockItem;
    public MTBlock(MTBlockItem blockItem) {
        this.blockItem = blockItem;
    }
    public static MTBlock fromID(String id) {
        return MTBlocks.registeredBlocks.get(id);
    }

    @Override
    public String getID() {
        return blockItem.getID();
    }
}
