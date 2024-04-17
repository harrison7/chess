package facade;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    private String authToken;
    private String username;

    public WebSocketFacade(String url, NotificationHandler notificationHandler, String authToken, String username) throws IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;
            this.authToken = authToken;
            this.username = username;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);

                    switch (notification.getServerMessageType()) {
                        case LOAD_GAME -> {
                            try {
                                loadGame(message);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case NOTIFICATION -> notification(message);
                        case ERROR -> errorMessage(message);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(int gameID, ChessGame.TeamColor teamColor) throws IOException {
        try {

            var action = new JoinPlayerCommand(authToken, gameID, username, teamColor);
            send(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void joinObserver(int gameID) throws IOException {
        try {
            var action = new JoinObserverCommand(authToken, gameID, username);
            send(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void makeMove(ChessMove move, int gameID) throws IOException {
        try {
            var action = new MakeMoveCommand(authToken, move, gameID, username);
            send(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void leave(int gameID) throws IOException {
        try {
            var action = new LeaveCommand(authToken, gameID, username);
            send(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void resign(int gameID) throws IOException {
        try {
            var action = new ResignCommand(authToken, gameID, username);
            send(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void loadGame(String message) throws IOException {
        LoadGameMessage game = new Gson().fromJson(message, LoadGameMessage.class);
        notificationHandler.notify(game);
    }

    public void notification(String message) {
        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
        notificationHandler.notify(notification);
    }

    public void errorMessage(String message) {
        ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
        notificationHandler.notify(error);
    }
}
