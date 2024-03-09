package dataAccess.memoryDAOs;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.HelperGameDAO;
import model.AuthData;
import model.GameData;

import java.util.*;

public class MemoryGameDAO extends HelperGameDAO {
    //Memory class
    private static MemoryGameDAO instance;
    private ArrayList<GameData> gameList;

    public MemoryGameDAO() {
        gameList = new ArrayList<>();
    }

    public static synchronized MemoryGameDAO getInstance() {
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
    }

    @Override
    public void clear() {
        gameList = new ArrayList<>();
    };
    public GameData createGame(GameData game) throws DataAccessException {
        for (GameData checkGame : gameList) {
            if (Objects.equals(checkGame.gameName(), game.gameName())) {
                throw new DataAccessException("Game name is already in use");
            }
        }

        int gameID = gameList.size() + 1;
        GameData newGame = new GameData(gameID, game.whiteUsername(),
                game.blackUsername(), game.gameName(), game.game());
        gameList.add(newGame);
        return newGame;
    };
    @Override
    public GameData getGame(GameData game) throws DataAccessException {
        if (gameList.contains(game)) {
            var trueGame = gameList.get(game.gameID() - 1);
            return gameList.get(trueGame.gameID() - 1);
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
    @Override
    public Collection<GameData> listGames() {
        return gameList;
    };
    @Override
    public GameData updateGame(GameData game, AuthData auth, String clientColor)
            throws DataAccessException {
        GameData updatedGame;
        var trueGame = gameList.get(game.gameID() - 1);
        if (trueGame.gameID() > 0) {
            if (clientColor == null) {
                return getGame(game);
            }
            updatedGame = updateGameHelper(trueGame, auth, clientColor);
            gameList.remove(trueGame.gameID() - 1);
            gameList.add(updatedGame.gameID() - 1, updatedGame);
            return updatedGame;
        } else {
            throw new DataAccessException("bad request");
        }
    };
}
