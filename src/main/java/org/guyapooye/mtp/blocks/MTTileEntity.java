package org.guyapooye.mtp.blocks;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.utils.PlayerRunnable;

public abstract class MTTileEntity extends MTBlock {
    @Getter
    @Setter
    private PlayerRunnable onRightClick;


    public MTTileEntity(MTBlockItem blockItem) {
        super(blockItem);
        if(!(blockItem.getBaseBlock() instanceof BaseEntityBlock)) throw new IllegalArgumentException();
    }
    public void rightClick(Player p) {
        onRightClick.run(p);
    }
}
