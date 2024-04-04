package client;

import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import server.Server;
import webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URISyntaxException;

import static client.ui.State.PRELOGIN;

public class Repl implements NotificationHandler {
    private PreloginUI preloginUI;
    private PostloginUI postloginUI;
    private GameplayUI gameplayUI;

    private int port;
    private String serverURL;

    public Repl(int port, String serverUrl) throws URISyntaxException, IOException {
        preloginUI = new PreloginUI(port);
        postloginUI = new PostloginUI(port);
        gameplayUI = new GameplayUI(port);

        this.port = port;
        this.serverURL = serverUrl;
    }

    public void run() throws IOException, URISyntaxException {
        var server = new Server();
        port = server.run(port);

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
                    state = gameplayUI.run();
                    break;
            }
        }

        server.stop();
    }

    public void notify(ServerMessage notification) {
        //System.out.println(RED + notification.message());
        printPrompt();
    }

    private void printPrompt() {
        //System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
