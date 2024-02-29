package server.results;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(String message, Collection<GameData> games) {
}
