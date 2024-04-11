package client;

import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import server.Server;
import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URISyntaxException;

import static client.ui.State.PRELOGIN;

public class Repl implements NotificationHandler {
    private PreloginUI preloginUI;
    private PostloginUI postloginUI;
    private GameplayUI gameplayUI;

    private Server server;

    private int port;
    private String serverURL;

    public Repl(int port, String serverUrl) throws URISyntaxException, IOException {
        this.port = port;
        this.serverURL = serverUrl;

        server = new Server();
        port = server.run(port);

        preloginUI = new PreloginUI(port);
        postloginUI = new PostloginUI(port);
        gameplayUI = new GameplayUI(port, serverURL, this, "");
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
                    gameplayUI.setAuthToken(preloginUI.getAuthToken());
                    state = gameplayUI.run();
                    break;
            }
        }

        server.stop();
    }

    public void notify(ServerMessage notification) {
        System.out.println(notification.getServerMessageType());
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            gameplayUI.setGame(((LoadGameMessage)notification).getGame());
        }
    }
}
