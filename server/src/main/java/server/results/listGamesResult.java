package server.results;

import model.GameData;

import java.util.ArrayList;

public record listGamesResult(String message, ArrayList<GameData> games) {
}
