package server.handlers;

import com.google.gson.Gson;
import spark.*;

public class ServerHandler {

    private static ServerHandler instance;

    public ServerHandler() {}

    public static synchronized ServerHandler getInstance() {
        if (instance == null) {
            instance = new ServerHandler();
        }
        return instance;
    }

    public String handleRequest(Request req, Response res) {
        var gson = new Gson();
        return gson.toJson("bruh");
    }
}
