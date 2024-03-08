package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Map;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public GameData createGame(GameData game) throws DataAccessException;
    public GameData getGame(GameData game) throws DataAccessException;
    public Map<Integer, GameData> listGames() throws DataAccessException;
    public GameData updateGame(GameData game, AuthData auth, String clientColor)
            throws DataAccessException;
}
