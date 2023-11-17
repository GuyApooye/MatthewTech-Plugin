package org.guyapooye.mtp.blocks.tileentities;

import lombok.Getter;
import net.minecraft.world.level.block.BaseEntityBlock;
import org.guyapooye.mtp.items.MTBlockItem;

public class TieredTileEntity extends MTTileEntity implements TileEntityTiered {
    @Getter
    private final int voltage;
    public TieredTileEntity(MTBlockItem blockItem, int v) {
        super(blockItem);
        if(!(blockItem.getBaseBlock() instanceof BaseEntityBlock)) throw new IllegalArgumentException();
        this.voltage = v;
    }
}
