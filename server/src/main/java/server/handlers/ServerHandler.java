package server.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import server.requests.ClearRequest;
import server.requests.RegisterRequest;
import server.results.ClearResult;
import server.results.RegisterResult;
import service.ClearService;
import service.UserService;
import spark.*;

public class ServerHandler {

    private static ServerHandler instance;
    protected MemoryUserDAO userDAO = MemoryUserDAO.getInstance();
    protected MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    protected MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    public ServerHandler() {}

    public static synchronized ServerHandler getInstance() {
        if (instance == null) {
            instance = new ServerHandler();
        }
        return instance;
    }

    public String clear(Request req, Response res) {
        var gson = new Gson();
        ClearRequest request = (ClearRequest)gson.fromJson(String.valueOf(req), ClearRequest.class);

        ClearService service = new ClearService(userDAO, authDAO, gameDAO);
        ClearResult result = service.clear();
        res.status(200);

        return gson.toJson(result);
    }

    public String register(Request req, Response res) throws DataAccessException {
        var gson = new Gson();
        RegisterRequest request = (RegisterRequest)gson.fromJson("", RegisterRequest.class);

        UserService service = new UserService(userDAO, authDAO);
        RegisterResult result = service.register(request);
        res.status(200);

        return gson.toJson(result);
    }
}
