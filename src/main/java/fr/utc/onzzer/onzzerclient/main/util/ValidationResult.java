package fr.utc.onzzer.onzzerclient.main.util;

public record ValidationResult<T>(T value, boolean hasError) {

}
