package org.pjp.lingo.storage;

import java.util.HashSet;
import java.util.Set;

import org.pjp.lingo.model.DataRoot;
import org.pjp.lingo.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameStorageImpl implements GameStorage {

    private final StorageManager storageManager;

    public GameStorageImpl(StorageManager storageManager) {
        super();
        this.storageManager = storageManager;
    }

    @Override
    public void save(String user, Game game) {
        DataRoot root = (DataRoot) storageManager.getStorageManager().root();

        Set<Game> set = root.getGames().get(user);
        if (set == null) {
            set = new HashSet<>();
        }

        set.add(game);

        root.getGames().put(user, set);

        storageManager.getStorageManager().store(root.getGames().get(user));
    }

    @Override
    public Set<Game> getAll(String user) {
        DataRoot root = (DataRoot) storageManager.getStorageManager().root();

        Set<Game> set = root.getGames().get(user);
        if (set == null) {
            set = new HashSet<>();
        }

        return set;
    }

}
