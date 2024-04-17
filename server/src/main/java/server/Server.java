package server;

import dataAccess.*;
import dataAccess.memoryDAOs.MemoryAuthDAO;
import dataAccess.memoryDAOs.MemoryGameDAO;
import dataAccess.memoryDAOs.MemoryUserDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import server.handlers.ServerHandler;
import server.webSocket.WebSocketManager;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Spark;

@WebSocket
public class Server {
    private ClearService clearService;
    private UserService userService;
    private GameService gameService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    //private static WebSocketManager webSocketManager;

    public Server() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        clearService = new ClearService(userDAO, authDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        //webSocketManager = new WebSocketManager();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", Server.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

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
        Spark.put("/game", (req, res) -> {
            ServerHandler handler = ServerHandler.getInstance();
            return handler.joinGame(req, res);
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

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        WebSocketManager manager = WebSocketManager.getInstance();
        System.out.printf("Received: %s\n", message);
        manager.evaluateMessage(session, message);

        //
        //if message is joinGame
        // websocketManager.joinGame(session, message);
        // find the corresponding game
        // check if the player is in that game
        // verify the auth
        //
        //session.getRemote().sendString("WebSocket response: " + message);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
