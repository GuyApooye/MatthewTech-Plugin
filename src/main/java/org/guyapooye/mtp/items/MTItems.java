package org.guyapooye.mtp.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.guyapooye.mtp.blocks.MTBlocks;
import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.items.MTItem;
import org.guyapooye.mtp.utils.MTValues;

import java.util.HashMap;
import java.util.Map;


public class MTItems {
    public static final Map<String, MTItem> registeredItems = new HashMap<>();
    protected static MTItem registerItem(String id, ItemStack item, String name) {
        MTItem mtItem = new MTItem(id,item,name);
        registeredItems.put(mtItem.getID(),mtItem);
        return mtItem;
    }

    protected static MTBlockItem registerBlockItem(String id, ItemStack item, String name) {
        MTBlockItem mtItem = new MTBlockItem(id, item, name);
        registeredItems.put(mtItem.getID(), mtItem);
        return mtItem;
    }

    public static final MTItem TEST_ITEM;
    public static final MTBlockItem TEST_BLOCK;
    public static final MTItem TEST_TIERED_ULV;
    public static final MTItem TEST_TIERED_LV;
    public static final MTItem TEST_TIERED_MV;
    public static final MTItem TEST_TIERED_HV;
    public static final MTItem TEST_TIERED_EV;
    public static final MTItem TEST_TIERED_IV;
    public static final MTItem TEST_TIERED_LUV;
    public static final MTItem TEST_TIERED_ZPM;
    public static final MTItem TEST_TIERED_UV;
    public static final MTBlockItem TEST_TILE_ENTITY;
    static{
        TEST_ITEM = registerItem("test_item", new ItemStack(Items.DIAMOND), "Test Item");
        TEST_BLOCK = registerBlockItem("test_block", new ItemStack(Items.STONE), "Test Block");
        TEST_TIERED_ULV = registerItem("test_tiered.ulv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[0]);
        TEST_TIERED_LV = registerItem("test_tiered.lv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[1]);
        TEST_TIERED_MV = registerItem("test_tiered.mv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[2]);
        TEST_TIERED_HV = registerItem("test_tiered.hv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[3]);
        TEST_TIERED_EV = registerItem("test_tiered.ev", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[4]);
        TEST_TIERED_IV = registerItem("test_tiered.iv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[5]);
        TEST_TIERED_LUV = registerItem("test_tiered.luv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[6]);
        TEST_TIERED_ZPM = registerItem("test_tiered.zpm", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[7]);
        TEST_TIERED_UV = registerItem("test_tiered.uv", new ItemStack(Items.LAPIS_LAZULI),"Test TileEntityTiered "+ MTValues.VNF[8]);
        TEST_TILE_ENTITY = registerBlockItem("test_tile_entity", new ItemStack(Items.FURNACE),"Test Tile Entity");
    }
}
