package org.guyapooye.mtp.recipes;

import java.util.List;
import java.util.Objects;

public abstract class RecipeProperty<T> {
    private final Class<T> type;
    private final String key;

    protected RecipeProperty(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    public int getInfoHeight(Object value) {
        return 10;
    }

    public boolean isOfType(Class<?> otherType) {
        return this.type == otherType;
    }

    public String getKey() {
        return key;
    }

    public T castValue(Object value) {
        return this.type.cast(value);
    }


    public boolean isHidden() {
        return false;
    }


    public boolean hideTotalEU() {
        return false;
    }


    public boolean hideEUt() {
        return false;
    }


    public boolean hideDuration() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeProperty<?> that = (RecipeProperty<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key);
    }
}
