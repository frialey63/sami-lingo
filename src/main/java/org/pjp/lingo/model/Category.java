package org.pjp.lingo.model;

import java.util.List;

public record Category(String name, List<Definition> definitions) implements Comparable<Category> {

    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }

}
