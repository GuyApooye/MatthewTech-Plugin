package org.guyapooye.mtp.loaders.recipes;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Block;
import org.bukkit.inventory.ItemStack;
import org.guyapooye.mtp.blocks.MTBlock;
import org.guyapooye.mtp.items.MTItem;
import org.guyapooye.mtp.recipes.types.MTShapedRecipe;

import javax.annotation.Nonnull;
import java.util.*;

public class RecipeHandler {
    @Nonnull
    public static Object[] finalizeShapedRecipeInput(Object... recipe) {
        for (byte i = 0; i < recipe.length; i++) {
            recipe[i] = finalizeIngredient(recipe[i]);
        }
        int idx = 0;
        Collection<Object> recipeList = new ArrayList<>(Arrays.asList(recipe));

        while (recipe[idx] instanceof String) {
            StringBuilder s = new StringBuilder((String) recipe[idx++]);
            while (s.length() < 3) s.append(" ");
            if (s.length() > 3) throw new IllegalArgumentException("Recipe row cannot be larger than 3. Index: " + idx);
        }
        return recipeList.toArray();
    }
    @Nonnull
    public static Object finalizeIngredient(@Nonnull Object ingredient) {
        if (!(ingredient instanceof ItemStack
                || ingredient instanceof Item
                || ingredient instanceof Block
                || ingredient instanceof MTItem
                || ingredient instanceof MTBlock
                || ingredient instanceof String
                || ingredient instanceof Character
                || ingredient instanceof Boolean
                || ingredient instanceof Ingredient)) {
            throw new IllegalArgumentException(ingredient.getClass().getSimpleName() + " type is not suitable for crafting input.");
        }
        return ingredient;
    }
    public static void addShapedRecipe(@Nonnull String id, @Nonnull net.minecraft.world.item.ItemStack result, @Nonnull Object... recipe) {
        ShapedRecipe shapedOreRecipe = new MTShapedRecipe(id, result.copy(), finalizeShapedRecipeInput(recipe));
        registerRecipe(shapedOreRecipe);
    }
    public static void addShapedRecipe(@Nonnull String id, @Nonnull MTItem result, @Nonnull Object... recipe) {
        addShapedRecipe(id, result.getModifiableItem(), recipe);
    }
    public static void addShapedRecipe(@Nonnull String id, @Nonnull MTBlock result, @Nonnull Object... recipe) {
        addShapedRecipe(id, result.getBlockItem(), recipe);
    }
    private static void registerRecipe(@Nonnull CraftingRecipe recipe) {
        MinecraftServer.getServer().getRecipeManager().addRecipe(recipe);
    }
}
