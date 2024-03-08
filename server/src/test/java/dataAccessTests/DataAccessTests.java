package dataAccessTests;

import dataAccess.*;
import dataAccess.memoryDAOs.MemoryUserDAO;
import model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataAccessTests {
    private UserDAO getUserDAO(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO db;
        if (databaseClass.equals(MySQLUserDAO.class)) {
            db = new MySQLUserDAO();
        } else {
            db = new MemoryUserDAO();
        }
        db.clear();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void clearUserSuccess(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO dataAccess = getUserDAO(dbClass);

        var user = new UserData("username", "password", "email");
        dataAccess.createUser(user);
        assertDoesNotThrow(dataAccess::clear);
    }
    //authClear
    //gameClear


    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void createUserSuccess(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO dataAccess = getUserDAO(dbClass);

        var user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> dataAccess.createUser(user));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void createUserFail(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO dataAccess = getUserDAO(dbClass);

        var user = new UserData("username", "password", "email");
        dataAccess.createUser(user);
        assertThrows(DataAccessException.class, () -> dataAccess.createUser(user));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void getUserSuccess(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO dataAccess = getUserDAO(dbClass);

        var user = new UserData("username", "password", "email");
        dataAccess.createUser(user);
        assertDoesNotThrow(() -> dataAccess.getUser(user));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void getUserFail(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO dataAccess = getUserDAO(dbClass);

        var user = new UserData("username", "password", "email");
        var wrongUser = new UserData("username", "wrongPass", "wrongEmail");

        dataAccess.createUser(user);
        assertThrows(DataAccessException.class, () -> dataAccess.getUser(wrongUser));
    }


}
