package org.guyapooye.mtp.recipes;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.guyapooye.mtp.items.MTItemStack;
import org.guyapooye.mtp.recipes.ingredients.MTRecipeInput;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class Recipe {
    private static final NonNullList<MTItemStack> EMPTY = NonNullList.create();
    private final List<MTRecipeInput> inputs;
    private final NonNullList<MTItemStack> outputs;
    @Getter
    private final int duration, EUt;
    private int hashCode;

    public Recipe(@Nonnull List<MTRecipeInput> inputs, List<MTItemStack> outputs, int duration, int EUt) {
        if (inputs.isEmpty()) {
            this.inputs = Collections.emptyList();
        } else {
            this.inputs = NonNullList.create();
            this.inputs.addAll(inputs);
            this.inputs.sort((ing1, ing2) -> Boolean.compare(ing1.isNonConsumable(), ing2.isNonConsumable()));
        }
        if (outputs.isEmpty()) {
            this.outputs = EMPTY;
        } else {
            this.outputs = NonNullList.create();
            this.outputs.addAll(outputs);
        }
        this.duration = duration;
        this.EUt = EUt;
    }
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    private int makeHashCode() {
        int hash = 31 * hashInputs();
        //hash = 31 * hash + hashFluidList(this.fluidInputs);
        return hash;
    }
    private int hashInputs() {
        int hash = 0;
        for (MTRecipeInput recipeIngredient : this.inputs) {
            hash = 31 * hash + recipeIngredient.getHashedID();
        }
        return hash;
    }
    private boolean hasSameInputs(Recipe otherRecipe) {
        List<MTItemStack> otherStackList = new ObjectArrayList<>(otherRecipe.inputs.size());
        for (MTRecipeInput otherInputs : otherRecipe.inputs) {
            otherStackList.addAll(List.of(otherInputs.getInputStacks()));
        }
        if (!this.matchesItems(otherStackList).getLeft()) return false;

        List<MTItemStack> thisStackList = new ObjectArrayList<>(this.inputs.size());
        for (MTRecipeInput thisInputs : otherRecipe.inputs) {
            thisStackList.addAll(List.of(thisInputs.getInputStacks()));
        }
        return otherRecipe.matchesItems(thisStackList).getLeft();
    }
    private Pair<Boolean, int[]> matchesItems(List<MTItemStack> inputs) {
        int[] itemAmountInSlot = new int[inputs.size()];
        int indexed = 0;

        List<MTRecipeInput> gtRecipeInputs = this.inputs;
        for (MTRecipeInput ingredient : gtRecipeInputs) {
            int ingredientAmount = ingredient.getAmount();
            for (int j = 0; j < inputs.size(); j++) {
                MTItemStack inputStack = inputs.get(j);

                if (j == indexed) {
                    itemAmountInSlot[j] = inputStack.itemStack.isEmpty() ? 0 : inputStack.itemStack.getCount();
                    indexed++;
                }

                if (inputStack.itemStack.isEmpty() || !ingredient.acceptsStack(inputStack))
                    continue;
                int itemAmountToConsume = Math.min(itemAmountInSlot[j], ingredientAmount);
                ingredientAmount -= itemAmountToConsume;
                if (!ingredient.isNonConsumable()) itemAmountInSlot[j] -= itemAmountToConsume;
                if (ingredientAmount == 0) break;
            }
            if (ingredientAmount > 0)
                return Pair.of(false, itemAmountInSlot);
        }
        int[] retItemAmountInSlot = new int[indexed];
        System.arraycopy(itemAmountInSlot, 0, retItemAmountInSlot, 0, indexed);

        return Pair.of(true, retItemAmountInSlot);
    }
    public List<MTRecipeInput> getInputs() {
        return inputs;
    }
    public List<MTItemStack> getOutputs() {
        return outputs;
    }

    protected static class TagToStack implements Object2ObjectMap.Entry<CompoundTag, ItemStack> {
        CompoundTag tag;
        ItemStack stack;

        TagToStack(CompoundTag tag, ItemStack stack) {
            this.tag = tag;
            this.stack = stack;
        }

        TagToStack(ItemStack stack) {
            this.tag = stack.getOrCreateTag();
            this.stack = stack;
        }

        @Override
        public CompoundTag getKey() {
            return tag;
        }

        @Override
        public ItemStack getValue() {
            return stack;
        }

        @Override
        public ItemStack setValue(ItemStack value) {
            return stack = value;
        }
    }

}
