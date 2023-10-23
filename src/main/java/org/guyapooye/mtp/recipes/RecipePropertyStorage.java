package org.guyapooye.mtp.recipes;

import java.util.Map;
import java.util.Set;

public interface RecipePropertyStorage {
    String STACKTRACE = "Stacktrace:";

    boolean store(RecipeProperty<?> recipeProperty, Object value);

    boolean remove(RecipeProperty<?> recipeProperty);

    void freeze(boolean frozen);

    RecipePropertyStorage copy();

    int getSize();


    Set<Map.Entry<RecipeProperty<?>, Object>> getRecipeProperties();

    <T> T getRecipePropertyValue(RecipeProperty<T> recipeProperty, T defaultValue);

    boolean hasRecipeProperty(RecipeProperty<?> recipeProperty);

    Set<String> getRecipePropertyKeys();

    Set<RecipeProperty<?>> getPropertyTypes();

    Object getRawRecipePropertyValue(String key);

}
