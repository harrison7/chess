package facade;

import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    private static String authToken;

    public WebSocketFacade(String url, NotificationHandler notificationHandler, String authToken) throws IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;
            this.authToken = authToken;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
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

    public void join() throws IOException {
        try {
            var action = new JoinPlayerCommand(authToken);
            send(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }
}
