package org.guyapooye.mtp.recipes;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Block;
import org.guyapooye.mtp.blocks.MTBlock;
import org.guyapooye.mtp.items.MTItem;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

public class MTShapedRecipe extends ShapedRecipe {


    public MTShapedRecipe(String id, @Nonnull ItemStack result, ShapedRecipeData data) {
        super(new ResourceLocation("matthewtech",id),"MatthewTech", CraftingBookCategory.MISC,data.width,data.height,data.ingredients, result,false);
    }
    public MTShapedRecipe(String id, @Nonnull ItemStack result, Object... recipe) {
        this(id,result, parseShaped(recipe));
    }

    public static ShapedRecipeData parseShaped(Object... recipe) {
        ShapedRecipeData ret = new ShapedRecipeData();
        StringBuilder shape = new StringBuilder();
        int idx = 0;

        if (recipe[idx] instanceof Boolean) {
            if (recipe[idx + 1] instanceof Object[]) recipe = (Object[]) recipe[idx + 1];
            else idx = 1;
        }

        if (recipe[idx] instanceof String[]) {
            String[] parts = ((String[]) recipe[idx++]);

            for (String s : parts) {
                ret.width = s.length();
                shape.append(s);
            }

            ret.height = parts.length;
        } else {
            while (recipe[idx] instanceof String) {
                String s = (String) recipe[idx++];
                shape.append(s);
                ret.width = s.length();
                ret.height++;
            }
        }

        if (ret.width * ret.height != shape.length() || shape.length() == 0) {
            StringBuilder err = new StringBuilder("Invalid shaped recipe: ");
            for (Object tmp : recipe) {
                err.append(tmp).append(", ");
            }
            throw new RuntimeException(err.toString());
        }

        HashMap<Character, Ingredient> itemMap = Maps.newHashMap();
        itemMap.put(' ', Ingredient.EMPTY);

        for (; idx < recipe.length; idx += 2) {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];
            Ingredient ing = getIngredient(in);

            if (' ' == chr) throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            if (ing != null) {
                itemMap.put(chr, ing);
            } else {
                StringBuilder err = new StringBuilder("Invalid shaped ore recipe: ");
                for (Object tmp : recipe) {
                    err.append(tmp).append(", ");
                }
                throw new RuntimeException(err.toString());
            }
        }

        ret.ingredients = NonNullList.withSize(ret.width * ret.height, Ingredient.EMPTY);

        Set<Character> keys = Sets.newHashSet(itemMap.keySet());
        keys.remove(' ');

        int x = 0;
        for (char chr : shape.toString().toCharArray()) {
            Ingredient ing = itemMap.get(chr);
            if (ing == null) {
                throw new IllegalArgumentException("Pattern references symbol '" + chr + "' but it's not defined in the key");
            }
            ret.ingredients.set(x++, ing);
            keys.remove(chr);
        }

        if (!keys.isEmpty()) {
            throw new IllegalArgumentException("Key defines symbols that aren't used in pattern: " + keys);
        }

        return ret;
    }
    public static Ingredient getIngredient(Object obj) {
        if(obj instanceof Ingredient ing) return ing;
        if(obj instanceof net.minecraft.world.item.ItemStack ing) return Ingredient.of(ing.copy());
        if(obj instanceof MTItem ing) return Ingredient.of(ing.getModifiableItem());
        if(obj instanceof MTBlock ing) return Ingredient.of(ing.getBlockItem().getModifiableItem());
        if(obj instanceof Item ing) return Ingredient.of(new net.minecraft.world.item.ItemStack(ing));
        if(obj instanceof Block ing) return Ingredient.of(new net.minecraft.world.item.ItemStack(ing.asItem()));
        return null;
    }
    public static final class ShapedRecipeData {
        public int width;
        public int height;
        NonNullList<Ingredient> ingredients;
    }
}
