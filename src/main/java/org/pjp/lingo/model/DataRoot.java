package org.pjp.lingo.model;

import java.util.HashMap;
import java.util.Map;

public class DataRoot {

    private Map<String, Category> categories = new HashMap<>();

    public Map<String, Category> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Category> categories) {
        this.categories = categories;
    }

    private Map<String, Game> games = new HashMap<>();

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGames(Map<String, Game> games) {
        this.games = games;
    }

}
