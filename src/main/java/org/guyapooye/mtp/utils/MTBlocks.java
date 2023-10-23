package org.guyapooye.mtp.utils;

import org.guyapooye.mtp.blocks.MTBlock;
import org.guyapooye.mtp.blocks.MTTileEntity;
import org.guyapooye.mtp.blocks.machines.SimpleMachine;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.recipes.RecipeMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.guyapooye.mtp.utils.MTItems.registeredItems;

public class MTBlocks {
    public static final Map<String, MTBlock> registeredBlocks = new HashMap<>();
    static {
    }
    protected static MTBlock registerBlock(MTBlockItem blockItem) {
        MTBlock gtBlock = new MTBlock(blockItem);
        registeredBlocks.put(gtBlock.getID(),gtBlock);
        return gtBlock;
    }
    protected static void registerTileEntities(MTTileEntity[] machines, String name, Function<Integer, MTTileEntity> teCreator) {
        for (int i = 0; i < machines.length - 1; i++) {
            machines[i + 1] = registerTileEntity(teCreator.apply(i + 1));
        };
    }
    public static <T extends MTTileEntity> T registerTileEntity(T sampleMetaTileEntity) {
        registeredBlocks.put(sampleMetaTileEntity.getID(), sampleMetaTileEntity);
        return sampleMetaTileEntity;
    }
    public static void registerSimpleTileEntity(SimpleMachine[] machines,
                                                    String name,
                                                    RecipeMap<?> map
    //                                                boolean hasFrontFacing
    ) {
        registerTileEntities(machines, name, tier -> new SimpleMachine((MTBlockItem) registeredItems.get(name+"."+MTValues.VNF[tier].toLowerCase()), map, tier));
    }

}
