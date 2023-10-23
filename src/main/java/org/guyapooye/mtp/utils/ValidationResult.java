package org.guyapooye.mtp.utils;

public class ValidationResult<T> {
    private final MTUtils.EnumValidationResult type;
    private final T result;

    public ValidationResult(MTUtils.EnumValidationResult typeIn, T resultIn) {
        this.type = typeIn;
        this.result = resultIn;
    }

    public MTUtils.EnumValidationResult getType() {
        return this.type;
    }

    public T getResult() {
        return this.result;
    }

    public static <T> ValidationResult<T> newResult(MTUtils.EnumValidationResult result, T value) {
        return new ValidationResult<>(result, value);
    }
}
