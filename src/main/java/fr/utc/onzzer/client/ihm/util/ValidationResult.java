package fr.utc.onzzer.client.ihm.util;

public record ValidationResult<T>(T value, boolean hasError) {

}
