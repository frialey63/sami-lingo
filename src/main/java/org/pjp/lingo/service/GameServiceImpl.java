package org.pjp.lingo.service;

import java.util.Set;

import org.pjp.lingo.model.Game;
import org.pjp.lingo.storage.GameStorage;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameStorage gameStorage;

    public GameServiceImpl(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    @Override
    public void saveCompleted(String user, Game game) {
        gameStorage.save(user, game);
    }

    @Override
    public Set<Game> getCompleted(String user) {
        return gameStorage.getAll(user);
    }

    @Override
    public void clearCompleted(String user) {
        gameStorage.getAll(user).clear();
    }

}
