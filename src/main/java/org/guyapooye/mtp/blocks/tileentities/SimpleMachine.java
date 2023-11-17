package org.guyapooye.mtp.blocks.tileentities;

import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.recipes.RecipeMap;



public class SimpleMachine extends WorkableTieredTileEntity {
    public RecipeMap<?> map;


    public SimpleMachine(MTBlockItem blockItem, RecipeMap<?> map, int v) {
        super(blockItem,map,v);
        this.map = map;

    }
}
