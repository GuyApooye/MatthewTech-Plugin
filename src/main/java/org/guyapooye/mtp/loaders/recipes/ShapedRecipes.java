package org.guyapooye.mtp.loaders.recipes;

import net.minecraft.world.item.Items;
import org.guyapooye.mtp.utils.MTItems;

public class ShapedRecipes {
    public static void init() {
        RecipeHandler.addShapedRecipe("test_ulv", MTItems.TEST_TIERED_ULV,"AAA","A A", "AAA",'A', Items.COPPER_INGOT);
        RecipeHandler.addShapedRecipe("test_lv", MTItems.TEST_TIERED_LV,"AAA","ABA", "AAA",'A', Items.IRON_INGOT, 'B', MTItems.TEST_TIERED_ULV);
    }
}
