package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class HelperGameDAO implements GameDAO {

    public HelperGameDAO() {
    }

    public void clear() throws DataAccessException {

    }
    public GameData createGame(GameData game) throws DataAccessException {
        return game;
    }
    public GameData getGame(GameData game) throws DataAccessException {
        return game;
    }
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<>();
    }
    public GameData updateGame(GameData game, AuthData auth, String clientColor)
            throws DataAccessException {
        return game;
    }

    protected GameData updateGameHelper(GameData game, AuthData auth,
                                         String clientColor)
            throws DataAccessException {
        GameData updatedGame;
        if ((clientColor.equals("WHITE") && game.whiteUsername() != null) ||
                (clientColor.equals("BLACK") && game.blackUsername() != null)){
            throw new DataAccessException("already taken");
        } else if (clientColor.equals("WHITE")) {
            updatedGame = new GameData(game.gameID(), auth.username(),
                    game.blackUsername(), game.gameName(), game.game());
        } else if (clientColor.equals("BLACK")) {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(),
                    auth.username(), game.gameName(), game.game());
        } else {
            throw new DataAccessException("wrong color");
        }
        return updatedGame;
    }
}
