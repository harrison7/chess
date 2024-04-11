package service;

import dataAccess.*;
import model.*;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("bad request");
        }
        if (userDAO.getUser(user) == null) {
            userDAO.createUser(user);
            AuthData newAuth = new AuthData("", user.username());
            return authDAO.createAuth(newAuth);

        } else {
            throw new DataAccessException("already taken");
        }
    }
    public AuthData login(UserData user) throws DataAccessException {
        if (userDAO.getUser(user) != null) {
            AuthData newAuth = new AuthData("", user.username());
            return authDAO.createAuth(newAuth);
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
    public void logout(AuthData auth) throws DataAccessException {
        authDAO.deleteAuth(auth);
    }
}