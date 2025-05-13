package org.pjp.lingo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataRoot {

    private Map<String, Category> categories = new HashMap<>();

    public Map<String, Category> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Category> categories) {
        this.categories = categories;
    }

    private Map<String, Set<Game>> games = new HashMap<>();

    public Map<String, Set<Game>> getGames() {
        return games;
    }

    public void setGames(Map<String, Set<Game>> games) {
        this.games = games;
    }

}
