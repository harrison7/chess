package server;

import dataAccess.*;
import server.handlers.ServerHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private ClearService clearService;
    private UserService userService;
    private GameService gameService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    //private final WebSocketHandler webSocketHandler;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        clearService = new ClearService(userDAO, authDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        //webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Spark.webSocket("/connect", webSocketHandler);

        Spark.post("/user", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.register(req, res);
        });
        Spark.post("/session", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.login(req, res);
        });
        Spark.delete("/session", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.logout(req, res);
        });

        Spark.get("/game", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.listGames(req, res);
        });
        Spark.post("/game", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.createGame(req, res);
        });

        Spark.delete("/db", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.clear(req, res);
        });

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
