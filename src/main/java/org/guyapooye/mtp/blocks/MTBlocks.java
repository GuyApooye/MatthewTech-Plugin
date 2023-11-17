package org.guyapooye.mtp.blocks;

import org.guyapooye.mtp.blocks.tileentities.MTTileEntity;
import org.guyapooye.mtp.blocks.tileentities.SimpleMachine;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.items.MTItems;
import org.guyapooye.mtp.recipes.RecipeMap;
import org.guyapooye.mtp.utils.MTValues;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.guyapooye.mtp.items.MTItems.registeredItems;

public class MTBlocks {
    public static final Map<String, MTBlock> registeredBlocks = new HashMap<>();
    public static final MTTileEntity TEST_TILE_ENTITY;
    public static final MTBlock TEST_BLOCK;
    static {
        TEST_BLOCK = registerBlock(MTItems.TEST_BLOCK);
        TEST_TILE_ENTITY = registerTileEntity(new MTTileEntity(MTItems.TEST_TILE_ENTITY));
    }
    protected static MTBlock registerBlock(MTBlockItem blockItem) {
        MTBlock gtBlock = new MTBlock(blockItem);
        registeredBlocks.put(gtBlock.getID(),gtBlock);
        return gtBlock;
    }
    protected static void registerTileEntities(MTTileEntity[] machines, Function<Integer, MTTileEntity> teCreator) {
        for (int i = 0; i < machines.length - 1; i++) {
            machines[i + 1] = registerTileEntity(teCreator.apply(i + 1));
        };
    }
    public static <T extends MTTileEntity> T registerTileEntity(T mtTileEntity) {
        registeredBlocks.put(mtTileEntity.getID(), mtTileEntity);
        return mtTileEntity;
    }
    public static void registerSimpleTileEntity(SimpleMachine[] machines,
                                                String name,
                                                RecipeMap<?> map
                                                //                                              boolean hasFrontFacing
    ) {
        registerTileEntities(machines, tier -> new SimpleMachine((MTBlockItem) registeredItems.get(name+"."+ MTValues.VNF[tier].toLowerCase()), map, tier));
    }

}
