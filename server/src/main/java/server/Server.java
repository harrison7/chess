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
        /*Spark.post("/pet", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/pet", this::deleteAllPets);
        Spark.exception(ResponseException.class, this::exceptionHandler);*/
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

    private Object clear(Request req, Response res) throws DataAccessException {
        clearService.clear();
        res.status(204);
        return "bruh";
    }

    /*private Object login(Request req, Response res) throws DataAccessException {
        LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);

        UserService service = new UserService();
        LoginResult result = service.login(request);

        return gson.toJson(result);

    }*/
}
