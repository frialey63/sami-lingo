package org.pjp.lingo.model;

import java.util.HashSet;
import java.util.Set;

public class Progress {

    private final Set<String> words = new HashSet<>();

    public void update(String word) {
        words.add(word);
    }

    public boolean excludes(String other) {
        return !words.contains(other);
    }

    public int count() {
        return words.size();
    }

    public void reset() {
        words.clear();
    }
}
