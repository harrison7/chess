package service;

import dataAccess.*;
import results.ClearResult;

public class ClearService {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResult clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        return new ClearResult(null);
    }
}
