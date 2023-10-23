package org.guyapooye.mtp.recipes;

import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.guyapooye.mtp.blocks.MTBlock;
import org.guyapooye.mtp.items.MTItem;
import org.guyapooye.mtp.items.MTItemStack;
import org.guyapooye.mtp.recipes.ingredients.MTRecipeInput;
import org.guyapooye.mtp.recipes.ingredients.MTRecipeStackInput;
import org.guyapooye.mtp.utils.MTUtils;
import org.guyapooye.mtp.utils.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
@SuppressWarnings("unchecked")
public class RecipeBuilder<R extends RecipeBuilder<R>> {
    protected RecipeMap<R> recipeMap;
    @Getter
    protected final List<MTRecipeInput> inputs;
    @Getter
    protected final List<MTItemStack> outputs;
    protected int duration, EUt;
    protected RecipePropertyStorage recipePropertyStorage = null;
    protected MTUtils.EnumValidationResult recipeStatus = MTUtils.EnumValidationResult.VALID;
    protected Consumer<R> onBuildAction = null;
    protected boolean recipePropertyStorageErrored = false;
    protected RecipeBuilder() {
        this.inputs = NonNullList.create();
        this.outputs = NonNullList.create();
    }
    protected RecipeBuilder(RecipeBuilder<R> recipeBuilder) {
        this.recipeMap = recipeBuilder.recipeMap;
        this.inputs = NonNullList.create();
        this.inputs.addAll(recipeBuilder.getInputs());
        this.outputs = NonNullList.create();
        this.outputs.addAll(recipeBuilder.getOutputs());
        this.duration = recipeBuilder.duration;
        this.EUt = recipeBuilder.EUt;
        this.onBuildAction = recipeBuilder.onBuildAction;
    }
    public R input(MTRecipeInput input) {
        this.inputs.add(input);
        return (R) this;
    }
    public R input(ItemStack input) {
        this.inputs.add(MTRecipeInput.getOrCreate(new MTItemStack(input)));
        return (R) this;
    }
    public R input(MTItemStack input) {
        this.inputs.add(MTRecipeInput.getOrCreate(input));
        return (R) this;
    }
    public R input(MTItem input) {
        this.inputs.add(MTRecipeInput.getOrCreate(new MTItemStack(input)));
        return (R) this;
    }
    public R input(MTBlock input) {
        this.inputs.add(MTRecipeInput.getOrCreate((new MTItemStack(input.getBlockItem()))));
        return (R) this;
    }
    public R inputs(ItemStack... inputs) {
        for (ItemStack input : inputs) {
            if (input == null || input.isEmpty()) {
                recipeStatus = MTUtils.EnumValidationResult.INVALID;
                continue;
            }
            this.inputs.add(MTRecipeStackInput.getOrCreate(new MTItemStack(input)));
        }
        return (R) this;
    }
    public R inputs(MTItemStack... inputs) {
        for (MTItemStack input : inputs) {
            if (input == null || input.itemStack.isEmpty()) {
                recipeStatus = MTUtils.EnumValidationResult.INVALID;
                continue;
            }
            this.inputs.add(MTRecipeStackInput.getOrCreate(input));
        }
        return (R) this;
    }
    public R inputs(MTRecipeInput... inputs) {
        for (MTRecipeInput input : inputs) {
            if (input.getAmount() < 0) {
                recipeStatus = MTUtils.EnumValidationResult.INVALID;
                continue;
            }
            this.inputs.add(input);
        }
        return (R) this;
    }
    public R clearInputs() {
        this.inputs.clear();
        return (R) this;
    }
    public R duration(int duration) {
        this.duration = duration;
        return (R) this;
    }

    public R EUt(int EUt) {
        this.EUt = EUt;
        return (R) this;
    }
    public R setRecipeMap(RecipeMap<R> recipeMap) {
        this.recipeMap = recipeMap;
        return (R) this;
    }
    public R copy() {
        return (R) new RecipeBuilder<>(this);
    }
    protected R onBuild(Consumer<R> consumer) {
        this.onBuildAction = consumer;
        return (R) this;
    }
    protected R invalidateOnBuildAction() {
        this.onBuildAction = null;
        return (R) this;
    }
    public void buildAndRegister() {
        if (onBuildAction != null) {
            onBuildAction.accept((R) this);
        }
        ValidationResult<Recipe> validationResult = build();
        recipeMap.addRecipe(validationResult);
    }
    public ValidationResult<Recipe> build() {
        return ValidationResult.newResult(finalizeAndValidate(), new Recipe(inputs, outputs, duration, EUt));
    }
    protected MTUtils.EnumValidationResult finalizeAndValidate() {
        return recipePropertyStorageErrored ? MTUtils.EnumValidationResult.INVALID : validate();
    }
    protected MTUtils.EnumValidationResult validate() {
        if (EUt == 0) {
            recipeStatus = MTUtils.EnumValidationResult.INVALID;
        }
        if (duration <= 0) {
            recipeStatus = MTUtils.EnumValidationResult.INVALID;
        }
        if (recipePropertyStorage != null) {
            recipePropertyStorage.freeze(true);
        }
        return recipeStatus;
    }
}
