package org.guyapooye.mtp.recipes.ingredients;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.concurrent.Computable;
import org.guyapooye.mtp.items.MTItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class MapStackTagIngredient extends MapStackIngredient {
    protected MTRecipeInput gtRecipeInput = null;

    public MapStackTagIngredient(MTItemStack stack) {
        super(stack.itemStack,stack.getIdentifier(),stack.itemStack.getOrCreateTag());
    }

    public MapStackTagIngredient(MTItemStack s, MTRecipeInput gtRecipeInput) {
        super(s, gtRecipeInput);
        this.gtRecipeInput = gtRecipeInput;
    }

    @Nonnull
    public static List<AbstractMapIngredient> from(@Nonnull MTRecipeInput r) {
        ObjectArrayList<AbstractMapIngredient> list = new ObjectArrayList<>();
        for (MTItemStack s : r.getInputStacks()) {
            list.add(new MapStackTagIngredient(s, r));
        }
        return list;
    }

    @Override
    protected int hash() {
        return stack.itemStack.getItem().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MapStackTagIngredient other) {
            if (this.stack.itemStack.getItem() != other.stack.itemStack.getItem()) {
                return false;
            }
            if (this.tag != other.tag) {
                return false;
            }
            if (this.gtRecipeInput != null) {
                if (other.gtRecipeInput != null) {
                    return gtRecipeInput.equalIgnoreAmount(other.gtRecipeInput);
                }
            } else if (other.gtRecipeInput != null) {
                return other.gtRecipeInput.acceptsStack(this.stack);
            }
        }
        return false;
    }

    @Override
    public boolean isSpecialIngredient() {
        return true;
    }
}
