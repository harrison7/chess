package server.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.memoryDAOs.MemoryAuthDAO;
import dataAccess.memoryDAOs.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.requests.*;
import server.results.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Objects;

public class ServerHandler {

    private static ServerHandler instance;
    protected MySQLUserDAO userDAO;
    protected MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    protected MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    public ServerHandler() throws DataAccessException {
        userDAO = MySQLUserDAO.getInstance();
    }

    public static synchronized ServerHandler getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new ServerHandler();
        }
        return instance;
    }

    public String clear(Request req, Response res) throws DataAccessException {
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

        var request = new ListGamesRequest(req.headers("Authorization"));
        var auth = new AuthData(request.authorization(), "");

        GameService service = new GameService(gameDAO, authDAO);

        try {
            var list = service.listGames(auth);
            var result = new ListGamesResult(null, list.values());
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")) {
                res.status(401);
            }
            return gson.toJson(new ListGamesResult("Error: " + e.getMessage(), null));
        }
    }

    public String createGame(Request req, Response res) {
        var gson = new Gson();
        var tempRequest = gson.fromJson(req.body(), CreateGameRequest.class);
        var request = new CreateGameRequest(req.headers("Authorization"), tempRequest.gameName());
        var auth = new AuthData(request.authorization(), "");
        var game = new GameData(0, null, null, request.gameName(), null);

        GameService service = new GameService(gameDAO, authDAO);

        try {
            var newGame = service.createGame(auth, game);
            var result = new CreateGameResult(null, String.valueOf(newGame.gameID()));
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")) {
                res.status(401);
            }
            return gson.toJson(new CreateGameResult("Error: " + e.getMessage(), null));
        }
    }

    public String joinGame(Request req, Response res) {
        var gson = new Gson();
        var tempRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        var request = new JoinGameRequest(req.headers("Authorization"),
                tempRequest.playerColor(), tempRequest.gameID());
        var tempAuth = new AuthData(request.authorization(), "");

        GameService service = new GameService(gameDAO, authDAO);

        try {
            var username = authDAO.getAuth(tempAuth).username();
            var auth = new AuthData(request.authorization(), username);
            var game = new GameData(request.gameID(), null, null, null, null);
            var newGame = service.joinGame(auth, game, request.playerColor());
            var result = new JoinGameResult(null);
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "bad request")) {
                res.status(400);
            } else if (Objects.equals(e.getMessage(), "unauthorized")) {
                res.status(401);
            } else if (Objects.equals(e.getMessage(), "already taken")) {
                res.status(403);
            }
            return gson.toJson(new JoinGameResult("Error: " + e.getMessage()));
        }
    }
}
