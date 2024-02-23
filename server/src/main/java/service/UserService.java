package service;

import dataAccess.*;
import model.*;

import java.util.HashMap;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public AuthData register(UserData user) throws DataAccessException {
        if (userDAO.getUser(user) == null) {
            userDAO.createUser(user);
            AuthData newAuth = new AuthData("", user.username());
            return authDAO.createAuth(newAuth);
        } else {
            throw new DataAccessException("User already exists");
        }
    }
    public AuthData login(UserData user) {

    }
    public void logout(UserData user) {

    }
}