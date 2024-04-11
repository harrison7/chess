package server.webSocket;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;

public class WebSocketManager {
    private static WebSocketManager instance;
    private ConnectionManager connections;
    protected MySQLGameDAO gameDAO;
    protected MySQLUserDAO userDAO;
    protected MySQLAuthDAO authDAO;
    public WebSocketManager() throws DataAccessException {
        connections = ConnectionManager.getInstance();
        gameDAO = MySQLGameDAO.getInstance();
        userDAO = MySQLUserDAO.getInstance();
        authDAO = MySQLAuthDAO.getInstance();
    }

    public static synchronized WebSocketManager getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void evaluateMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
        }
    }

    public void joinPlayer(Session session, String message) throws IOException, DataAccessException {
        JoinPlayerCommand action = new Gson().fromJson(message, JoinPlayerCommand.class);
        var user = action.getAuthString();
        connections.add(user, session);

        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));

        try {
            var joiningUser = authDAO.getAuth(new AuthData(user, ""));
            if (game == null) {
                var res = new ErrorMessage("Error: bad game ID");
                connections.reply(user, res);
                connections.remove(user);
            } else if ((!Objects.equals(game.whiteUsername(), joiningUser.username()) && action.getPlayerColor() == WHITE) ||
                    (!Objects.equals(game.blackUsername(), joiningUser.username()) && action.getPlayerColor() == BLACK)) {
                var res = new ErrorMessage("Error: wrong team or empty team");
                connections.reply(user, res);
                connections.remove(user);
            } else {
                var res = new LoadGameMessage(game.game());
                connections.reply(user, res);

                var reply = String.format("%s joined the game", action.getUsername());
                var notification = new NotificationMessage(reply);
                connections.broadcast(user, notification);
            }
        } catch (DataAccessException e) {
            var res = new ErrorMessage("Error: bad authToken");
            connections.reply(user, res);
            connections.remove(user);
        }
    }

    public void joinObserver(Session session, String message) throws IOException, DataAccessException {
        JoinObserverCommand action = new Gson().fromJson(message, JoinObserverCommand.class);
        var user = action.getAuthString();
        connections.add(user, session);

        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));

        try {
            authDAO.getAuth(new AuthData(user, ""));
            if (game == null) {
                var res = new ErrorMessage("Error: bad game ID");
                connections.reply(user, res);
                connections.remove(user);
            } else {
                var res = new LoadGameMessage(game.game());
                connections.reply(user, res);

                var reply = String.format("%s joined as an observer", action.getUsername());
                var notification = new NotificationMessage(reply);
                connections.broadcast(user, notification);
            }
        } catch (DataAccessException e) {
            var res = new ErrorMessage("Error: bad authToken");
            connections.reply(user, res);
            connections.remove(user);
        }
    }

    public void makeMove(Session session, String message) throws IOException {
        MakeMoveCommand action = new Gson().fromJson(message, MakeMoveCommand.class);
        var user = action.getAuthString();
        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));
        //Make the move
        var res = new LoadGameMessage(new ChessGame());
        connections.broadcast("", res);

        var reply = String.format("%s made this move:", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast(user, notification);
    }

    public void leave(Session session, String message) throws IOException {
        LeaveCommand action = new Gson().fromJson(message, LeaveCommand.class);
        var user = action.getAuthString();
        connections.remove(user);
        var res = new LoadGameMessage(new ChessGame());

        var reply = String.format("%s left the game", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast(user, notification);
    }

    public void resign(Session session, String message) throws IOException {
        ResignCommand action = new Gson().fromJson(message, ResignCommand.class);
        var user = action.getAuthString();
        var res = new LoadGameMessage(new ChessGame());
        //connections.broadcast("", res);

        var reply = String.format("%s left the game", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast("", notification);
    }
}
