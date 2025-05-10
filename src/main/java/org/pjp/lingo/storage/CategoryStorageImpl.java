package org.pjp.lingo.storage;

import java.nio.file.Paths;
import java.util.Collection;

import org.eclipse.serializer.reflect.ClassLoaderProvider;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.pjp.lingo.model.Category;
import org.pjp.lingo.model.DataRoot;
import org.springframework.stereotype.Component;

@Component
public class CategoryStorageImpl implements CategoryStorage {

    private final EmbeddedStorageManager storageManager;

    public CategoryStorageImpl() {
        super();

        storageManager = EmbeddedStorage.Foundation(Paths.get("category.db")).onConnectionFoundation(cf -> cf
                .setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
                .start();

        DataRoot root = (DataRoot) storageManager.root();

        if (root == null) {
            root = new DataRoot();
            storageManager.setRoot(root);
            storageManager.storeRoot();
        }
    }

    @Override
    public void store(Category category) {
        DataRoot root = (DataRoot) storageManager.root();
        root.getCategories().put(category.name(), category);
        storageManager.store(root.getCategories());
    }

    @Override
    public Category load(String name) {
        DataRoot root = (DataRoot) storageManager.root();
        return root.getCategories().get(name);
    }

    @Override
    public Collection<Category> list() {
        DataRoot root = (DataRoot) storageManager.root();
        return root.getCategories().values();
    }

}
