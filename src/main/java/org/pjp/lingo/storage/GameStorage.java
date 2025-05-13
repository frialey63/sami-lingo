package org.pjp.lingo.storage;

import java.util.Set;

import org.pjp.lingo.model.Game;

public interface GameStorage {

    void save(String user, Game game);

    Set<Game> getAll(String user);
}
