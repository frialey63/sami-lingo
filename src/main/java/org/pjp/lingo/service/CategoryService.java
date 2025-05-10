package org.pjp.lingo.service;

import java.util.List;

import org.pjp.lingo.model.Category;
import org.pjp.lingo.model.Challenge;
import org.pjp.lingo.model.Progress;

public interface CategoryService {

    List<Category> list();

    int loadCategory(String name);

    Category getCategory(String name);

    Challenge generateChallenge(String name, boolean french, int numOptions, Progress progress);

    int calculateProgress(String name, Progress progress);
}
