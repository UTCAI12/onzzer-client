package fr.utc.onzzer.client.common.util;

public record ValidationResult<T>(T value, boolean hasError) {

}
