package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    public void clear() {

    };
    public void createGame(GameData game) throws DataAccessException {

    };
    public GameData getGame(GameData game) throws DataAccessException {
        return game;
    };
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<GameData>();
    };
    public void updateGame(GameData game) throws DataAccessException {

    };
}
