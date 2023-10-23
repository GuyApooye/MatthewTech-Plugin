package org.guyapooye.mtp.recipes;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import org.guyapooye.mtp.recipes.ingredients.AbstractMapIngredient;
import org.guyapooye.mtp.recipes.ingredients.MTRecipeInput;
import org.guyapooye.mtp.recipes.ingredients.MapStackIngredient;
import org.guyapooye.mtp.utils.MTUtils;
import org.guyapooye.mtp.utils.ValidationResult;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.*;

public class RecipeMap<R extends RecipeBuilder<R>> {

    private static final Map<String, RecipeMap<?>> RECIPE_MAP_REGISTRY = new Object2ReferenceOpenHashMap<>();
    private static final Comparator<Recipe> RECIPE_DURATION_THEN_EU = Comparator.comparingInt(Recipe::getDuration).thenComparingInt(Recipe::getEUt).thenComparing(Recipe::hashCode);
    private static boolean foundInvalidRecipe;
    private static final WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> ingredientRoot = new WeakHashMap<>();
    public final String unlocalizedName;
    private final R recipeBuilderSample;
    private final Branch lookup = new Branch();
    @Getter
    private int maxInputs;
    @Getter
    private int maxOutputs;
    @Getter
    private boolean allowEmptyOutput;

