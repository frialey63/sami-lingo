package org.pjp.lingo.storage;

import java.nio.file.Paths;

import org.eclipse.serializer.reflect.ClassLoaderProvider;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.pjp.lingo.model.DataRoot;
import org.springframework.stereotype.Component;

@Component
public class StorageManager {

    private static final String DATABASE_NAME = "lingo.db";

    private final EmbeddedStorageManager storageManager;

    public StorageManager() {
        super();

        storageManager = EmbeddedStorage.Foundation(Paths.get(DATABASE_NAME)).onConnectionFoundation(cf -> cf
                .setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
                .start();

        DataRoot root = (DataRoot) storageManager.root();

        if (root == null) {
            root = new DataRoot();
            storageManager.setRoot(root);
            storageManager.storeRoot();
        }
    }

    public EmbeddedStorageManager getStorageManager() {
        return storageManager;
    }


}
