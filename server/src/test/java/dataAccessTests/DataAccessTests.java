package dataAccessTests;

import dataAccess.*;
import dataAccess.memoryDAOs.MemoryUserDAO;
import model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    void createUser(Class<? extends UserDAO> dbClass) throws DataAccessException {
        UserDAO dataAccess = getUserDAO(dbClass);

        var user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> dataAccess.createUser(user));
    }
}
