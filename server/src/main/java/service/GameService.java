package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Map;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Map<Integer, GameData> listGames(AuthData auth) throws DataAccessException {
        if (authDAO.getAuth(auth).equals(auth)) {
            return gameDAO.listGames();
        } else {
            throw new DataAccessException("Authentication is incorrect");
        }
    }

    public GameData createGame(AuthData auth, GameData game) throws DataAccessException {
        if (authDAO.getAuth(auth).equals(auth)) {
            return gameDAO.createGame(game);
        } else {
            throw new DataAccessException("Authentication is incorrect");
        }
    }

    public GameData joinGame(AuthData auth, GameData game, ChessGame.TeamColor clientColor)
            throws DataAccessException {
        if (authDAO.getAuth(auth).equals(auth) && gameDAO.getGame(game).equals(game)) {
            return gameDAO.updateGame(game, auth, clientColor);
        } else {
            throw new DataAccessException("Authentication is incorrect");
        }
    }
}
