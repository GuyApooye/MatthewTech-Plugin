package org.guyapooye.mtp.recipes.ingredients;

import net.minecraft.core.NonNullList;

import net.minecraft.world.item.ItemStack;
import org.guyapooye.mtp.items.MTItemStack;

import javax.annotation.Nullable;

public class MTRecipeStackInput extends MTRecipeInput{
    MTItemStack[] inputStacks;

    protected MTRecipeStackInput(MTItemStack stack, int amount) {
        this(new MTItemStack[]{stack}, amount);
    }

    protected MTRecipeStackInput(MTItemStack[] stack, int amount) {
        this.amount = amount;
        NonNullList<MTItemStack> lst = NonNullList.create();
        this.inputStacks = lst.stream().map(is -> {
            is = is.copy();
            is.itemStack.setCount(this.amount);
            return is;
        }).toArray(MTItemStack[]::new);
    }

    protected MTRecipeStackInput(MTItemStack... stacks) {
        this(stacks, stacks[0].itemStack.getCount());
    }

    public static MTRecipeInput getOrCreate(ItemStack stack, int amount) {
        return getFromCache(new MTRecipeStackInput(new MTItemStack(stack), amount));
    }
    public static MTRecipeInput getOrCreate(MTItemStack stack, int amount) {
        return getFromCache(new MTRecipeStackInput(stack, amount));
    }

    @Override
    protected MTRecipeInput copy() {
        return new MTRecipeStackInput(inputStacks,amount);
    }
    @Override
    public int hashCode() {
        int hash = 1;
        for (MTItemStack stack : inputStacks) {
            hash = 31 * hash + stack.itemStack.getItem().hashCode();
            hash = 31 * hash + stack.itemStack.getOrCreateTag().hashCode();
        }
        hash = 31 * hash + this.amount;
        hash = 31 * hash + (this.isConsumable ? 1 : 0);
        return hash;
    }
    @Override
    public boolean equalIgnoreAmount(MTRecipeInput input) {
        if (this == input) return true;
        if (!(input instanceof MTRecipeStackInput)) return false;
        MTRecipeStackInput other = (MTRecipeStackInput) input;


        if (this.inputStacks.length != other.inputStacks.length) return false;
        for (int i = 0; i < this.inputStacks.length; i++) {
            if (!ItemStack.isSameItem(this.inputStacks[i].itemStack, other.inputStacks[i].itemStack) || !ItemStack.isSameItemSameTags(this.inputStacks[i].itemStack, other.inputStacks[i].itemStack))
                return false;
        }
        return true;
    }
}
