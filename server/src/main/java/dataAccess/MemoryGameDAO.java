package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private static MemoryGameDAO instance;
    private HashMap<Integer, GameData> gameList;

    public MemoryGameDAO() {
        gameList = new HashMap<>();
    }

    public static synchronized MemoryGameDAO getInstance() {
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
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
            var trueGame = gameList.get(game.gameID());
            return gameList.get(trueGame.gameID());
        }
//        GameData returnGame = null;
//        for (var gameName : gameList.values()) {
//            if (Objects.equals(gameName.gameName(), game.gameName())) {
//                returnGame = gameName;
//            }
//        }
//        if (returnGame != null) {
//            return returnGame;
//        }
        else {
            throw new DataAccessException("Game does not exist");
        }
    };
    public Map<Integer, GameData> listGames() {
        return gameList;
    };
    public GameData updateGame(GameData game, AuthData auth, String clientColor)
            throws DataAccessException {
        GameData updatedGame;
        var trueGame = gameList.get(game.gameID());
        if (gameList.containsKey(trueGame.gameID())) {
            if (clientColor == null) {
                return getGame(game);
            } else if ((clientColor.equals("WHITE") && trueGame.whiteUsername() != null) ||
                    (clientColor.equals("BLACK") && trueGame.blackUsername() != null)){
                throw new DataAccessException("already taken");
            } else if (clientColor.equals("WHITE")) {
                updatedGame = new GameData(trueGame.gameID(), auth.username(),
                        trueGame.blackUsername(), trueGame.gameName(), trueGame.game());
            } else if (clientColor.equals("BLACK")) {
                updatedGame = new GameData(trueGame.gameID(), trueGame.whiteUsername(),
                        auth.username(), trueGame.gameName(), trueGame.game());
            } else {
                throw new DataAccessException("wrong color");
            }
            gameList.put(trueGame.gameID(), updatedGame);
            return updatedGame;
        } else {
            throw new DataAccessException("bad request");
        }
    };
}
