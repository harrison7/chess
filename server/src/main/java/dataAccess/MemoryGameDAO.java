package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> gameList;

    public MemoryGameDAO() {
        gameList = new HashMap<>();
    }

    public void clear() {
        gameList = new HashMap<>();
    };
    public GameData createGame(GameData game) throws DataAccessException {
        for (GameData checkGame : gameList.values()) {
            if (Objects.equals(checkGame.gameName(), game.gameName())) {
                throw new DataAccessException("Game name is already in use");
            }
        }

        int gameID = gameList.size() + 1;
        GameData newGame = new GameData(gameID, game.whiteUsername(),
                game.blackUsername(), game.gameName(), game.game());
        gameList.put(newGame.gameID(), newGame);
        return newGame;
    };
    public GameData getGame(GameData game) throws DataAccessException {
        if (gameList.containsKey(game.gameID())) {
            return gameList.get(game.gameID());
        } else {
            throw new DataAccessException("Game does not exist");
        }
    };
    public Map<Integer, GameData> listGames() {
        return gameList;
    };
    public GameData updateGame(GameData game, AuthData auth, ChessGame.TeamColor clientColor)
            throws DataAccessException {
        GameData updatedGame;
        if (gameList.containsKey(game.gameID())) {
            if (clientColor == ChessGame.TeamColor.WHITE) {
                updatedGame = new GameData(game.gameID(), auth.username(),
                        game.blackUsername(), game.gameName(), game.game());
            } else {
                updatedGame = new GameData(game.gameID(), game.whiteUsername(),
                        auth.username(), game.gameName(), game.game());
            }
            gameList.put(game.gameID(), updatedGame);
            return updatedGame;
        } else {
            throw new DataAccessException("Game can't be updated as it doesn't exist");
        }
    };
}
