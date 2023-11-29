package fr.utc.onzzer.client.hmi.util;

public record ValidationResult<T>(T value, boolean hasError) {

}
