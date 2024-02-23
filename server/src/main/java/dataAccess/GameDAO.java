package dataAccess;

import model.GameData;

import java.util.Map;

public interface GameDAO {
    public void clear();
    public void createGame(GameData game) throws DataAccessException;
    public GameData getGame(GameData game) throws DataAccessException;
    public Map<Integer, GameData> listGames() throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException;
}
