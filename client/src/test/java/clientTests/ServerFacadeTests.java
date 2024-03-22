package clientTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import serverFacade.ServerFacade;
import server.Server;
import ui.State;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws URISyntaxException, IOException {
        server = new Server();
        var port = server.run(0);

        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port, State.PRELOGIN);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerSuccess() throws URISyntaxException, IOException {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFail() throws DataAccessException, IOException, URISyntaxException {
        UserData userData = new UserData("u", "p", "e");
        UserData invalidUser = new UserData("u", null, "m");
        facade.register(userData.username(), userData.password(), userData.email());

        Assertions.assertThrows(IOException.class, () -> {
            facade.register(invalidUser.username(), invalidUser.password(), invalidUser.email());
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.register(userData.username(), userData.password(), userData.email());
        });
    }

    @Test
    void loginSuccess() throws URISyntaxException, IOException {
        var regData = facade.register("playerlog", "password", "p1@email.com");
        facade.logout(regData.authToken());
        var authData = facade.login("playerlog", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginFail() throws URISyntaxException, IOException {
        var regData = facade.register("playerlf", "password", "p1@email.com");
        facade.logout(regData.authToken());

        Assertions.assertThrows(IOException.class, () -> {
            var authData = facade.login("playerfl", "password");
        });
        Assertions.assertThrows(IOException.class, () -> {
            var authData = facade.login("playerlf", "asdf");
        });
    }

    @Test
    void logoutSuccess() throws URISyntaxException, IOException {
        var authData = facade.register("playerout", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test
    void logoutFail() throws URISyntaxException, IOException {
        var authData = facade.register("playerof", "password", "p1@email.com");
        Assertions.assertThrows(IOException.class, () -> facade.logout(""));
    }

    @Test
    void listGamesSuccess() throws DataAccessException, IOException, URISyntaxException {
        var authData = facade.register("ulist", "p", "e");
        facade.createGame(authData.authToken(), "name");

        Assertions.assertFalse(
                facade.listGames(authData.authToken()).games().isEmpty());
    }

    @Test
    void listGamesFail() throws DataAccessException, IOException, URISyntaxException {
        var authData = facade.register("ulistfail", "p", "e");
        facade.createGame(authData.authToken(), "name2");

        Assertions.assertThrows(IOException.class, () -> {
            facade.listGames("");
        });
    }

    @Test
    void createGameSuccess() throws DataAccessException, IOException, URISyntaxException {
        var authData = facade.register("ucreate", "p", "e");
        var gameData = facade.createGame(authData.authToken(), "name3");

        Assertions.assertTrue(Integer.parseInt(gameData.gameID()) > 0);
    }

    @Test
    void createGameFail() throws DataAccessException, IOException, URISyntaxException {
        var authData = facade.register("ucreatefail", "p", "e");

        Assertions.assertThrows(IOException.class, () -> {
            facade.createGame("", "newGame");
        });
    }

    @Test
    void joinGameSuccess() throws DataAccessException, IOException, URISyntaxException {
        var authData = facade.register("ujoin", "p", "u");
        var gameData = facade.createGame(authData.authToken(), "name");

        assertDoesNotThrow(() ->
                facade.joinGame(authData.authToken(), "name",
                        Integer.parseInt(gameData.gameID()))
        );


    }

    @Test
    void joinGameFail() throws DataAccessException, IOException, URISyntaxException {
        var authData = facade.register("ujoin", "p", "u");
        var gameData = facade.createGame(authData.authToken(), "name");


        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame("", "name", Integer.parseInt(gameData.gameID()));
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame(authData.authToken(), "name", 0);
        });
    }
}
