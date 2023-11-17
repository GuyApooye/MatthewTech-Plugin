package org.guyapooye.mtp.recipes.ingredients;

import org.guyapooye.mtp.items.MTItemStack;

import java.util.Objects;

import static org.guyapooye.mtp.items.MTItems.registeredItems;

public class MTRecipeIDInput extends MTRecipeInput{
    String id;
    MTItemStack[] inputStacks;
    public MTRecipeIDInput(String id, int amount) {
        this.amount = amount;
        this.id = id;
    }
    @Override
    protected MTRecipeIDInput copy() {
        MTRecipeIDInput copy = new MTRecipeIDInput(id, amount);
        copy.isConsumable = this.isConsumable;
        return copy;
    }

    @Override
    public boolean equalIgnoreAmount(MTRecipeInput input) {
        if (this == input) return true;
        if (!(input instanceof MTRecipeIDInput other)) {
            return false;
        }
        return Objects.equals(id, other.id);
    }
    @Override
    public MTItemStack[] getInputStacks() {
        if (this.inputStacks == null) {
            inputStacks = new MTItemStack[]{new MTItemStack(registeredItems.get(id).clone().setCount(amount))};
        }
        return inputStacks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, id, isConsumable);
    }
}
