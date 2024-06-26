package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGames(AuthData auth) throws DataAccessException {
        if (authDAO.getAuth(auth).authToken().equals(auth.authToken())) {
            return gameDAO.listGames();
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    public GameData createGame(AuthData auth, GameData game) throws DataAccessException {
        if (authDAO.getAuth(auth).authToken().equals(auth.authToken())) {
            return gameDAO.createGame(game);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }

    public GameData joinGame(AuthData auth, GameData game, String clientColor)
            throws DataAccessException {
        if (game.gameID() == 0) {
            throw new DataAccessException("bad request");
        } else if ((gameDAO.getGame(game).whiteUsername() != null && Objects.equals(clientColor, "WHITE")) ||
                (gameDAO.getGame(game).blackUsername() != null && Objects.equals(clientColor, "BLACK"))) {
            throw new DataAccessException("already taken");
        } else if (authDAO.getAuth(auth).authToken().equals(auth.authToken()) &&
                gameDAO.getGame(game).gameID() == game.gameID()) {
            return gameDAO.updateGame(game, auth, clientColor);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
