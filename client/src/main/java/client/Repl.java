package client;

import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import facade.ServerFacade;
import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static client.ui.State.PRELOGIN;

public class Repl implements NotificationHandler {
    private PreloginUI preloginUI;
    private PostloginUI postloginUI;
    private GameplayUI gameplayUI;

    //private Server server;

    private int port;
    private String serverURL;

    public Repl(int port, String serverUrl, ServerFacade sf) throws URISyntaxException, IOException {
        this.port = port;
        this.serverURL = serverUrl;

//        server = new Server();
//        port = server.run(port);

        preloginUI = new PreloginUI(port, sf);
        postloginUI = new PostloginUI(port, sf);
        gameplayUI = new GameplayUI(port, serverURL, this, "", sf);
    }

    public void run() throws Exception {
        boolean quit = false;
        State state = PRELOGIN;

        while (!quit) {
            switch (state) {
                case QUIT:
                    quit = true;
                    break;
                case PRELOGIN:
                    state = preloginUI.run();
                    break;
                case POSTLOGIN:
                    state = postloginUI.run();
                    break;
                case GAMEPLAY:
                    gameplayUI.setUsername(preloginUI.getUsername());
                    gameplayUI.setColor(postloginUI.getColor());
                    gameplayUI.setGameID(postloginUI.getGameID());
                    gameplayUI.setAuthToken(preloginUI.getAuthToken());
                    state = gameplayUI.run(postloginUI.getSpectating());
                    break;
            }
        }

        //server.stop();
    }

    public void notify(ServerMessage notification) {
        System.out.println(notification.getServerMessageType());
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            gameplayUI.setGame(((LoadGameMessage)notification).getGame());
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(((ErrorMessage)notification).getErrorMessage());
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println(((NotificationMessage)notification).getMessage());
        }
    }
}
