package io.github.pigaut.rpg.core.language;

public class TranslationNotFoundException extends RuntimeException {

    public TranslationNotFoundException(String translationId) {
        super("Translation with id: " + translationId + " not found in language file.");
    }

}
