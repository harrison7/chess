package server;

import dataAccess.UserDAO;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import service.UserService;
import spark.*;

public class Server {
    //private final UserService service;
    //private final WebSocketHandler webSocketHandler;

    public Server(/*UserDAO userDAO*/) {
        //service = new UserService(userDAO);
        //webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Spark.webSocket("/connect", webSocketHandler);

        /*Spark.post("/pet", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/pet", this::deleteAllPets);
        Spark.exception(ResponseException.class, this::exceptionHandler);*/

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
