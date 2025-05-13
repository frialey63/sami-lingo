package org.pjp.lingo.storage;

import java.util.Collection;

import org.pjp.lingo.model.Category;
import org.pjp.lingo.model.DataRoot;
import org.springframework.stereotype.Component;

@Component
public class CategoryStorageImpl implements CategoryStorage {

    private final StorageManager storageManager;

    public CategoryStorageImpl(StorageManager storageManager) {
        super();
        this.storageManager = storageManager;
    }

    @Override
    public void save(Category category) {
        DataRoot root = (DataRoot) storageManager.getStorageManager().root();
        root.getCategories().put(category.name(), category);
        storageManager.getStorageManager().store(root.getCategories());
    }

    @Override
    public Category load(String name) {
        DataRoot root = (DataRoot) storageManager.getStorageManager().root();
        return root.getCategories().get(name);
    }

    @Override
    public Collection<Category> getAll() {
        DataRoot root = (DataRoot) storageManager.getStorageManager().root();
        return root.getCategories().values();
    }

}
