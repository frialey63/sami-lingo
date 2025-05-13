package org.pjp.lingo.service;

import java.util.Set;

import org.pjp.lingo.model.Game;

public interface GameService {

    void saveCompleted(String user, Game game);

    Set<Game> getCompleted(String user);

    void clearCompleted(String user);
}
