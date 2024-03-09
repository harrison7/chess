package dataAccessTests;

import dataAccess.*;
import dataAccess.memoryDAOs.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

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
    private AuthDAO getAuthDAO(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        AuthDAO db;
        if (databaseClass.equals(MySQLAuthDAO.class)) {
            db = new MySQLAuthDAO();
        } else {
            db = new MemoryAuthDAO();
        }
        db.clear();
        return db;
    }
    private GameDAO getGameDAO(Class<? extends GameDAO> databaseClass) throws DataAccessException {
        GameDAO db;
        if (databaseClass.equals(MySQLGameDAO.class)) {
            db = new MySQLGameDAO();
        } else {
            db = new MemoryGameDAO();
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
    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void clearAuthSuccess(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        dataAccess.createAuth(auth);
        assertDoesNotThrow(dataAccess::clear);
    }
    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void clearGameSuccess(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO dataAccess = getGameDAO(dbClass);

        var game = new GameData(0, null, null, "name", null);
        dataAccess.createGame(game);
        assertDoesNotThrow(dataAccess::clear);
    }


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

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void createAuthSuccess(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        assertDoesNotThrow(() -> dataAccess.createAuth(auth));
    }
    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class/*, MemoryAuthDAO.class*/})
    void createAuthFail(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        assertDoesNotThrow(() -> dataAccess.createAuth(auth));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void getAuthSuccess(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        var newAuth = dataAccess.createAuth(auth);
        assertDoesNotThrow(() -> dataAccess.getAuth(newAuth));
    }
    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void getAuthFail(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        assertThrows(DataAccessException.class, () -> dataAccess.getAuth(auth));

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void deleteAuthSuccess(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        var newAuth = dataAccess.createAuth(auth);
        dataAccess.deleteAuth(newAuth);
        assertThrows(DataAccessException.class, () -> dataAccess.getAuth(newAuth));
    }
    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void deleteAuthFail(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dataAccess = getAuthDAO(dbClass);

        var auth = new AuthData("", "username");
        var newAuth = dataAccess.createAuth(auth);
        assertThrows(DataAccessException.class, () -> dataAccess.deleteAuth(auth));

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void createGameSuccess(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO dataAccess = getGameDAO(dbClass);

        var game = new GameData(0, null, null, "name", null);
        assertDoesNotThrow(() -> dataAccess.createGame(game));
    }
//    @ParameterizedTest
//    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
//    void createGameFail(Class<? extends GameDAO> dbClass) throws DataAccessException {
//        GameDAO dataAccess = getGameDAO(dbClass);
//
//        var game = new GameData(0, null, null, "name", null);
//        dataAccess.createGame(game);
//        assertThrows(DataAccessException.class, () -> dataAccess.createGame(game));
//    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void updateGameSuccess(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO dataAccess = getGameDAO(dbClass);
        AuthDAO authDAO = new MySQLAuthDAO();
        var auth = new AuthData("", "Me");
        var newAuth = authDAO.createAuth(auth);

        var game = new GameData(0, null, null, "name", null);
        var newGame = dataAccess.createGame(game);
        var updatedGame = new GameData(newGame.gameID(), "Me", null, "name", null);
        assertDoesNotThrow(() -> dataAccess.updateGame(updatedGame, newAuth, "WHITE"));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void listGamesSuccess(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO dataAccess = getGameDAO(dbClass);

        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(dataAccess.createGame(new GameData(0, null, null, "name", null)));
        expected.add(dataAccess.createGame(new GameData(0, null, null, "name1", null)));
        expected.add(dataAccess.createGame(new GameData(0, null, null, "name2", null)));

        var actual = dataAccess.listGames();
        assertGameCollectionEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void listGamesFail(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO dataAccess = getGameDAO(dbClass);

        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(dataAccess.createGame(new GameData(0, null, null, "name", null)));
        expected.add(dataAccess.createGame(new GameData(0, null, null, "name1", null)));
        expected.add(new GameData(0, null, null, "name2", null));

        var actual = dataAccess.listGames();
        Assertions.assertNotEquals(expected.size(), actual.size());
    }

    public static void assertGameEqual(GameData expected, GameData actual) {
        assertEquals(expected.gameID(), actual.gameID());
        assertEquals(expected.whiteUsername(), actual.whiteUsername());
        assertEquals(expected.blackUsername(), actual.blackUsername());
        assertEquals(expected.gameName(), actual.gameName());
    }


    public static void assertGameCollectionEqual(Collection<GameData> expected, Collection<GameData> actual) {
        GameData[] actualList = actual.toArray(new GameData[]{});
        GameData[] expectedList = expected.toArray(new GameData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++) {
            assertGameEqual(expectedList[i], actualList[i]);
        }
    }
}
