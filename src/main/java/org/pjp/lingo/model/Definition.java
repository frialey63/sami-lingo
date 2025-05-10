package org.pjp.lingo.model;

public record Definition(String french, String english) {

    public String getWord(boolean french) {
        return french ? french() : english();
    }
}
