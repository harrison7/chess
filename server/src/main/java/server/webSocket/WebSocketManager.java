package server.webSocket;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MySQLUserDAO;
import org.eclipse.jetty.websocket.api.*;
import server.handlers.ServerHandler;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
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
            case JOIN_PLAYER -> join(session, message);
        }
    }

    public void join(Session session, String message) throws IOException {
        JoinPlayerCommand action = new Gson().fromJson(message, JoinPlayerCommand.class);
        var user = action.getAuthString();
        connections.add(user, session);
        var res = new LoadGameMessage(new ChessGame());
        connections.reply(user, res);

        var reply = String.format("%s is in the shop", action.getCommandType());
        var notification = new NotificationMessage(reply);
        connections.broadcast(user, notification);
    }

//    public void sendNotification(String username, ChessGame.TeamColor playerColor) throws IOException {
//        try {
//            //var message = String.format("%s joins team %s", username, playerColor);
//            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new IOException(ex.getMessage());
//        }
//    }
}
