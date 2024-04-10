package server.webSocket;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

public class WebSocketManager {
    private static WebSocketManager instance;
    private ConnectionManager connections;
    public WebSocketManager() throws DataAccessException {
        connections = ConnectionManager.getInstance();
    }

    public static synchronized WebSocketManager getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void evaluateMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
        }
    }

    public void joinPlayer(Session session, String message) throws IOException {
        JoinPlayerCommand action = new Gson().fromJson(message, JoinPlayerCommand.class);
        var user = action.getAuthString();
        connections.add(user, session);
        var res = new LoadGameMessage(new ChessGame());
        connections.reply(user, res);

        var reply = String.format("%s joined the game", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast(user, notification);
    }

    public void joinObserver(Session session, String message) throws IOException {
        JoinObserverCommand action = new Gson().fromJson(message, JoinObserverCommand.class);
        var user = action.getAuthString();
        connections.add(user, session);
        var res = new LoadGameMessage(new ChessGame());
        connections.reply(user, res);

        var reply = String.format("%s joined as an observer", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast(user, notification);
    }

    public void makeMove(Session session, String message) throws IOException {
        MakeMoveCommand action = new Gson().fromJson(message, MakeMoveCommand.class);
        var user = action.getAuthString();
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
        //connections.broadcast("", res);

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
