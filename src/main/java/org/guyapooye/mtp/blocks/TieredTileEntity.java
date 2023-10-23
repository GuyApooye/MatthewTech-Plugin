package org.guyapooye.mtp.blocks;

import lombok.Getter;
import org.guyapooye.mtp.items.MTBlockItem;

public class TieredTileEntity extends MTTileEntity implements TileEntityTiered {
    @Getter
    private final int voltage;
    public TieredTileEntity(MTBlockItem blockItem, int v) {
        super(blockItem);
        this.voltage = v;
    }
}
