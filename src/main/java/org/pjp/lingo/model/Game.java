package org.pjp.lingo.model;

public record Game(String categoryName,  boolean frenchToEnglish, int difficulty) {

    public boolean isEnglishToFrench() {
        return !frenchToEnglish;
    }

}
