package org.guyapooye.mtp.recipes.ingredients;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.guyapooye.mtp.items.MTItemStack;

import javax.annotation.Nullable;

public abstract class MTRecipeInput {
    protected boolean isConsumable;
    @Getter
    protected int amount;
    public static ObjectOpenHashSet<MTRecipeInput> INSTANCES = new ObjectOpenHashSet<>(15072);
    static MTRecipeInput getFromCache(MTRecipeInput realIngredient) {
        MTRecipeInput cachedIngredient = INSTANCES.get(realIngredient);
        if (cachedIngredient == null) {
            INSTANCES.add(cachedIngredient = realIngredient);
        }
        return cachedIngredient;
    }
    public MTRecipeInput setNonConsumable() {
        MTRecipeInput copy = this.copy();
        copy.isConsumable = false;
        return getFromCache(copy);
    }
    public MTItemStack[] getInputStacks() {
        return null;
    }
    public int getHashedID() {
        return -1;
    }
    public boolean isNonConsumable() {
        return !isConsumable;
    }
    public static MTRecipeInput getOrCreate(String id, int amount) {
        return getFromCache(new MTRecipeIDInput(id, amount));
    }
    public static MTRecipeInput getOrCreate(MTItemStack stack) {
        return getFromCache(new MTRecipeStackInput(stack));
    }

    protected abstract MTRecipeInput copy();
    public boolean acceptsStack(@Nullable MTItemStack input) {
        return true;
    }
    public abstract boolean equalIgnoreAmount(MTRecipeInput input);
}
