package server.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.requests.*;
import server.results.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.ArrayList;
import java.util.Objects;

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
        ClearRequest request = gson.fromJson(req.body(), ClearRequest.class);

        ClearService service = new ClearService(userDAO, authDAO, gameDAO);
        ClearResult result = service.clear();
        res.status(200);

        return gson.toJson(result);
    }

    public String register(Request req, Response res) {
        var gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        var user = new UserData(request.username(), request.password(), request.email());

        UserService service = new UserService(userDAO, authDAO);

        try {
            var auth = service.register(user);
            var result = new RegisterResult(null, auth.username(), auth.authToken());
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "bad request")) {
                res.status(400);
            } else if (Objects.equals(e.getMessage(), "already taken")) {
                res.status(403);
            }

            return gson.toJson(new RegisterResult("Error: " + e.getMessage(), null, null));
        }
    }

    public String login(Request req, Response res) {
        var gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        var user = new UserData(request.username(), request.password(), "");

        UserService service = new UserService(userDAO, authDAO);

        try {
            var auth = service.login(user);
            var result = new LoginResult(null, auth.username(), auth.authToken());
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")) {
                res.status(401);
            }
            return gson.toJson(new LoginResult("Error: " + e.getMessage(), null, null));
        }
    }

    public String logout(Request req, Response res) {
        var gson = new Gson();

        var request = new LogoutRequest(req.headers("Authorization"));
        //LogoutRequest request = gson.fromJson(req.headers("Authorization"), LogoutRequest.class);
        var auth = new AuthData(request.authorization(), "");

        UserService service = new UserService(userDAO, authDAO);

        try {
            service.logout(auth);
            var result = new LogoutResult(null);
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")) {
                res.status(401);
            }
            return gson.toJson(new LogoutResult("Error: " + e.getMessage()));
        }
    }

    public String listGames(Request req, Response res) {
        var gson = new Gson();

        var request = new listGamesRequest(req.headers("Authorization"));
        var auth = new AuthData(request.authorization(), "");

        GameService service = new GameService(gameDAO, authDAO);

        try {
            var list = service.listGames(auth);
            var result = new listGamesResult(null, list.values());
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")) {
                res.status(401);
            }
            return gson.toJson(new LogoutResult("Error: " + e.getMessage()));
        }
    }
}
