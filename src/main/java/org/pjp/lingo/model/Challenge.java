package org.pjp.lingo.model;

import java.util.List;

public record Challenge(String target, List<String> options, int answer) {

}
