package dataAccess;

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
    public void createGame(GameData game) throws DataAccessException {
        for (GameData checkGame : gameList.values()) {
            if (Objects.equals(checkGame.gameName(), game.gameName())) {
                throw new DataAccessException("Game name is already in use");
            }
        }
        // Create game ID here
        gameList.put(game.gameID(), game);
    };
    public GameData getGame(GameData game) throws DataAccessException {
        if (gameList.containsKey(game.gameID())) {
            return gameList.get(game.gameID());
        } else {
            throw new DataAccessException("Game does not exist");
        }
    };
    public Map<Integer, GameData> listGames() throws DataAccessException {
        return gameList;
    };
    public void updateGame(GameData game) throws DataAccessException {
        if (gameList.containsKey(game.gameID())) {
            gameList.put(game.gameID(), game);
        } else {
            throw new DataAccessException("Game can't be updated as it doesn't exist");
        }
    };
}
