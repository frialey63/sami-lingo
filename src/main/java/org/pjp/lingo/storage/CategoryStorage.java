package org.pjp.lingo.storage;

import java.util.Collection;

import org.pjp.lingo.model.Category;

public interface CategoryStorage {

    void save(Category category);

    Category load(String name);

    Collection<Category> getAll();
}
