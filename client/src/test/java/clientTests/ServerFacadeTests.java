package clientTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import serverFacade.ServerFacade;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerSuccess() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        AuthData newToken = facade.register(userData);

        Assertions.assertNotNull(newToken.authToken());
        Assertions.assertEquals("u", newToken.username());
    }

    @Test
    void registerFail() throws DataAccessException {
        UserData userData = new UserData("u", "p", "e");
        UserData dupeData = new UserData("u", "l", "m");
        UserData invalidUser = new UserData("u", null, "m");
        AuthData newToken = facade.register(userData);

//        Assertions.assertThrows(DataAccessException.class, () -> {
//            facade.register(dupeData);
//        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.register(invalidUser);
        });
    }

}
