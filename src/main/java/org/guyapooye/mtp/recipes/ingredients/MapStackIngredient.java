package org.guyapooye.mtp.recipes.ingredients;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.guyapooye.mtp.items.MTItem;
import org.guyapooye.mtp.items.MTItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class MapStackIngredient extends AbstractMapIngredient{

    protected MTItemStack stack;
    protected String identifier;
    protected CompoundTag tag;
    protected MTRecipeInput gtRecipeInput = null;

    public MapStackIngredient(ItemStack stack, String identifier, CompoundTag tag) {
        this.stack = new MTItemStack(stack);
        this.identifier = identifier;
        this.tag = tag;
    }
    public MapStackIngredient(MTItem stack) {
        this.stack = new MTItemStack(stack);
        this.identifier = stack.getID();
        this.tag = stack.getTag();
    }

    public MapStackIngredient(MTItemStack stack, MTRecipeInput gtRecipeInput) {
        this.stack = stack;
        this.identifier = stack.getIdentifier();
        this.tag = stack.itemStack.getOrCreateTag();
        this.gtRecipeInput = gtRecipeInput;
    }

    @Nonnull
    public static List<AbstractMapIngredient> from(@Nonnull MTRecipeInput r) {
        ObjectArrayList<AbstractMapIngredient> list = new ObjectArrayList<>();
        for (MTItemStack s : r.getInputStacks()) {
            list.add(new MapStackIngredient(s, r));
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            MapStackIngredient other = (MapStackIngredient) o;
            if (this.stack.itemStack.getItem() != other.stack.itemStack.getItem()) {
                return false;
            }
            if (this.identifier != other.identifier) {
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
    protected int hash() {
        int hash = stack.itemStack.getItem().hashCode() * 31;
        hash += 31 * this.identifier.hashCode();
        hash += 31 * (this.tag != null ? this.tag.hashCode() : 0);
        return hash;
    }
}
