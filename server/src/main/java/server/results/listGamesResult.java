package server.results;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public record listGamesResult(String message, Collection<GameData> games) {
}
