package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.memoryDAOs.MemoryAuthDAO;
import dataAccess.memoryDAOs.MemoryGameDAO;
import dataAccess.memoryDAOs.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.*;
import service.*;

public class ServiceTests {
    private UserDAO userDAO = new MemoryUserDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
    private UserService userService = new UserService(userDAO, authDAO);

    private GameService gameService = new GameService(gameDAO, authDAO);

    @BeforeEach
    @Test
    void clearSuccess() throws DataAccessException {
        clearService.clear();
    }

    @Test
    void registerSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData newToken = userService.register(userData);
        AuthData storedToken = authDAO.getAuth(newToken);

        Assertions.assertNotNull(newToken.authToken());
        Assertions.assertEquals(newToken, storedToken);
        Assertions.assertEquals("u", newToken.username());
    }

    @Test
    void registerFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        UserData dupeData = new UserData("u", "l", "m");
        AuthData newToken = userService.register(userData);

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(dupeData);
        });
    }

    @Test
    void loginSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData newToken = userService.register(userData);
        userService.logout(newToken);
        UserData loginData = new UserData("u", "p", "");
        AuthData authData = userService.login(loginData);
        AuthData storedToken = authDAO.getAuth(authData);

        Assertions.assertNotNull(authData.authToken());
        Assertions.assertEquals(authData, storedToken);
        Assertions.assertEquals("u", authData.username());
    }

    @Test
    void loginFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData newToken = userService.register(userData);
        userService.logout(newToken);
        UserData wrongPassword = new UserData("u", "l", "");
        UserData wrongUser = new UserData("k", "p", "");

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(wrongPassword);
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(wrongUser);
        });
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        userService.logout(authData);

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.getAuth(authData);
        });
    }

    @Test
    void logoutFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);

        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(new AuthData("", "u"));
        });
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        GameData newGame = new GameData(0, "", "",
                "name", new ChessGame());
        gameService.createGame(authData, newGame);

        Assertions.assertFalse(gameService.listGames(authData).isEmpty());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        AuthData wrongData = new AuthData("", "u");
        GameData newGame = new GameData(0, "", "",
                "name", new ChessGame());
        gameService.createGame(authData, newGame);

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames(wrongData);
        });
    }
    @Test
    void createGameSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        GameData newGame = new GameData(0, "", "",
                "name", null);
        GameData gameData = gameService.createGame(authData, newGame);

        Assertions.assertTrue(gameData.gameID() > 0);
    }

    @Test
    void createGameFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        AuthData wrongData = new AuthData("", "u");
        GameData newGame = new GameData(0, "", "",
                "name", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(wrongData, newGame);
        });
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        GameData newGame = new GameData(0, null, null,
                "name", new ChessGame());
        GameData gameData = gameService.createGame(authData, newGame);
        GameData joinedGame = gameService.joinGame(authData, gameData, "WHITE");

        Assertions.assertTrue(joinedGame.gameID() > 0);
    }

    @Test
    void joinGameFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData authData = userService.register(userData);
        GameData newGame = new GameData(0, "", "",
                "name", new ChessGame());
        GameData gameData = gameService.createGame(authData, newGame);
        AuthData wrongData = new AuthData("", "u");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(wrongData, gameData, "WHITE");
        });
    }
}