    public boolean addRecipe(@Nonnull ValidationResult<Recipe> validationResult) {
        validationResult = postValidateRecipe(validationResult);
        switch (validationResult.getType()) {
            case SKIP -> {
                return false;
            }
            case INVALID -> {
                setFoundInvalidRecipe(true);
                return false;
            }
        }
        Recipe recipe = validationResult.getResult();

        return compileRecipe(recipe);
    }
    @Nonnull
    protected static Map<AbstractMapIngredient, Either<Recipe, Branch>> determineRootNodes(@Nonnull AbstractMapIngredient ingredient,
                                                                                           @Nonnull Branch branchMap) {
        return ingredient.isSpecialIngredient() ? branchMap.getSpecialNodes() : branchMap.getNodes();
    }
    public boolean compileRecipe(Recipe recipe) {
        if (recipe == null) return false;
        List<List<AbstractMapIngredient>> items = fromRecipe(recipe);
        if (!recurseIngredientTreeAdd(recipe, items, lookup, 0, 0)) return false;
        return true;
    }
    private boolean recurseIngredientTreeAdd(@Nonnull Recipe recipe, @Nonnull List<List<AbstractMapIngredient>> ingredients,
                                             @Nonnull Branch branchMap, int index, int count) {
        if (count >= ingredients.size()) return true;
        if (index >= ingredients.size()) {
            throw new RuntimeException("Index out of bounds for recurseItemTreeAdd, should not happen");
        }
        // Loop through NUMBER_OF_INGREDIENTS times.

        // the current contents to be added to a node in the branch
        final List<AbstractMapIngredient> current = ingredients.get(index);
        final Branch branchRight = new Branch();
        Either<Recipe, Branch> r;

        // for every ingredient, add it to a node
        for (AbstractMapIngredient obj : current) {
            // determine the root nodes
            Map<AbstractMapIngredient, Either<Recipe, Branch>> targetMap = determineRootNodes(obj, branchMap);

            // Either add the recipe or create a branch.
            r = targetMap.compute(obj, (k, v) -> {
                if (count == ingredients.size() - 1) {
                    // handle very last ingredient
                    if (v != null) {
                        return v;
                    } else {
                        // nothing exists for this path, so end with the recipe
                        return Either.left(recipe);
                    }
                } else if (v == null) {
                    // no existing ingredient is present, so use the new one
                    return Either.right(branchRight);
                }
                // there is an existing ingredient here already, so use it
                return v;
            });

            // left branches are always either empty or contain recipes.
            // If there's a recipe present, the addition is finished for this ingredient
            if (r.left().isPresent()) {
                if (r.left().get() == recipe) {
                    // Cannot return here, since each ingredient to add is a separate path to the recipe
                    continue;
                } else {
                    // exit if a different recipe is already present for this path
                    return false;
                }
            }

            // recursive part: apply the addition for the next ingredient in the list, for the right branch.
            // the right branch only contains ingredients, or is empty when the left branch is present
            boolean addedNextBranch = r.right()
                    .filter(m -> recurseIngredientTreeAdd(recipe, ingredients, m, (index + 1) % ingredients.size(), count + 1))
                    .isPresent();

            if (!addedNextBranch) {
                // failed to add the next branch, so undo any made changes
                if (count == ingredients.size() - 1) {
                    // was the final ingredient, so the mapping of it to a recipe needs to be removed
                    targetMap.remove(obj);
                } else {
                    // was a regular ingredient
                    if (targetMap.get(obj).right().isPresent()) {
                        // if something was put into the map
                        if (targetMap.get(obj).right().get().isEmptyBranch()) {
                            // if what was put was empty (invalid), remove it
                            targetMap.remove(obj);
                        }
                    }
                }
                // because a branch addition failure happened, fail the recipe addition for this step
                return false;
            }
        }
        // recipe addition was successful
        return true;
    }
    @Nonnull
    protected List<List<AbstractMapIngredient>> fromRecipe(@Nonnull Recipe r) {
        List<List<AbstractMapIngredient>> list = new ObjectArrayList<>((r.getInputs().size()));
        if (r.getInputs().size() > 0) {
            buildFromRecipeItems(list, uniqueIngredientsList(r.getInputs()));
        }
//        if (r.getFluidInputs().size() > 0) {
//            buildFromRecipeFluids(list, r.getFluidInputs());
//        }
        return list;
    }
    @Nonnull
    public static List<MTRecipeInput> uniqueIngredientsList(@Nonnull Collection<MTRecipeInput> inputs) {
        List<MTRecipeInput> list = new ObjectArrayList<>(inputs.size());
        for (MTRecipeInput item : inputs) {
            boolean isEqual = false;
            for (MTRecipeInput obj : list) {
                if (item.equalIgnoreAmount(obj)) {
                    isEqual = true;
                    break;
                }
            }
            if (isEqual) continue;
            list.add(item);
        }
        return list;
    }
    protected void buildFromRecipeItems(List<List<AbstractMapIngredient>> list, @Nonnull List<MTRecipeInput> inputs) {
        for (MTRecipeInput r : inputs) {
            // input must be represented as a list of possible stacks
            List<AbstractMapIngredient> ingredients;
            ingredients = MapStackIngredient.from(r);

            for (int i = 0; i < ingredients.size(); i++) {
                AbstractMapIngredient mappedIngredient = ingredients.get(i);
                // attempt to use the cached value if possible, otherwise cache for the next time
                WeakReference<AbstractMapIngredient> cached = ingredientRoot.get(mappedIngredient);
                if (cached != null && cached.get() != null) {
                    ingredients.set(i, cached.get());
                } else {
                    ingredientRoot.put(mappedIngredient, new WeakReference<>(mappedIngredient));
                }
            }
            list.add(ingredients);
        }
    }
    protected static void retrieveCachedIngredient(List<List<AbstractMapIngredient>> list, @Nonnull AbstractMapIngredient defaultIngredient,
                                                   @Nonnull WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> cache) {
        WeakReference<AbstractMapIngredient> cached = cache.get(defaultIngredient);
        if (cached != null && cached.get() != null) {
            list.add(Collections.singletonList(cached.get()));
        } else {
            cache.put(defaultIngredient, new WeakReference<>(defaultIngredient));
            list.add(Collections.singletonList(defaultIngredient));
        }
    }
    public static void setFoundInvalidRecipe(boolean foundInvalidRecipe) {
        RecipeMap.foundInvalidRecipe |= foundInvalidRecipe;
    }
    public RecipeMap(@Nonnull String unlocalizedName,
                     @Nonnegative int maxInputs,
                     @Nonnegative int maxOutputs,
                     @Nonnull R defaultRecipeBuilder) {
        this.unlocalizedName = unlocalizedName;

        this.maxInputs = maxInputs;
        this.maxOutputs = maxOutputs;

        defaultRecipeBuilder.setRecipeMap(this);
        this.recipeBuilderSample = defaultRecipeBuilder;
        RECIPE_MAP_REGISTRY.put(unlocalizedName, this);
    }
    @Nonnull
    protected ValidationResult<Recipe> postValidateRecipe(@Nonnull ValidationResult<Recipe> validationResult) {
        MTUtils.EnumValidationResult recipeStatus = validationResult.getType();
        Recipe recipe = validationResult.getResult();

        boolean emptyInputs = recipe.getInputs().isEmpty();
        if (emptyInputs) {
            recipeStatus = MTUtils.EnumValidationResult.INVALID;
        }
        boolean emptyOutputs = !this.allowEmptyOutput && recipe.getEUt() > 0 &&
                recipe.getOutputs().isEmpty();
        if (emptyOutputs) {
            recipeStatus = MTUtils.EnumValidationResult.INVALID;
        }

        int amount = recipe.getInputs().size();
        if (amount > getMaxInputs()) {
            recipeStatus = MTUtils.EnumValidationResult.INVALID;
        }

        amount = recipe.getOutputs().size();
        if (amount > getMaxOutputs()) {
            recipeStatus = MTUtils.EnumValidationResult.INVALID;
        }

//        amount = recipe.getFluidInputs().size();
//        if (amount > getMaxFluidInputs()) {
//            GTLog.logger.error("Invalid amount of recipe fluid inputs. Actual: {}. Should be at most {}.", amount, getMaxFluidInputs());
//            GTLog.logger.error("Stacktrace:", new IllegalArgumentException("Invalid number of Fluid Inputs"));
//            recipeStatus = MTUtils.EnumValidationResult.INVALID;
//        }

//        amount = recipe.getFluidOutputs().size();
//        if (amount > getMaxFluidOutputs()) {
//            GTLog.logger.error("Invalid amount of recipe fluid outputs. Actual: {}. Should be at most {}.", amount, getMaxFluidOutputs());
//            GTLog.logger.error("Stacktrace:", new IllegalArgumentException("Invalid number of Fluid Outputs"));
//            recipeStatus = MTUtils.EnumValidationResult.INVALID;
//        }
        return ValidationResult.newResult(recipeStatus, recipe);
    }


}

